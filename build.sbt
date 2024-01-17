ThisBuild / scalaVersion := "2.13.6"

lazy val examples = (project in file("modules/examples"))
  .settings(
    libraryDependencies ++= Dependencies.sparkExamplesDeps
  )

lazy val root = (project in file("."))
  .settings(name := "jvm-examples")
  .aggregate(
    examples
  )
