import akka.actor._
import akka.io.Tcp.{Write, Connected}
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress


import akka.util.ByteString

/**
  * Created by remi on 09/11/16.
  */

object Stop

class Listener extends Actor {
  def receive = {
    case data: ByteString =>
      println("client received : "+data.utf8String)

    case data: String => println("client received : "+data)

    case c @ Connected(remote, local) => println("connected")

    case _ =>
  }
}

object MainServer {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Colivia_Server")

    val server = system.actorOf(Props[Server], "Server")

    var s = ""

    while (!s.equals("stop")) {
      s = System.console().readLine()
      server ! s
    }

    server ! PoisonPill

    System.exit(1)
  }

}

object MainClient {

  def main(args: Array[String]): Unit = {
    val remote = new InetSocketAddress("localhost", 5150)

    val system = ActorSystem("Colivia_Client")

    val listener = system.actorOf(Props[Listener])

    val client = system.actorOf(Client.props(remote, listener))

    var s = ""

    while (!s.equals("stop")) {
      s = System.console().readLine()
      client ! s
    }

    listener ! PoisonPill
    client ! Stop

    System.exit(1)
  }

}
