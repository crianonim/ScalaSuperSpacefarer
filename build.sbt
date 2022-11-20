ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaSuperSpacefarer",
    idePackagePrefix := Some("dev.jans")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test