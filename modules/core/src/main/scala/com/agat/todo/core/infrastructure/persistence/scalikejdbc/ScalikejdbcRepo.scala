/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence.scalikejdbc

import scalikejdbc.DBSession

import com.agat.todo.core.infrastructure.persistence.PersistContext

trait ScalikejdbcRepo {
  def withinSession[R](action: DBSession => R)(implicit context: PersistContext): R = action(context.unwrap[DBSession].get)
}
