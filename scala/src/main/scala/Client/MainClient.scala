package Client

import java.net.InetSocketAddress

import Messages.Stop
import akka.actor._

import scala.io.StdIn


object MainClient {

  def main(args: Array[String]): Unit = {

    val adr = args(0)

    val remote = new InetSocketAddress(adr, 5150)

    val system = ActorSystem("Colivia_Client")

    val listener = system.actorOf(Props[Listener])

    val client = system.actorOf(Client.props(remote, listener))

    val inputHandler = new ClientInputHandler(client)

    // handle input of client
    while(!inputHandler.handle(StdIn.readLine)) {}

    listener ! PoisonPill
    client ! Stop

    system terminate


  }

}
