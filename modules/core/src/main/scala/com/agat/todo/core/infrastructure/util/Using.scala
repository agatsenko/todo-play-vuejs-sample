/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-17
  */
package com.agat.todo.core.infrastructure.util

import scala.util.Try

object Using {
  def using[TResource <: AutoCloseable, TResult](resource: TResource)(action: TResource => TResult): TResult = {
    Try(action(resource)).eventually { _ =>
      if (resource != null) {
        resource.close()
      }
    }.get
  }
}
