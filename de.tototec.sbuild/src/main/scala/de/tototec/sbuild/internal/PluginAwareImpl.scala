package de.tototec.sbuild.internal

import scala.reflect.ClassTag
import scala.reflect.classTag
import de.tototec.sbuild.Plugin
import de.tototec.sbuild.PluginAware
import de.tototec.sbuild.Project
import de.tototec.sbuild.ProjectConfigurationException
import de.tototec.sbuild.Logger

trait PluginAwareImpl extends PluginAware { projectSelf: Project =>

  class RegisteredPlugin(val instanceClassName: String,
                         val factoryClassName: String,
                         val version: String,
                         val classLoader: ClassLoader) {

    private[this] val log = Logger[RegisteredPlugin]

    lazy val pluginClass: Class[_] = try {
      log.debug("About to load plugin factroy class: " + instanceClassName)
      val t = classLoader.loadClass(factoryClassName)
      if (!classOf[Plugin[_]].isAssignableFrom(t)) {
        // TODO specific exception
        val ex = new ProjectConfigurationException(s"Plugin factory class ${factoryClassName} does not implement ${classOf[Plugin[_]].getName} trait.")
        ex.buildScript = Some(projectSelf.projectFile)
        throw ex
      }
      t
    } catch {
      case e: ClassNotFoundException =>
        val ex = new ProjectConfigurationException(s"Plugin factory class ${factoryClassName} could not be loaded.", e)
        ex.buildScript = Some(projectSelf.projectFile)
        throw ex
    }

    lazy val instanceClass: Class[_] = try {
      log.debug("About to load plugin instance class: " + instanceClassName)
      val t = classLoader.loadClass(instanceClassName)
      if (!classOf[Plugin[_]].isAssignableFrom(t)) {
        // TODO specific exception
        val ex = new ProjectConfigurationException(s"Plugin instance class ${factoryClassName} does not implement ${classOf[Plugin[_]].getName} trait.")
        ex.buildScript = Some(projectSelf.projectFile)
        throw ex
      }
      t
    } catch {
      case e: ClassNotFoundException =>
        val ex = new ProjectConfigurationException(s"Plugin instance class ${factoryClassName} could not be loaded.", e)
        throw ex
    }

    lazy val factory: Plugin[_] = {
      val fac = pluginClass.getConstructors().find(c => c.getParameterTypes().size == 1 && c.getParameterTypes()(0) == classOf[Project]) match {
        case Some(ctr) =>
          log.debug("Creating a plugin instance with constructor: " + ctr)
          ctr.newInstance(projectSelf)
        case None =>
          pluginClass.getConstructors().find(c => c.getParameterTypes().size == 0) match {
            case Some(ctr) =>
              log.debug("Creating a plugin instance with constructor: " + ctr)
              ctr.newInstance()
            case None =>
              log.debug("Could not found any supported constructors: Found these: " + pluginClass.getConstructors().mkString("\n  "))
              throw new ProjectConfigurationException("Could not found any suitable constructors in plugin class " + pluginClass.getName)
          }
      }
      fac.asInstanceOf[Plugin[_]]
    }

    case class Instance(name: String, obj: Any, modified: Boolean)

    private[this] var _instances: Seq[Instance] = Seq()

    private def innerGet(name: String): Instance = _instances.find(_.name == name) match {
      case Some(i) =>
        log.debug("get(" + name + ") will return an already instantiated instance: " + i.obj)
        i
      case None =>
        log.debug("get(" + name + ") triggered the creation of a new instance")
        val instance = factory.create(name)
        val wrappedI = Instance(name, instance, false)
        _instances ++= Seq(wrappedI)
        log.debug("Created and return new plugin instance: " + instance)
        wrappedI
    }

    def get(name: String): Any = innerGet(name).obj
    def isModified(name: String): Boolean = innerGet(name).modified

    def update(name: String, update: Any => Any): Unit = {
      val instance = get(name)
      val updatedInstance = update(instance)
      _instances = _instances.map {
        case Instance(n, i, _) if n == name => Instance(n, updatedInstance, true)
        case x => x
      }
    }

    def getInstanceNames: Seq[String] = _instances.map(_.name)
    def getAll: Seq[Any] = _instances.map(_.obj)

    def applyToProject: Unit = {
      if (!_instances.isEmpty) {
        log.debug("About to run applyToProject for plugin: " + this)
        // TODO: cover this with a unit test to detect refactorings at test time
        try {
          factory.
            asInstanceOf[{ def applyToProject(instances: Seq[(String, Any)]) }].
            applyToProject(_instances.map(i => (i.name -> i.obj)))
        } catch {
          case e: ClassCastException =>
            val ex = new ProjectConfigurationException("Plugin configuration could to be applied to project: " + instanceClassName)
            ex.buildScript = Some(projectSelf.projectFile)
            throw ex
        }
      }
    }

    override def toString: String = getClass.getSimpleName +
      "(instanceClassName=" + instanceClassName +
      ",factoryClassName=" + factoryClassName +
      ",version=" + version +
      ",classLoader=" + classLoader +
      ",instances=" + _instances + ")"
  }

  private[this] val log = Logger[PluginAwareImpl]

  // we assume, plugins are registered in that order so that dependencies are already registered before
  private[this] var _plugins: Seq[RegisteredPlugin] = Seq()

  def registerPlugin(instanceClassName: String, factoryClassName: String, version: String, classLoader: ClassLoader) = {
    val reg = new RegisteredPlugin(instanceClassName, factoryClassName, version, classLoader)
    log.debug("About to register plugin: " + reg)
    _plugins ++= Seq(reg)
  }

  //  // TODO: check, that same instance type is not registered by two different plugins
  //  override def registerPlugin(plugin: Plugin[_]): Unit = {
  //    _plugins ++= Seq(new RegisteredPlugin(plugin, config))
  //  }

  override def findOrCreatePluginInstance[T: ClassTag](name: String): T =
    withPlugin[T, T] { rp =>
      rp.get(name).asInstanceOf[T]
    }

  def findAndUpdatePluginInstance[T: ClassTag](name: String, updater: T => T): Unit =
    withPlugin[T, Unit] { rp =>
      rp.update(name, { instance => updater(instance.asInstanceOf[T]) })
    }

  override def isPluginInstanceModified[T: ClassTag](name: String): Boolean =
    withPlugin[T, Boolean] { rp =>
      rp.isModified(name)
    }

  private def withPlugin[T: ClassTag, R](action: RegisteredPlugin => R): R = {
    log.debug("About to activate and access a plugin instance " + classTag[T].runtimeClass.getName)
    _plugins.find { rp =>
      log.debug("checking " + rp)
      val searchedClass = classTag[T].runtimeClass
      rp.instanceClassName == searchedClass.getName && rp.classLoader == searchedClass.getClassLoader
    } match {
      case Some(regPlugin) =>
        action(regPlugin)
      case None =>
        val ex = new ProjectConfigurationException("No plugin registered with instance type: " + classTag[T].runtimeClass.getName)
        ex.buildScript = Some(projectSelf.projectFile)
        throw ex
    }
  }

  override def finalizePlugins: Unit = _plugins.map(_.applyToProject)

  case class PluginInfo(override val name: String,
                        override val version: String,
                        override val instances: Seq[String])
      extends Plugin.PluginInfo

  override def registeredPlugins: Seq[PluginInfo] = {
    _plugins.map { p =>
      PluginInfo(name = p.instanceClassName, p.version, instances = p.getInstanceNames)
    }
  }

}