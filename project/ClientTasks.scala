/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-10
  */
import java.nio.file.{Files, Path}

import Loan.using
import NpmTasks.{npmRun, npmInstallIfNeed}

object ClientTasks {
  def clean(clientProjectPath: Path, assetsDirPath: Path, indexHtmlDestPath: Path): Unit = {
    npmInstallIfNeed(clientProjectPath.toString)
    npmRun(clientProjectPath.toString, "sbt:clean")

    if (Files.exists(assetsDirPath)) {
      using(Files.newDirectoryStream(assetsDirPath))(_.forEach(p => FilesUtil.delete(p)))
    }
    FilesUtil.delete(indexHtmlDestPath)
  }

  def compile(
      clientProjectPath: Path,
      clientDistDirPath: Path,
      assetsDirPath: Path,
      srcIndexHtmlName: Path,
      destIndexHtmlPath: Path): Unit = {
    npmInstallIfNeed(clientProjectPath.toString)
    npmRun(clientProjectPath.toString, "sbt:compile")

    // copy assets
    if (!Files.exists(assetsDirPath)) {
      Files.createDirectories(assetsDirPath)
    }
    FilesUtil.copyDir(
      srcDirPath = clientDistDirPath,
      destDirPath = assetsDirPath,
      exclude = path => path.getFileName == srcIndexHtmlName
    )

    // copy index.html
    if (!Files.exists(destIndexHtmlPath.getParent)) {
      Files.createDirectories(destIndexHtmlPath.getParent)
    }
    FilesUtil.copyWithReplace(clientDistDirPath.resolve(srcIndexHtmlName), destIndexHtmlPath)
  }

  def test(clientProjectPath: Path): Unit = {
    npmInstallIfNeed(clientProjectPath.toString)
    npmRun(clientProjectPath.toString, "sbt:test")
  }
}
