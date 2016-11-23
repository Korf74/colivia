package Server

/**
  * Created by remi on 09/11/16.
  */

import java.net.InetSocketAddress

import Messages.{Stop, HandlerStop, SpreadData, Send}
import akka.actor.{Props, Actor, ActorRef}
import akka.io.Tcp
import akka.util.ByteString

object SimplisticHandler {
  def props(connection: ActorRef, adr: InetSocketAddress) =
    Props(classOf[SimplisticHandler], connection, adr)
}

class SimplisticHandler(connection: ActorRef, adr: InetSocketAddress) extends Actor {
  import Tcp._

  def receive = {
    case Received(data: ByteString) =>
      println("Handler for "+adr.toString+" received : "+data.utf8String)
      context.parent ! SpreadData(ByteString.fromString(adr.toString+": ") ++ data)
    case Send(data: ByteString) => connection ! Write(data)

    case PeerClosed =>
      context.parent ! HandlerStop
      connection ! Close
      context stop self

    case Stop =>
      connection ! ConfirmedClose
      context become {
        case ConfirmedClose => context stop self
      }
  }
}
