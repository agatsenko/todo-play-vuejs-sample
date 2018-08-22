/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-22
  */
package controllers.api.todo.v2

import java.util.UUID

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}

import com.agat.todo.core.model.{TodoList, TodoTask}

class TodoListDecoder(val generateNewId: Boolean) extends Decoder[TodoList] {
  private implicit val taskDecoder: TodoTaskDecoder = new TodoTaskDecoder(generateNewId)

  override def apply(c: HCursor): Result[TodoList] = {
    for {
      id <- id(c)
      name <- name(c)
      tasks <- tasks(c)
    } yield TodoList(id, name, tasks)
  }

  private def id(c: HCursor): Decoder.Result[UUID] = {
    if (generateNewId) {
      Right(UUID.randomUUID())
    }
    else {
      c.downField("id").as[String].map(UUID.fromString)
    }
  }

  private def name(c: HCursor): Decoder.Result[String] = c.downField("name").as[String]

  private def tasks(c: HCursor): Decoder.Result[Set[TodoTask]] = c.downField("tasks").as[Set[TodoTask]]
}
