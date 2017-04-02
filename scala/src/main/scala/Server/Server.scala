package Server

/**
  * Created by remi on 09/11/16.
  */

import java.net.InetSocketAddress

import Messages.{HandlerStop, Send}
import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import Messages.{SpreadData, Stop}

object Server {
  def props(adr: String) =
    Props(classOf[Server], adr)
}

class Server(adr: String) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(adr, 5150))

  var clientHandlers: List[ActorRef] = List()

  def receive = {
    case b @ Bound(localAddress) =>
     println("Server bound on "+localAddress.toString)

    case HandlerStop =>
      println("Client disconnected")
      clientHandlers = clientHandlers filter(_ != sender())

    case CommandFailed(_: Bind) => context stop self

    case SpreadData(data: ByteString) => clientHandlers filter(_ != sender()) foreach(_ ! Send(data))


    case c @ Connected(remote, local) =>
      println("Distant client connected : "+remote.toString)
      val connection = sender()
      val handler = context.actorOf(SimplisticHandler.props(connection, remote))
      clientHandlers = handler :: clientHandlers
      connection ! Register(handler)

    case Stop =>
      context.children foreach(_ ! Stop)
      println("Stopping Server")
      context stop self

  }

}
