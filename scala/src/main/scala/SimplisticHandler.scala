/**
  * Created by remi on 09/11/16.
  */

import java.net.InetSocketAddress

import akka.actor.{ActorRef, Actor}
import akka.io.Tcp
import akka.util.ByteString

class SimplisticHandler extends Actor {
  import Tcp._

  var clients: List[ActorRef] = List()

  def receive = {
    case Received(data) =>
      println("Server received : "+data.utf8String)
      clients foreach(_ ! Write(data))

    case connection: ActorRef =>
      clients = connection :: clients

    case ListClients => clients foreach(println)

    case PeerClosed => context stop self

  }
}
