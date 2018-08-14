/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package persistence.slick.h2

import scala.concurrent.{ExecutionContext, Future}

import java.util.UUID
import javax.inject.Inject

import model.{TodoList, TodoListRepo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

class SlickH2TodoListRepo @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with TodoListRepo with Tables {
  import profile.api._

  private val _getByIdQry = Compiled((id: Rep[UUID]) => todoLists.filter(_.id === id))

  override def getById(id: UUID): Future[Option[TodoList]] = db.run(_getByIdQry(id).result.headOption)

  override def getAll: Future[Iterable[TodoList]] = db.run(todoLists.sortBy(_.name).result)

  override def exists(id: UUID): Future[Boolean] = db.run(todoLists.filter(_.id === id).exists.result)

  override def save(list: TodoList): Future[TodoList] = db.run(todoLists.insertOrUpdate(list)).map(_ => list)

  override def remove(id: UUID): Future[Boolean] = db.run(_getByIdQry(id).delete).map(count => count > 0)
}
