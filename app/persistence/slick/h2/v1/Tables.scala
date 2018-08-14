/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package persistence.slick.h2.v1

import java.util.UUID

import model.v1.TodoList
import slick.jdbc.JdbcProfile

trait Tables {
  protected val profile: JdbcProfile

  import profile.api._

  protected val todoLists = new TableQuery(tag => new TodoListTable(tag))

  protected class TodoListTable(tag: Tag) extends Table[TodoList](tag, "TODO_LISTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME", O.Length(50))

    def * = (id, name) <> ((TodoList.apply _).tupled, TodoList.unapply)
  }
}
