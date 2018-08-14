/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package model.v1

import scala.concurrent.Future

import java.util.UUID

trait TodoListRepo {
  def getById(id: UUID): Future[Option[TodoList]]

  def getAll: Future[Iterable[TodoList]]

  def exists(id: UUID): Future[Boolean]

  def save(list: TodoList): Future[TodoList]

  def remove(id: UUID): Future[Boolean]
}
