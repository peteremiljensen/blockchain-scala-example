package example

import dk.diku.blockchain._

object Main extends App {

  val node: Node = new Node(9000)

  while (true) {

    val args = scala.io.StdIn.readLine().split(" ")
    args match {
      case Array("loaf", data) => println(do_loaf(data))
      case Array("print", "blocklength") => println(node.getLength)
      case _ => println("*** unknown function")
    }

  }

  def do_loaf(data: String): Loaf = Loaf.generateLoaf(data)

}
