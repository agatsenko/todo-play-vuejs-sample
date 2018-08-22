/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-22
  */
package controllers.api.todo.v2

import java.util.UUID

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}

import com.agat.todo.core.model.TodoTask

class TodoTaskDecoder(val generateNewId: Boolean) extends Decoder[TodoTask] {
  override def apply(c: HCursor): Result[TodoTask] = {
    for {
      id <- id(c)
      description <- description(c)
      completed <- completed(c)
    } yield TodoTask(id, description, completed)
  }

  private def id(c: HCursor): Decoder.Result[UUID] = {
    if (generateNewId) {
      Right(UUID.randomUUID())
    }
    else {
      c.downField("id").as[String].map(UUID.fromString) match {
        case Left(_) => Right(UUID.randomUUID())
        case right => right
      }
    }
  }

  private def description(c: HCursor): Decoder.Result[String] = c.downField("description").as[String]

  private def completed(c: HCursor): Decoder.Result[Boolean] = c.downField("completed").as[Boolean]
}
