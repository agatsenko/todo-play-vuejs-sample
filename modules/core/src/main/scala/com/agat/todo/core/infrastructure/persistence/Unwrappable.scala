/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-17
  */
package com.agat.todo.core.infrastructure.persistence

import scala.reflect.ClassTag

trait Unwrappable {
  def unwrap[T : ClassTag]: Option[T]
}
