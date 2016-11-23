package Client

/**
  * Created by remi on 09/11/16.
  */

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import Messages.Stop

object Client {
  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)
}

class Client(remote: InetSocketAddress, listener: ActorRef) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    case c @ Connected(remote, local) =>

      listener ! c

      val connection = sender()
      connection ! Register(self)

      context become {

        case s: String =>
          connection ! Write(ByteString.fromString(s))

        case CommandFailed(w: Write) =>
          // O/S buffer was full
          listener ! "write failed"

        case Received(data) =>
          listener ! data

        case Stop =>
          connection ! Close
          context stop self

        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }
}


