/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package model.v1

import scala.concurrent.ExecutionContext

import java.util.UUID

case class TodoList(id: UUID, name: String)

object TodoList {
  val profile = slick.jdbc.H2Profile

  import profile.api._

  def findById(id: UUID): DBIO[Option[TodoList]] = Query.findById(id).result.headOption

  def findAll: DBIO[Seq[TodoList]] = Query.findAll.result

  def save(list: TodoList)(implicit ec: ExecutionContext): DBIO[TodoList] =
    (for (_ <- Query.lists.insertOrUpdate(list)) yield list).transactionally

  def remove(id: UUID)(implicit ec: ExecutionContext): DBIO[Int] = {
    (for {
      tasksCount <- TodoTask.Query.findByListId(id).delete
      listsCount <- Query.findById(id).delete
    } yield {
      tasksCount + listsCount
    }).transactionally
  }

  class TodoListTable(tag: Tag) extends Table[TodoList](tag, "TODO_LISTS") {
    def id: Rep[UUID] = column[UUID]("ID", O.PrimaryKey)
    def name: Rep[String] = column[String]("NAME", O.Length(50))

    def * = (id, name) <> ((TodoList.apply _).tupled, TodoList.unapply)
  }

  private[v1] object Query {
    val lists = TableQuery[TodoListTable]

    def findById = lists.findBy(_.id)
    def findAll = lists
  }
}
