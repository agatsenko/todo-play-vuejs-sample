/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package persistence.slick.h2

import scala.concurrent.ExecutionContext

import java.util.UUID
import javax.inject.Inject

import model.TodoList
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

class TodoLists @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class TodoListTable(tag: Tag) extends Table[TodoList](tag, "todo_lists") {
    def id = column[UUID]("id", O.PrimaryKey)
    def name = column[String]("name", O.Length(50))

    //def * = (id, name) <> (mp => TodoList.apply(mp._1, mp._2), list => TodoList.unapply(list))
    def * = (id, name) <> ((TodoList.apply _).tupled, TodoList.unapply)
  }
}
