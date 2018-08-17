/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.infrastructure.persistence

trait PersistContext extends Unwrappable {
  def persistService: PersistService
}
