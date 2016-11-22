/**
  * Created by remi on 09/11/16.
  */

import akka.actor.{ActorRef, Actor, Props}
import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress

class Server extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 5150))

  var clients: List[ActorRef] = List()

  def receive = {
    case b @ Bound(localAddress) =>
     println("Server bound")


    case CommandFailed(_: Bind) => context stop self


    case c @ Connected(remote, local) =>
      println("Distant client connected : "+remote.toString)
      val connection = sender()
      clients = connection :: clients
      val handler = context.actorOf(Props[SimplisticHandler])
      connection ! Register(handler)

    case s: String =>
     // if(s equals "clients")

    case Stop =>
      context stop self

  }

}
