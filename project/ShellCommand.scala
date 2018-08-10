/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-09
  */
import scala.sys.process.{Process, ProcessBuilder}

import java.nio.file.{Path, Paths}

import sbt.File

object ShellCommand {
  private val _isWindowsOs = System.getProperty("os.name") match {
    case null => false
    case osName => osName.toLowerCase().startsWith("win")
  }

  def shellCmd(cmd: Seq[String], cwd: Option[Path], extraEnv: (String, String)*): ProcessBuilder =
    Process(normalizeCmd(cmd), toFileOpt(cwd), extraEnv: _*)

  def shellCmd(cmd: Seq[String], extraEnv: (String, String)*): ProcessBuilder =
    shellCmd(cmd, None, extraEnv: _*)

  def shellCmd(cmd: String, cwd: Option[Path], extraEnv: (String, String)*): ProcessBuilder =
    Process(normalizeCmd(cmd), toFileOpt(cwd), extraEnv: _*)

  def shellCmd(cmd: String, extraEnv: (String, String)*): ProcessBuilder =
    shellCmd(cmd, None, extraEnv: _*)

  private def toFileOpt(cwd: Option[Path]): Option[File] =
    cwd match {
      case Some(cwdPath) => Some(cwdPath.normalize().toAbsolutePath.toFile)
      case _ => None
    }

  private def normalizeCmd(cmd: String): String =
    if (_isWindowsOs) s"cmd /C $cmd" else cmd

  private def normalizeCmd(cmd: Seq[String]): Seq[String] =
    if (_isWindowsOs) Seq("cmd", "/C") ++ cmd else cmd

  object Implicits {
    implicit def strToPathOpt(str: String): Option[Path] =
      if (str == null || str.trim.isEmpty) None else Some(Paths.get(str.trim))

    implicit def fileToPathOpt(file: File): Option[Path] =
      if (file == null) None else Some(file.toPath)
  }
}
