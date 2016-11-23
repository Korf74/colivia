package Messages

import akka.util.ByteString

/**
  * Created by remi on 22/11/16.
  */
trait ServerHandlerMessage

case class SpreadData(data: ByteString) extends ServerHandlerMessage
case class Send(data: ByteString) extends ServerHandlerMessage
object HandlerStop extends ServerHandlerMessage
