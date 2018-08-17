addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")
// https://github.com/tototoshi/sbt-slick-codegen
addSbtPlugin("com.github.tototoshi" % "sbt-slick-codegen" % "1.3.0")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.16")

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.+",
)