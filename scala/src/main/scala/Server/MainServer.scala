package Server

import Messages.Stop
import akka.actor.{PoisonPill, ActorSystem, Props}

import scala.io.StdIn

/**
  * Created by remi on 09/11/16.
  */

object MainServer {

  def main(args: Array[String]): Unit = {

    val adr = args(0)

    val system = ActorSystem("Colivia_Server")

    val server = system.actorOf(Props(new Server(adr)), "Server")

    while(!StdIn.readLine().equals("stop"))

    server ! Stop

    system terminate

  }

}
