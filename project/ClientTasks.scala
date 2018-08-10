/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-10
  */
import java.nio.file.{Files, Path, Paths}

import NpmTasks.npmRun

object ClientTasks {
  def clean(clientProjectPath: Path, assetsDirPath: Path, indexHtmlDestPath: Path): Unit = {
    npmRun(clientProjectPath.toAbsolutePath.toString, "sbt:clean")

    if (Files.exists(assetsDirPath)) {
      Files.newDirectoryStream(assetsDirPath).forEach(p => FilesUtil.delete(p))
    }
    FilesUtil.delete(indexHtmlDestPath)
  }

  def compile(
      clientProjectPath: Path,
      clientDistDirPath: Path,
      assetsDirPath: Path,
      srcIndexHtmlName: Path,
      destIndexHtmlPath: Path): Unit = {
    npmRun(clientProjectPath.toAbsolutePath.toString, "sbt:compile")

    // copy assets
    if (!Files.exists(assetsDirPath)) {
      Files.createDirectories(assetsDirPath)
    }
    FilesUtil.copy(
      srcDirPath = clientDistDirPath,
      destDirPath = assetsDirPath,
      exclude = path => path.getFileName == srcIndexHtmlName
    )

    // copy index.html
    if (!Files.exists(destIndexHtmlPath.getParent)) {
      Files.createDirectories(destIndexHtmlPath.getParent)
    }
    FilesUtil.copy(clientDistDirPath.resolve(srcIndexHtmlName), destIndexHtmlPath)
  }
}
