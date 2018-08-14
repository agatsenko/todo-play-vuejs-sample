/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-13
  */
package model

import java.util.UUID

case class TodoList(id: UUID, name: String) {
  def rename(newName: String): TodoList = if (name == newName) this else TodoList(id, newName)

  override def equals(obj: Any): Boolean = {
    if (!canEqual(obj)) {
      return false
    }

    val list = obj.asInstanceOf[TodoList]
    if (this.eq(list)) true else id == list.id
  }

  override def hashCode(): Int = id.hashCode

  // hide copy method
  private def copy(id: UUID, name: String): TodoList = throw new UnsupportedOperationException
}

