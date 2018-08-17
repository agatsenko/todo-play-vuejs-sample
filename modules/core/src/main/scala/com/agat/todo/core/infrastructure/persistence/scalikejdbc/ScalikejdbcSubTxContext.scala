/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scalikejdbc.DBSession

import com.agat.todo.core.infrastructure.persistence.PersistService

private[scalikejdbc] class ScalikejdbcSubTxContext(
    private val _parent: ScalikejdbcTxContext) extends ScalikejdbcTxContext {
  override def persistService: PersistService = _parent.persistService

  override def session: DBSession = _parent.session

  override val parent: Option[ScalikejdbcTxContext] = Some(_parent)

  override def isRollbackOnly: Boolean = _parent.isRollbackOnly

  override def setRollbackOnly(): Unit = _parent.setRollbackOnly()

  override def close(): Unit = {
    // do nothing
  }
}
