/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence

trait TxContext extends PersistContext {
  def isRollbackOnly: Boolean

  def setRollbackOnly(): Unit
}
