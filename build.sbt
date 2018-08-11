name := """todo-play-vuejs-sample"""
organization := "com.agat"

version := "0.1.0"

clientProjectPath := "./client"
clientDistDirPath := "./client/dist"
clientAssetsDirDestPath := "./public/client"
clientSrcIndexHtmlName := "index.html"
clientDestIndexHtmlPath := "./app/views/index_client.scala.html"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.agat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.agat.binders._"

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// custom setting keys

lazy val clientProjectPath = settingKey[String]("defines path to npm project")
lazy val clientDistDirPath = settingKey[String]("defines path to client distributive files")
lazy val clientAssetsDirDestPath = settingKey[String]("defines destination path of client assets dir")
lazy val clientSrcIndexHtmlName = settingKey[String]("defines name of source client index.html")
lazy val clientDestIndexHtmlPath = settingKey[String]("defines destination path of client index.html")

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// npm task

lazy val npm = inputKey[Unit]("runs the npm command")

npm := {
  NpmTasks.npm(clientProjectPath.value, NpmTasks.npmArgsParser.parsed)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// npm install task

lazy val npmInstall = taskKey[Unit]("runs 'npm install' in the npm project dir")

npmInstall := {
  NpmTasks.npmInstall(clientProjectPath.value)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// npm update task

lazy val npmUpdate = taskKey[Unit]("runs 'npm update' in the npm project dir")

npmUpdate := {
  NpmTasks.npmUpdate(clientProjectPath.value)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// npm run tasks

lazy val npmRun = inputKey[Unit]("run 'npm run <string>' in the npm project dir")

npmRun := {
  NpmTasks.npmRun(clientProjectPath.value, NpmTasks.npmRunScriptArgParser.parsed)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// client tasks

lazy val clientClean = taskKey[Unit]("deletes client files produced by build")
lazy val clientCompile = taskKey[Unit]("compiles client sources and deliver their to play dirs")
lazy val clientTest = taskKey[Unit]("runs tests of client project")

clientClean := {
  import FilesUtil.ImplicitConverters.strToPath

  ClientTasks.clean(
    clientProjectPath.value,
    clientAssetsDirDestPath.value,
    clientDestIndexHtmlPath.value
  )
}

clientCompile := {
  import FilesUtil.ImplicitConverters.strToPath

  ClientTasks.compile(
    clientProjectPath.value,
    clientDistDirPath.value,
    clientAssetsDirDestPath.value,
    clientSrcIndexHtmlName.value,
    clientDestIndexHtmlPath.value,
  )
}

clientTest := {
  import FilesUtil.ImplicitConverters.strToPath

  ClientTasks.test(clientProjectPath.value)
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// client & server tasks

lazy val cleanFull = taskKey[Unit]("runs 'clientClean' and 'clean' tasks")
lazy val compileFull = taskKey[Unit]("runs 'clientCompile' and 'compile' tasks")
lazy val testFull = taskKey[Unit]("runs 'test' and 'clientTest' tasks")
lazy val build = taskKey[Unit]("runs the following tasks: clientClean, clean, clientCompile, compile, clientTest, test")

cleanFull := {
  clean.value
  clientClean.value
}

compileFull := {
  Def.sequential(
    clientCompile,
    (compile in Compile)
  ).value
}

testFull := {
  Def.sequential(
    clientTest,
    test in Test,
  ).value
}

build := {
  Def.sequential(
    cleanFull,
    compileFull,
    testFull,
  ).value
}
