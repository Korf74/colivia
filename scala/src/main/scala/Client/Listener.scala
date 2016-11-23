package Client

import akka.actor.Actor
import akka.io.Tcp.Connected
import akka.util.ByteString

/**
  * Created by remi on 22/11/16.
  */
class Listener extends Actor {
  def receive = {
    case data: ByteString =>
      println(data.utf8String)

    case data: String => println("client received : "+data)

    case c @ Connected(remote, local) => println("connected")

    case _ =>
  }
}
