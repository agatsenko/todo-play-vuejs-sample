libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.+",
  "org.flywaydb" % "flyway-core" % "5.0.+"
)

flywayUrl := "jdbc:h2:file:./db/todo-play-vuejs-slick-sample"
flywayLocations := Seq("classpath:db/migration")
flywaySqlMigrationPrefix := ""
flywaySqlMigrationSeparator := "-"
flywayRepeatableSqlMigrationPrefix := "repeatable-"
