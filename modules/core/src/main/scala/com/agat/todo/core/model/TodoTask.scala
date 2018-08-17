/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-15
  */
package com.agat.todo.core.model

import java.util.UUID

case class TodoTask(id: UUID, description: String, completed: Boolean = false) {
  override def canEqual(obj: Any): Boolean = obj != null && getClass == obj.getClass

  override def equals(obj: Any): Boolean = {
    if (!canEqual(obj)) {
      return false
    }

    val task = obj.asInstanceOf[TodoTask]
    if (this eq task) true else id == task.id
  }

  override def hashCode: Int = id.hashCode
}
