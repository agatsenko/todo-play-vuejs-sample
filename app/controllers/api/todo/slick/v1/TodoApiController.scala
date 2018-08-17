/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-12
  */
package controllers.api.todo.slick.v1

import scala.concurrent.{ExecutionContext, Future}

import java.util.UUID
import javax.inject.{Inject, Singleton}

import model.v1.{TodoList, TodoTask}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import slick.jdbc.JdbcProfile

@Singleton
class TodoApiController @Inject()(
    cc: ControllerComponents,
    override protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {
  private implicit val listWrites: Writes[TodoList] = (list: TodoList) => Json.obj(
    "id" -> list.id,
    "name" -> list.name
  )

  private implicit val listReads: Reads[TodoList] = (
      (JsPath \ "id").read[UUID] and
      (JsPath \ "name").read[String]
      ) (TodoList.apply _)

  private implicit val taskWrites: Writes[TodoTask] = (task: TodoTask) => Json.obj(
    "id" -> task.id,
    "description" -> task.description,
    "completed" -> task.completed
  )

  def getLists: Action[AnyContent] = Action.async { implicit req =>
    db.run(TodoList.findAll).map(lists => Ok(Json.toJson(lists)))
  }

  def getList(listId: String): Action[AnyContent] = Action.async { implicit req =>
    db.run(TodoList.findById(UUID.fromString(listId))).map {
      case None => notFound(s"'$listId' list not found")
      case Some(list) => Ok(Json.toJson(list))
    }
  }

  def addList(): Action[JsValue] = Action(parse.json).async { implicit req =>
    db.run(TodoList.save(TodoList(UUID.randomUUID(), (req.body \ "name").as[String])).map(l => Ok(Json.toJson(l))))
  }

  def updateList(): Action[JsValue] = Action(parse.json).async { implicit req =>
    val list = req.body.validate[TodoList].get
    db.run(TodoList.findById(list.id)).flatMap {
      case None => Future(badRequest(s"'${list.id}' list not found"))
      case Some(foundList) => db.run(TodoList.save(foundList.copy(name = list.name))).map(l => Ok(Json.toJson(l)))
    }
  }

  def removeList(listId: String): Action[AnyContent] = Action.async { implicit req =>
    db.run(TodoList.remove(UUID.fromString(listId))).map {
      case 0 => badRequest(s"'$listId' list not found")
      case _ => Ok
    }
  }

  def getTasks(listId: String): Action[AnyContent] = Action.async { implicit req =>
    db.run(TodoTask.findByListId(UUID.fromString(listId))).map(t => Ok(Json.toJson(t)))
  }

  def getTask(listId: String, taskId: String): Action[AnyContent] = Action.async { implicit req =>
    db
        .run(TodoTask.findByListIdAndTaskId(UUID.fromString(listId), UUID.fromString(taskId)))
        .map {
          case None => notFound(s"task not found with specified listId = '$listId', taskId = '$taskId'")
          case Some(task) => Ok(Json.toJson(task))
        }
  }

  def addTask(listIdStr: String): Action[JsValue] = Action(parse.json).async { implicit req =>
    val listId = UUID.fromString(listIdStr)
    db.run(TodoList.findById(listId)).flatMap {
      case None => Future(notFound(s"'$listId' list not found"))
      case Some(list) => db
          .run(
            TodoTask.save(
              TodoTask(
                UUID.randomUUID(),
                list.id,
                (req.body \ "description").as[String],
                (req.body \ "completed").as[Boolean]
              )
            )
          ).map(t => Ok(Json.toJson(t)))
    }
  }

  def updateTask(listId: String): Action[JsValue] = Action(parse.json).async { implicit req =>
    val task = TodoTask(
      (req.body \ "id").as[UUID],
      UUID.fromString(listId),
      (req.body \ "description").as[String],
      (req.body \ "completed").as[Boolean]
    )
    db.run(TodoTask.findByListIdAndTaskId(UUID.fromString(listId), task.id)).flatMap {
      case None => Future(badRequest(s"task not found with specified listId = '$listId', taskId = '${task.id}'"))
      case Some(foundTask) => db.run(
        TodoTask
            .save(foundTask.copy(description = task.description, completed = task.completed))
            .map(t => Ok(Json.toJson(t)))
      )
    }
  }

  def removeTask(listId: String, taskId: String): Action[AnyContent] = Action.async { implicit req =>
    db.run(TodoTask.remove(UUID.fromString(listId), UUID.fromString(taskId))).map {
      case 0 => badRequest(s"task not found with specified listId = '$listId', taskId = '$taskId'")
      case _ => Ok
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
