package Client

import akka.actor.ActorRef

/**
  * Created by remi on 23/11/16.
  */
class ClientInputHandler(client: ActorRef) {

  def handle(input: String): Boolean = input match {
    case s if s.startsWith("/") =>
      return options(input)
    case _ =>
      client ! input
      return false
  }

  def options(input: String): Boolean = input match {
    case "/help" =>
      printHelp()
      return false
    case "/dc" =>
      println("Disconnecting...")
      return true

    case _ =>
      println("Bad command")
      printHelp()
      return false
  }

  def printHelp() = println("\n##########################\n" +
    "Commands help : \n" +
    "/help for this help.\n" +
    "/dc to disconnect\n" +
    "#########################\n")

}
