/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-15
  */
package com.agat.todo.core.model

import java.util.UUID

case class TodoList(id: UUID, name: String, tasks: Set[TodoTask] = Set.empty) {
  override def canEqual(obj: Any): Boolean = obj != null && getClass == obj.getClass

  override def equals(obj: Any): Boolean = {
    if (!canEqual(obj)) {
      return false
    }

    val list = obj.asInstanceOf[TodoList]
    if (this eq list) true else id == list.id
  }

  override def hashCode: Int = id.hashCode
}
