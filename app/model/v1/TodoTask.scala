/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
package model.v1

import java.util.UUID

case class TodoTask(id: UUID, listId: UUID, description: String, completed: Boolean = false) {
  def modify(description: String = this.description, completed: Boolean = this.completed): TodoTask =
    if (this.description == description && this.completed == completed)
      this
    else
      TodoTask(id, listId, description, completed)

  override def equals(obj: Any): Boolean = {
    if (!canEqual(obj)) {
      return false
    }

    val item = obj.asInstanceOf[TodoTask]
    if (this eq item) true else id == item.id
  }

  override def hashCode: Int = id.hashCode

  // hide copy method
  private def copy(): Unit = throw new UnsupportedOperationException
}
