/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-12
  */
package controllers.api.todo.slick.v1

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.{Inject, Singleton}

import model.{TodoList, TodoListRepo, TodoTask}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class TodoApiController @Inject()(
    cc: ControllerComponents,
    private val _listRepo: TodoListRepo)(
    implicit ec: ExecutionContext) extends AbstractController(cc) {
  private implicit val _listWrites: Writes[TodoList] = (list: TodoList) => Json.obj(
    "id" -> list.id,
    "name" -> list.name
  )

  private implicit val _listReads: Reads[TodoList] = (
      (JsPath \ "id").read[UUID] and
      (JsPath \ "name").read[String]
  )(TodoList.apply _)

  private implicit val _taskWrites: Writes[TodoTask] = (task: TodoTask) => Json.obj(
    "id" -> task.id,
    "description" -> task.description,
    "completed" -> task.completed
  )

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // FIXME: need to remove after db access will be implemented

  private def buildLists(): mutable.Map[UUID, TodoList] = {
    import scala.collection.JavaConverters._

    var listSeq = 0

    def createListRecord(): (UUID, TodoList) = {
      val id = UUID.randomUUID()
      listSeq += 1
      id -> TodoList(id, s"list $listSeq")
    }

    val listMap = new ConcurrentHashMap[UUID, TodoList]().asScala
    listMap += createListRecord()
    listMap += createListRecord()
    listMap += createListRecord()
    listMap += createListRecord()
    listMap += createListRecord()
    listMap += createListRecord()
    listMap
  }

  private def buildMockTasks(lists: Iterable[TodoList]): mutable.Map[UUID, TodoTask] = {
    import scala.collection.JavaConverters._

    var taskSeq = 0

    def createTaskRecord(list: TodoList) = {
      val id = UUID.randomUUID()
      taskSeq += 1
      id -> TodoTask(id, list.id, s"description of task $taskSeq (listId: ${list.id})", (taskSeq & 1) == 0)
    }

    val taskMap = new ConcurrentHashMap[UUID, TodoTask]().asScala
    lists.foreach(l => {
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
      taskMap += createTaskRecord(l)
    })
    taskMap
  }

  private val _mockLists = buildLists()
  private val _mockTasks = buildMockTasks(_mockLists.values)

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  def getLists: Action[AnyContent] = Action.async { implicit req =>
    _listRepo.getAll.map(lists => Ok(Json.toJson(lists)))
  }

  def getList(listId: String): Action[AnyContent] = Action.async { implicit req =>
    _listRepo.getById(UUID.fromString(listId)).map {
      case None => notFound(s"'$listId' list not found")
      case Some(foundList) => Ok(Json.toJson(foundList))
    }
  }

  def addList(): Action[JsValue] = Action(parse.json).async { implicit req =>
    _listRepo.save(TodoList(UUID.randomUUID(), (req.body \ "name").as[String])).map(l => Ok(Json.toJson(l)))
  }

  def updateList(): Action[JsValue] = Action(parse.json).async { implicit req =>
    val list = req.body.validate[TodoList].get
    _listRepo.exists(list.id).flatMap {
      case true => _listRepo.save(list).map(l => Ok(Json.toJson(l)))
      case false => Future(badRequest(s"'${list.id}' list not found"))
    }
  }

  def removeList(listId: String): Action[AnyContent] = Action.async { implicit req =>
    val uuidListId = UUID.fromString(listId)
    _listRepo.remove(uuidListId).map {
      case true => Ok
      case false => notFound(s"'$listId' list not found")
    }
  }

  def getTasks(listId: String) = Action { implicit req =>
    val uuidListId = UUID.fromString(listId)
    if (_mockLists.contains(uuidListId)) {
      Ok(Json.toJson(_mockTasks.values.filter(uuidListId == _.listId)))
    }
    else {
      notFound(s"'$listId' list not found")
    }
  }

  def getTask(listId: String, taskId: String) = Action { implicit req =>
    val uuidListId = UUID.fromString(listId)
    val uuidTaskId = UUID.fromString(taskId)
    if (_mockLists.contains(uuidListId)) {
      _mockTasks.values.filter(uuidListId == _.listId).find(uuidTaskId == _.id) match {
        case None => notFound(s"'$taskId' task not found")
        case Some(task) => Ok(Json.toJson(task))
      }
    }
    else {
      notFound(s"'$listId' list not found")
    }
  }

  def addTask(listId: String) = Action(parse.json) { implicit req =>
    // FIXME: not yet implemented
    ???
  }

  def updateTask(listId: String) = Action(parse.json) { implicit req =>
    // FIXME: not yet implemented
    ???
  }

  def removeTask(listId: String) = Action(parse.json) { implicit req =>
    // FIXME: not yet implemented
    ???
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
