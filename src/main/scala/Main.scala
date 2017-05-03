package example

import akka.actor.ActorSystem
import dk.diku.blockchain._

object Main extends App {

  implicit val validator = Validator((l: Loaf) => true, (b: Block) => true)
  implicit val system = ActorSystem()
  val node: Node = new Node(9000)

  while (true) {
    print("(freechain) ")
    val args = scala.io.StdIn.readLine().split(" ")
    args match {
      case Array("loaf", data) =>
        node.addLoaf(Loaf.generateLoaf(data))
      case Array("print", "blocklength") => println(node.getLength)
      case Array("") =>
      case _ => println("*** unknown function")
    }
  }

}
