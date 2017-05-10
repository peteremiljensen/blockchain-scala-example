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

  def mine(loaves: Seq[Loaf], previousBlock: Block) = {
    var nounce: Int = 0
    var block: Block = Block.generateBlock(loaves, previousBlock, JObject())
    do {
      val data: JValue = JObject("nounce" -> JInt(nounce))
      block = Block.generateBlock(loaves, previousBlock, data)
      nounce = nounce + 1
    } while (!block.validate)
      block
  }

  val port: Integer = if (args.length > 0) args(0).toInt else 9000
  val node: Node = new Node(port)
  val genesisBlock = Block(Seq(), 0, "-1", "2017-05-01 15:16:52.579123",
    JObject("nounce" -> JInt(27413)),
    "000077dbf86e9c0d593ac746a0658d88b966ddd0a132dcf9294c23a929ed4573")

  val events = new Events ({
    case Events.ConnectionReady => println("*** new connection")
    case _ =>
  })

  if (node.addBlock(genesisBlock)) {
    print("(freechain) ")
    while (true) {
      val in = scala.io.StdIn.readLine()
      if (in != null) {
        print("(freechain) ")
        val args = in.split(" ")
        args match {
          case Array("loaf", data) =>
            node.addLoaf(Loaf.generateLoaf(data))
          case Array("mine") =>
            (node.getLoaves(1000), node.getLength) match {

              case (Right(loaves: Seq[Loaf] @unchecked), Right(length: Int)) =>
                node.getBlock(length-1) match {

                  case Right(Some(block: Block)) =>
                    if (!node.addBlock(mine(loaves, block)))
                      println("*** error adding mined block")
                  case _ => println("*** error getting block")
                }
              case _ => println("*** error getting chain info")
            }
          case Array("print", "blocklength") => println(node.getLength)
          case Array("print", "blockchain") => println(node.getChain)
          case Array("print", "loafpool") => println(node.getLoaves(20))
          case Array("connect", ip, port) => node.connect(ip, port.toInt)
          case Array("quit") | Array("exit") => node.exit; System.exit(0)
          case Array("") =>
          case _ => println("*** unknown function")
        }
      }
    }
  } else {
    println("*** could not validate genesisBlock: ") //+ genesisBlock.toJson)
  }

}
