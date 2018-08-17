/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scala.reflect.{ClassTag, classTag}
import scala.util.Try
import scala.util.control.NonFatal

import org.slf4j.{Logger, LoggerFactory}
import scalikejdbc.{ConnectionPool, DB}

import com.agat.todo.core.infrastructure.persistence.{PersistContext, PersistService, TxContext}
import com.agat.todo.core.infrastructure.util.{TryOps, using}

class ScalikejdbcPersistService(private val connPool: ConnectionPool) extends PersistService with AutoCloseable {
  import ScalikejdbcPersistService.logger

  private val currentPersistContextHolder: ThreadLocal[ScalikejdbcPersistContext] = new ThreadLocal
  private val currentTxContextHolder: ThreadLocal[ScalikejdbcTxContext] = new ThreadLocal

  override def exec[R](action: PersistContext => R): R = {
    if (isTxStarted) {
      throw new IllegalStateException(
        "unable to execute the action because there is an active transaction in the current thread"
      )
    }
    currentPersistContext match {
      case None =>
        try {
          using(new ScalikejdbcPersistContext(this, DB(connPool.borrow()))) { context =>
            currentPersistContext = Some(context)
            action(context)
          }
        }
        finally {
          currentPersistContext = None
        }
      case Some(context) => action(context)
    }
  }

  override def execTx[R](action: TxContext => R): R = {
    if (isNonTxExecStarted) {
      throw new IllegalStateException(
        "unable to execute transaction because there is an active non transactional action in the current thread"
      )
    }
    val txContext = beginTx()
    completeTx(
      Try(action(txContext)).eventually { result =>
        if (result.isFailure) {
          txContext.setRollbackOnly()
        }
      }
    ).get
  }

  override def unwrap[T: ClassTag]: Option[T] = {
    implicitly[ClassTag[T]] match {
      case ct if ct == classTag[PersistService] || ct == classTag[ScalikejdbcPersistService] =>
        Some(this.asInstanceOf[T])
      case _ => None
    }
  }

  override def close(): Unit = {
    connPool.close()
  }

  private def currentPersistContext: Option[ScalikejdbcPersistContext] = {
    currentPersistContextHolder.get match {
      case null =>
        currentPersistContextHolder.remove()
        None
      case context => Some(context)
    }
  }

  private def currentPersistContext_=(contextOpt: Option[ScalikejdbcPersistContext]): Unit = {
    contextOpt match {
      case None => currentPersistContextHolder.remove()
      case Some(context) => currentPersistContextHolder.set(context)
    }
  }

  private def isNonTxExecStarted: Boolean = currentPersistContext.isDefined

  private def currentTxContext: Option[ScalikejdbcTxContext] = {
    currentTxContextHolder.get() match {
      case null =>
        currentTxContextHolder.remove()
        None
      case context => Some(context)
    }
  }

  private def currentTxContext_=(txContextOpt: Option[ScalikejdbcTxContext]): Unit = {
    txContextOpt match {
      case Some(txContext) => currentTxContextHolder.set(txContext)
      case _ => currentTxContextHolder.remove()
    }
  }

  private def isTxStarted: Boolean = currentTxContext.isDefined

  private def beginRootTx(): ScalikejdbcTxContext = {
    val conn = DB(connPool.borrow())
    try {
      val rootTxContext = new ScalikejdbcRootTxContext(this, conn)
      currentTxContext = Some(rootTxContext)
      rootTxContext
    }
    catch {
      case NonFatal(ex) =>
        try {
          conn.close()
        }
        catch {
          case NonFatal(connCloseEx) =>
            logger.warn("unable to close the transaction", connCloseEx)
        }
        throw ex
    }
  }

  private def beginSubTx(parent: ScalikejdbcTxContext): ScalikejdbcTxContext = {
    val context = new ScalikejdbcSubTxContext(parent)
    currentTxContext = Some(context)
    context
  }

  private def beginTx(): ScalikejdbcTxContext = {
    currentTxContext match {
      case Some(parent) =>
        beginSubTx(parent)
      case _ =>
        beginRootTx()
    }
  }

  private def completeTx[R](result: Try[R]): Try[R] = {
    val txContext = currentTxContext.get
    result.eventually(_ => txContext.close()).eventually(_ => currentTxContext = txContext.parent)
  }
}

private object ScalikejdbcPersistService {
  val logger: Logger = LoggerFactory.getLogger(classOf[ScalikejdbcPersistService])
}