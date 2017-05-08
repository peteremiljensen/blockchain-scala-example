package example

import org.json4s._
import org.json4s.native.JsonMethods._
import akka.actor.ActorSystem
import dk.diku.blockchain._

object Main extends App {

  def loafVal(l: Loaf) =
    l.calculateHash == l.hash

  def blockVal(b: Block) =
    b.calculateHash == b.hash && b.hash.substring(0,4) == "0000"

  def consensusCheck(localLength: Integer, recLength: Integer) =
    localLength < recLength

  def consensus(localChain: List[Block], recChain: List[Block]) =
    if (localChain.length > recChain.length)
      localChain
    else
      recChain

  implicit val validator = Validator(loafVal, blockVal,
    consensusCheck, consensus)
  implicit val system = ActorSystem()

  val port: Integer = if (args.length > 0) args(0).toInt else 9000
  val node: Node = new Node(port)

  val genesisBlock = Block(Seq(), 0, "-1", "2017-05-01 15:16:52.579123",
    JObject("nounce" -> JInt(27413)),
    "000077dbf86e9c0d593ac746a0658d88b966ddd0a132dcf9294c23a929ed4573")

  if (node.addBlock(genesisBlock)) {
    while (true) {
      print("(freechain) ")
      val args = scala.io.StdIn.readLine().split(" ")
      args match {
        case Array("loaf", data) =>
          node.addLoaf(Loaf.generateLoaf(data))
        /*case Array("mine") =>
         node.*/
        case Array("print", "blocklength") => println(node.getLength)
        case Array("print", "blockchain") => println(node.getChain)
        case Array("connect", ip, port) => node.connect(ip, port.toInt)
        case Array("") =>
        case _ => println("*** unknown function")
      }
    }
  } else {
    println("*** could not validate genesisBlock: ") //+ genesisBlock.toJson)
  }

}
