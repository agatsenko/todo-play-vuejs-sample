/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-18
  */
package controllers.api.todo.v2

import java.util.UUID
import javax.inject.{Inject, Singleton}

import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.Logger
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import com.agat.todo.core.infrastructure.persistence.PersistService
import com.agat.todo.core.model.{TodoList, TodoListRepo}

@Singleton
class TodoApiController @Inject()(
    cc: ControllerComponents,
    private val persistSvc: PersistService,
    private val listRepo: TodoListRepo) extends AbstractController(cc) with Circe {
  def getLists = Action { implicit req =>
    Ok(persistSvc.execTx(implicit context => listRepo.getAll).asJson)
  }

  def getList(listId: UUID) = Action { implicit req =>
    persistSvc.execTx(implicit context => listRepo.getById(listId)) match {
      case None => NotFound(ErrorInfo(s"'$listId' list not found").asJson)
      case Some(list) => Ok(list.asJson)
    }
  }

  def addList(): Action[Json] = Action(circe.json) { implicit req =>
    import TodoApiController.newTodoListDecoder

    req.body.as[TodoList].toOption match {
      case None => BadRequest(ErrorInfo("invalid json").asJson)
      case Some(list) => persistSvc.execTx { implicit ctx =>
        Logger("addList").warn("save")
        Ok(listRepo.save(list).asJson)
      }
    }
  }

  def updateList(): Action[Json] = Action(circe.json) { implicit req =>
    import TodoApiController.existTodoListDecoder

    req.body.as[TodoList].toOption match {
      case None => BadRequest(ErrorInfo("invalid json").asJson)
      case Some(list) => persistSvc.execTx { implicit ctx =>
        if (listRepo.exists(list.id)) {
          Ok(listRepo.save(list).asJson)
        }
        else {
          NotFound(ErrorInfo(s"'${list.id}' list not found").asJson)
        }
      }
    }
  }

  def removeList(listId: UUID) = Action { implicit req =>
    persistSvc.execTx { implicit ctx =>
      if (listRepo.exists(listId)) {
        listRepo.remove(listId)
        Ok
      }
      else {
        NotFound(ErrorInfo(s"'$listId' list not found").asJson)
      }
    }
  }
}

private object TodoApiController {
  implicit val newTodoListDecoder: TodoListDecoder = new TodoListDecoder(true)
  implicit val existTodoListDecoder: TodoListDecoder = new TodoListDecoder(false)
}
