package de.tototec.sbuild.runner

import de.tototec.cmdoption.CmdOption

class Config {
  @CmdOption(names = Array("--buildfile", "-f"), args = Array("FILE"),
    description = "The buildfile to use (default: SBuild.scala).")
  var buildfile = "SBuild.scala"

  @CmdOption(names = Array("--additional-buildfile", "-F"), args = Array("FILE"), maxCount = -1,
    description = "Add an additional buildfile into scope.")
  def addAdditionalBuildfiles(buildfile: String) {
    additionalBuildfiles ++= Seq(buildfile)
  }
  var additionalBuildfiles: Seq[String] = Seq()

  @CmdOption(names = Array("--verbose", "-v"), description = "Be verbose when running.")
  var verbose = false

  @CmdOption(names = Array("--list-targets", "-l"),
    description = "Show a list of targets defined in the current buildfile.")
  var listTargets = false

  @CmdOption(names = Array("--list-targets-recursive", "-L"),
    description = "Show a list of targets defined in the current buildfile and all modules.")
  var listTargetsRecursive = false

  @CmdOption(names = Array("--list-modules"),
    description = "Show a list of modules involved in this project.")
  var listModules = false

  @CmdOption(names = Array("--define", "-D"), args = Array("KEY=VALUE"), maxCount = -1,
    description = "Define or override properties. If VALUE is omitted it defaults to \"true\".")
  def addDefine(keyValue: String) {
    keyValue.split("=", 2) match {
      case Array(key, value) => defines.put(key, value)
      case Array(key) => defines.put(key, "true")
    }
  }
  val defines: java.util.Map[String, String] = new java.util.LinkedHashMap()

  @CmdOption(names = Array("--execution-plan"), description = "Show the execution plan and exit.")
  val showExecutionPlan = false

  @CmdOption(names = Array("--clean"),
    description = "Remove all generated output and caches. This will force a new compile of the buildfile. (This will not remove output generated by buildfiles.)")
  var clean: Boolean = false

  @CmdOption(names = Array("--create-stub"),
    description = "Create a new minimal SBuild.scala file to start with.")
  var createStub: Boolean = false

  @CmdOption(args = Array("TARGETS"), maxCount = -1, description = "The target(s) to execute (in order).")
  val params = new java.util.LinkedList[String]()

  @CmdOption(names = Array("--dependency-tree"), description = "Show dependency tree(s) and exit.")
  var showDependencyTree = false

  @CmdOption(names = Array("-q", "--quiet", "--no-progress"), description = "Quiet mode. Don't show progress messages with progress in percent. (This will speed up SBuild initialization.)")
  var noProgress = false

  @CmdOption(names = Array("--check"),
    description = "Check targets for cycles and missing scheme handlers.")
  var check = false

  @CmdOption(names = Array("--check-recursive"),
    description = "Check targets of this project and all its modules for cycles and missing scheme handlers.")
  var checkRecusive = false

  @CmdOption(names = Array("--just-clean"),
    description = "Remove all generated output and caches without reading any buildfile. This will essentially remove the \".sbuild\" directory in the current directory.")
  var justClean: Boolean = false

  @CmdOption(names = Array("--just-clean-recursive"),
    description = "Remove all generated output and caches without reading any buildfile. This will essentially remove the \".sbuild\" directory in the current directory and all sub-directories, no matter, if sub-directories contain SBuild projects or not.")
  var justCleanRecursive: Boolean = false

  @CmdOption(names = Array("--experimental-parallel"),
    description = "Experimental: Enable parallel procession of targets.")
  var parallelProcessing: Boolean = false

  @CmdOption(names = Array("--jobs", "-j"), args = Array("N"), description = "Experimental: Allow processing N targets in parallel.")
  def setParallelJobs(jobs: Int) {
    parallelProcessing = true
    parallelJobs = jobs
  }
  var parallelJobs: Int = 0

}