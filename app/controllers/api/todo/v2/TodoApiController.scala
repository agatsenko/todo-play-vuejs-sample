/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-18
  */
package controllers.api.todo.v2

import java.util.UUID
import javax.inject.{Inject, Singleton}

import io.circe.generic.auto._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc.{AbstractController, ControllerComponents}

import com.agat.todo.core.infrastructure.persistence.PersistService
import com.agat.todo.core.model.TodoListRepo

@Singleton
class TodoApiController @Inject()(
    cc: ControllerComponents,
    private val persistSvc: PersistService,
    private val listRepo: TodoListRepo) extends AbstractController(cc) with Circe {
  def getLists = Action { implicit req =>
    Ok(persistSvc.execTx(implicit context => listRepo.getAll).asJson)
  }

  def getList(listIdStr: String) = Action { implicit req =>
    val listId = UUID.fromString(listIdStr)
    persistSvc.execTx(implicit context => listRepo.getById(listId)) match {
      case None => NotFound(ErrorInfo(s"'$listId' not found").asJson)
      case Some(list) => Ok(list.asJson)
    }
  }

  def addList() = Action { implicit req =>
    // FIXME: not yet implemented
    ???
  }

  def updateList() = Action { implicit req =>
    // FIXME: not yet implemented
    ???
  }

  def removeList(listIdStr: String) = Action { implicit req =>
    // FIXME: not yet implemented
    ???
  }
}
