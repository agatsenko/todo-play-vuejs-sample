/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-10
  */
import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._

object FilesUtil {
  def isExistAndDir(path: Path): Boolean = {
    Files.exists(path) && Files.isDirectory(path)
  }

  def copy(src: Path, dest: Path): Unit = {
    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES)
  }

  def copy(
      srcDirPath: Path,
      destDirPath: Path,
      include: Path => Boolean = _ => true,
      exclude: Path => Boolean = _ => false): Unit = {
    if (!isExistAndDir(srcDirPath)) {
      throw new IllegalArgumentException(s"'$srcDirPath' is not exist or directory")
    }
    if (!isExistAndDir(destDirPath)) {
      throw new IllegalArgumentException(s"'$destDirPath' is not exist or directory")
    }
    srcDirPath.getFileName

    Files.newDirectoryStream(srcDirPath).forEach { path =>
      if (include(path) && !exclude(path)) {
        copy(path, destDirPath.resolve(path.getFileName))
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
}
