/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scala.reflect.{ClassTag, classTag}

import scalikejdbc.DBSession

import com.agat.todo.core.infrastructure.persistence.TxContext

private[scalikejdbc] trait ScalikejdbcTxContext extends TxContext with AutoCloseable {
  def session: DBSession

  def isRoot: Boolean = parent.isEmpty

  def parent: Option[ScalikejdbcTxContext]

  override def unwrap[T: ClassTag]: Option[T] = implicitly[ClassTag[T]] match {
    case ct if classOf[DBSession].isAssignableFrom(ct.runtimeClass) => Some(session.asInstanceOf[T])
    case ct if ct == classTag[TxContext] || ct == classTag[ScalikejdbcPersistContext] => Some(this.asInstanceOf[T])
    case _ => persistService.unwrap[T]
  }
}
