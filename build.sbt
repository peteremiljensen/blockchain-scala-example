name := """freechain-example"""
organization := "dk.diku"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "dk.diku" %% "freechain" % "1.0" % "test"

scalacOptions += "-deprecation"
scalacOptions += "-feature"