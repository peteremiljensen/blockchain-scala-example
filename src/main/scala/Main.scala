package example

import dk.diku.blockchain._

object Main extends App {

  val node: Node = new Node(9000)

  while (true) {
    val ln = scala.io.StdIn.readLine()
    println(ln)
  }

}
