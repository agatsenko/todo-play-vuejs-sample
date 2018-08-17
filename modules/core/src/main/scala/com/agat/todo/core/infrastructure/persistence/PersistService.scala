/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence

trait PersistService extends Unwrappable {
  def exec[R](action: PersistContext => R): R

  def execTx[R](action: TxContext => R): R
}
