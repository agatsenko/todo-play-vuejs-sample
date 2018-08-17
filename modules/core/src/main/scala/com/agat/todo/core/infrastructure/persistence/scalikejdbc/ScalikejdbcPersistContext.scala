/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-17
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scala.reflect.{ClassTag, classTag}

import scalikejdbc.{DBConnection, DBSession}

import com.agat.todo.core.infrastructure.persistence.{PersistContext, PersistService}

private[scalikejdbc] class ScalikejdbcPersistContext(
    private val _persistService: ScalikejdbcPersistService,
    private val conn: DBConnection) extends PersistContext with AutoCloseable {
  private val session: DBSession = conn.autoCommitSession()

  override def persistService: PersistService = _persistService

  override def unwrap[T: ClassTag]: Option[T] = {
    implicitly[ClassTag[T]] match {
      case ct if ct == classTag[DBSession] => Some(session.asInstanceOf[T])
      case ct if ct == classTag[PersistContext] || ct == classTag[ScalikejdbcPersistContext] =>
        Some(this.asInstanceOf[T])
      case _ => _persistService.unwrap[T]
    }
  }
  override def close(): Unit = {
    conn.close()
  }
}
