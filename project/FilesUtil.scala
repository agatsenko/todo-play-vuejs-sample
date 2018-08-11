/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-10
  */
import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._

import Loan.using

object FilesUtil {
  def isExistAndDir(path: Path): Boolean = {
    Files.exists(path) && Files.isDirectory(path)
  }

  def copyWithReplace(src: Path, dest: Path): Unit = {
    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
  }

  def copyDir(
      srcDirPath: Path,
      destDirPath: Path,
      include: Path => Boolean = _ => true,
      exclude: Path => Boolean = _ => false): Unit = {
    if (!Files.exists(srcDirPath) || !Files.isDirectory(srcDirPath)) {
      throw new IllegalArgumentException(s"'$srcDirPath' is not exist or not directory")
    }
    if (!Files.exists(destDirPath)) {
      Files.createDirectories(destDirPath)
    }

    using(Files.newDirectoryStream(srcDirPath)) {
      _.forEach { path =>
        if (include(path) && !exclude(path)) {
          val destPath = destDirPath.resolve(path.getFileName)
          copyWithReplace(path, destPath)
          if (Files.isDirectory(path)) {
            copyDir(path, destPath, include, exclude)
          }
        }
      }
    }
  }

  def delete(path: Path): Unit = {
    val absolutePath = path.normalize().toAbsolutePath
    if (Files.exists(absolutePath)) {
      if (Files.isDirectory(absolutePath)) {
        Files.walkFileTree(
          absolutePath,
          new SimpleFileVisitor[Path]() {
            override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
              Files.deleteIfExists(file)
              FileVisitResult.CONTINUE
            }

            override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
              Files.deleteIfExists(dir)
              FileVisitResult.CONTINUE
            }
          }
        )
      }
      else {
        Files.delete(absolutePath)
      }
    }
  }

  object ImplicitConverters {
    implicit def strToPath(str: String): Path = {
      Paths.get(str)
    }
  }
}
