name := """todo-play-vuejs-sample"""
organization := "com.agat"

version := "0.1.0"

scalaVersion := "2.12.6"

lazy val flyway = (project in file("modules/flyway")).
    enablePlugins(FlywayPlugin).
    settings(Common.projectSettings)

lazy val core = (project in file("modules/core")).
    enablePlugins(CodegenPlugin).
    settings(Common.projectSettings)

lazy val root = (project in file(".")).
    enablePlugins(PlayScala).
    aggregate(core).
    dependsOn(core)

// compile dependencies
libraryDependencies ++= Seq(
  guice,
  "com.h2database" % "h2" % "1.4.+",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.zaxxer" % "HikariCP" % "2.7.+",
)

// test dependencies
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
)

clientProjectPath := "./client"
clientDistDirPath := "./client/dist"
clientAssetsDirDestPath := "./public/todo"
clientSrcIndexHtmlName := "index.html"
clientDestIndexHtmlPath := "./app/views/todo.scala.html"

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

lazy val npmInstallIfNeed = taskKey[Unit]("run 'npm install' if node_modules dir is absent in client project")

npmInstallIfNeed := {
  NpmTasks.npmInstallIfNeed(clientProjectPath.value)
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
    compile in Compile
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
