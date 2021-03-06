package de.tototec

import org.sbuild.ExportDependencies
import org.sbuild.RichFile
import java.io.File

package object sbuild {

  implicit def toRichFile(file: File): RichFile = org.sbuild.toRichFile(file)

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type classpath = org.sbuild.classpath

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type CmdlineMonitor = org.sbuild.CmdlineMonitor

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Constants = org.sbuild.Constants

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val ExportDependencies = org.sbuild.ExportDependencies

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type include = org.sbuild.include

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type Logger = org.sbuild.Logger
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Logger = org.sbuild.Logger

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type MapperSchemeHandler = org.sbuild.MapperSchemeHandler

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Module = org.sbuild.Module
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Modules = org.sbuild.Modules

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Path = org.sbuild.Path

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Plugin = org.sbuild.Plugin

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type Project = org.sbuild.Project

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Prop = org.sbuild.Prop
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val SetProp = org.sbuild.SetProp

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type ScanSchemeHandler = org.sbuild.ScanSchemeHandler

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type SchemeHandler = org.sbuild.SchemeHandler
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val SchemeHandler = org.sbuild.SchemeHandler
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type SchemeResolver = org.sbuild.SchemeResolver
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type TransparentSchemeResolver = org.sbuild.TransparentSchemeResolver
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type SideeffectFreeSchemeResolver = org.sbuild.SideeffectFreeSchemeResolver
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type CacheableSchemeResolver = org.sbuild.CacheableSchemeResolver

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type Target = org.sbuild.Target
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val Target = org.sbuild.Target

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type TargetContext = org.sbuild.TargetContext

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type TargetRef = org.sbuild.TargetRef
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val TargetRef = org.sbuild.TargetRef

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type TargetRefs = org.sbuild.TargetRefs
  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  val TargetRefs = org.sbuild.TargetRefs

  @deprecated("Package de.tototec.sbuild was renamed to org.sbuild.", "0.7.2.9000")
  type version = org.sbuild.version

}
