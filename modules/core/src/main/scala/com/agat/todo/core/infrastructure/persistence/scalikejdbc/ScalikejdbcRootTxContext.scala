/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scala.util.Try
import scala.util.control.NonFatal

import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc.{DBConnection, DBSession}

import com.agat.todo.core.infrastructure.persistence.PersistService
import com.agat.todo.core.infrastructure.util.TryOps

private[scalikejdbc] class ScalikejdbcRootTxContext(
    override val persistService: PersistService,
    private val conn: DBConnection) extends ScalikejdbcTxContext {
  import ScalikejdbcRootTxContext._

  private val tx = {
    val tx = conn.newTx
    tx.begin()
    tx
  }
  private var rollbackOnly: Boolean = false

  override val parent: Option[ScalikejdbcTxContext] = None

  override val session: DBSession = conn.withinTxSession(tx)

  override def isRollbackOnly: Boolean = rollbackOnly

  override def setRollbackOnly(): Unit = rollbackOnly = true

  override def close(): Unit = {
    Try {
      if (rollbackOnly) {
        tx.rollback()
      }
      else {
        commit()
      }
    }.eventually(_ => conn.close())
  }

  private def silentRollback(): Unit = {
    try {
      tx.rollbackIfActive()
    }
    catch {
      case NonFatal(rollbackEx) => logger.warn("unable to rollback the current transaction", rollbackEx)
    }
  }

  private def commit(): Unit = {
    try {
      tx.commit()
    }
    catch {
      case NonFatal(ex) =>
        silentRollback()
        throw ex
    }
  }
}

private object ScalikejdbcRootTxContext {
  val logger: Logger = LoggerFactory.getLogger(classOf[ScalikejdbcRootTxContext])
}
