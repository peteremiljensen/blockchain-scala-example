name := """freechain-example"""
organization := "dk.diku"

version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies += "dk.diku" %% "freechain" % "1.0" % "test"

scalacOptions += "-deprecation"
scalacOptions += "-feature"