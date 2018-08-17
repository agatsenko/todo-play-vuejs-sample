/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package model.v1

import scala.concurrent.ExecutionContext

import java.util.UUID

case class TodoTask(id: UUID, listId: UUID, description: String, completed: Boolean = false)

object TodoTask {
  val profile = slick.jdbc.H2Profile

  import profile.api._

  def findByListId(listId: UUID): DBIO[Seq[TodoTask]] = Query.findByListId(listId).result

  def findByListIdAndTaskId(listId: UUID, taskId: UUID): DBIO[Option[TodoTask]] =
    Query.findByListIdAndTaskId(listId, taskId).result.headOption

  def save(task: TodoTask)(implicit ec: ExecutionContext): DBIO[TodoTask] =
    (for(_ <- Query.tasks.insertOrUpdate(task)) yield task).transactionally

  def remove(listId: UUID, taskId: UUID): DBIO[Int] = Query.findByListIdAndTaskId(listId, taskId).delete.transactionally

  class TodoTaskTable(tag: Tag) extends Table[TodoTask](tag, "TODO_TASKS") {
    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)
    def listId: Rep[UUID] = column[UUID]("LIST_ID")
    def description: Rep[String] = column[String]("DESCRIPTION", O.Length(1000))
    def completed: Rep[Boolean] = column[Boolean]("COMPLETED")

    def * = (id, listId, description, completed) <> ((TodoTask.apply _).tupled, TodoTask.unapply)
  }

  private[v1] object Query {
    val tasks = TableQuery[TodoTaskTable]

    def findByListId = tasks.findBy(_.listId)
    def findByListIdAndTaskId(listId: UUID, taskId: UUID) = tasks.filter(t => t.listId === listId && t.id === taskId)
  }
}
