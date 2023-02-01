ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaCrawler"
  )

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "4.8.0"
libraryDependencies += "io.github.bonigarcia" % "webdrivermanager" % "5.3.2"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "2.0.5"
libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"