/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-12
  */
package controllers

import scala.collection.mutable

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.{Inject, Singleton}

import model.TodoList
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}

@Singleton
class TodoApiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  private implicit val _listWrites: Writes[TodoList] = (list: TodoList) => Json.obj(
    "id" -> list.id,
    "name" -> list.name
  )

  //  private implicit val _listReads: Reads[TodoList] = (json: JsValue) => {
  //    // FIXME: not yet implemented
  //    ???
  //  }

  private implicit val _listReads: Reads[TodoList] = (
      (JsPath \ "id").read[UUID] and
      (JsPath \ "name").read[String]
  )(TodoList.apply _)

  // FIXME: need to remove after db supported will be implemented
  private var _listNameSeq = new AtomicInteger
  private def makeListRecord(): (UUID, TodoList) = {
    val id = UUID.randomUUID()
    id -> TodoList(id, s"list ${_listNameSeq.incrementAndGet()}")
  }
  private val _mockLists = mutable.Map(
    makeListRecord(),
    makeListRecord(),
    makeListRecord(),
  )

  def getLists = Action { implicit req =>
    Ok(Json.toJson(_mockLists.values))
  }

  def getList(listId: String) = Action { implicit req =>
    _mockLists.get(UUID.fromString(listId)) match {
      case None => notFound(s"'$listId' list isn't found")
      case Some(foundList) => Ok(Json.toJson(foundList))
    }
  }

  def addList() = Action(parse.json) { implicit req =>
    val json = req.body
    val list = TodoList(UUID.randomUUID(), (json \ "name").as[String])
    _mockLists.put(list.id, list)
    Ok(Json.toJson(Json.toJson(list)))
  }

  def updateList() = Action(parse.json) { implicit req =>
    val list = req.body.validate[TodoList].get
    _mockLists.get(list.id) match {
      case None => badRequest(s"'${list.id}' list isn't found")
      case Some(_) =>
        _mockLists.put(list.id, list)
        Ok(Json.toJson(list))
    }
  }

  def removeList(listId: String) = Action { implicit req =>
    val uuidListId = UUID.fromString(listId)
    if (_mockLists.contains(uuidListId)) {
      _mockLists.remove(uuidListId)
      Ok
    }
    else {
      notFound(s"'$listId' list isn't found")
    }
  }

  private def jsonError(errorMsg: String, statusCode: Int) = Json.obj(
    "message" -> errorMsg,
    "statusCode" -> statusCode,
  )

  private def notFound(errorMsg: String) = NotFound(
    jsonError(errorMsg, 404)
  )

  private def badRequest(errorMsg: String) = BadRequest(
    jsonError(errorMsg, 400)
  )
}
