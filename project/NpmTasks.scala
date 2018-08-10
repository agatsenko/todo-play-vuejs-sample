/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-09
  */
import scala.sys.process.ProcessBuilder

import ShellCommand._
import sbt.complete.DefaultParsers._
import sbt.complete.Parser

object NpmTasks {
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // npm task

  lazy val npmArgsParser: Parser[Seq[String]] = Space ~> StringBasic +

  def npm(npmProjectPath: String, args: Seq[String], errorMsg: Int => Option[String] = _ => None): Unit = {
    import Implicits.strToPathOpt

    runShellCmd(
      shellCmd(Seq("npm") ++ args, npmProjectPath),
      returnCode => errorMsg(returnCode) match {
        case msgOpt: Some[String] => msgOpt
        case _ => Some(s"the npm task has been failed with #$returnCode return code")
      }
    )
  }

  def npm(npmProjectPath: String, arg: String, errorMsg: Int => Option[String]): Unit = {
    npm(npmProjectPath, Seq(arg), errorMsg)
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // npm install task

  def npmInstall(npmProjectPath: String): Unit = {
    npm(
      npmProjectPath,
      "install",
      (returnCode: Int) => Some(s"'npm install' has been failed with #$returnCode return code")
    )
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // npm update task

  def npmUpdate(npmProjectPath: String): Unit = {
    npm(
      npmProjectPath,
      "update",
      (returnCode: Int) => Some(s"'npm update' has been failed with #$returnCode return code")
    )
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // npm run task

  lazy val npmRunScriptArgParser = PredefinedParsers.oneString

  def npmRun(npmProjectPath: String, script: String): Unit = {
    npm(
      npmProjectPath,
      Seq("run", script),
      (returnCode: Int) => Some(s"'npm run $script' has been failed with #$returnCode return code")
    )
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // internal

  private def runShellCmd(pb: ProcessBuilder, errorMessage: Int => Option[String] = _ => None): Unit = {
    val returnCode = pb.!
    if (returnCode != 0) {
      sys.error(
        errorMessage(returnCode) match {
          case Some(msg) => msg
          case _ => s"shell command has been failed with $returnCode error code"
        }
      )
    }
  }

  private object PredefinedParsers {
    val oneString: Parser[String] = Space ~> StringBasic
    val optOneString: Parser[Option[String]] = (Space ~> StringBasic).?
  }
}
