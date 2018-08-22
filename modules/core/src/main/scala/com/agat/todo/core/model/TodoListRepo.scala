/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-15
  */
package com.agat.todo.core.model

import java.util.UUID

import com.agat.todo.core.infrastructure.persistence.PersistContext

trait TodoListRepo {
  def getAll(implicit context: PersistContext): Seq[TodoList]

  def getById(id: UUID)(implicit context: PersistContext): Option[TodoList]

  def exists(id: UUID)(implicit context: PersistContext): Boolean

  def save(list: TodoList)(implicit context: PersistContext): TodoList

  def remove(id: UUID)(implicit context: PersistContext): Unit
}
