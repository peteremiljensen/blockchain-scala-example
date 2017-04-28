import sbt._
import Keys._

/**
 * based on https://github.com/harrah/xsbt/wiki/Getting-Started-Multi-Project
 */
object ExampleBuild extends Build {

  // aggregate: running a task on the aggregate project will also run it on the aggregated projects.
  // dependsOn: a project depends on code in another project.
  // without dependsOn, you'll get a compiler error: "object bar is not a member of package
  // com.alvinalexander".
  lazy val root = Project(id = "example",
                          base = file(".")) aggregate(blockchain) dependsOn(blockchain)

  // sub-project in the Foo subdirectory
  lazy val blockchain = Project(id = "blockchain",
                         base = file("Blockchain"))
}