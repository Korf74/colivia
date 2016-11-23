package Server

/**
  * Created by remi on 09/11/16.
  */

import java.net.InetSocketAddress

import Messages.{Stop, HandlerStop, SpreadData, Send}
import akka.actor.{Actor, ActorRef}
import akka.io.Tcp
import akka.util.ByteString

class SimplisticHandler(connection: ActorRef, adr: InetSocketAddress) extends Actor {
  import Tcp._

  def receive = {
    case Received(data: ByteString) =>
      println("Handler for "+adr.toString+" received : "+data.utf8String)
      context.parent ! SpreadData(ByteString.fromString(adr.toString+": ") ++ data)
    case Send(data: ByteString) => connection ! Write(data)

    case PeerClosed =>
      context.parent ! HandlerStop
      context stop self

    case Stop =>
      connection ! Close
      context stop self

  }
}
