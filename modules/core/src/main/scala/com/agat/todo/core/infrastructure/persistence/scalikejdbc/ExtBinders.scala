/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-17
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import java.util.UUID

import scalikejdbc.Binders

object ExtBinders {
  implicit val uuid: Binders[UUID] =
      Binders(_.getObject(_, classOf[UUID]))(_.getObject(_, classOf[UUID]))(v => (ps, idx) => ps.setObject(idx, v))
}
