/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-11
  */
import scala.util.{Failure, Success, Try}

import akka.japi.Option.Some

object Loan {
  def using[TResource <: AutoCloseable, TResult](resource: TResource)(block: TResource => TResult): TResult = {
    Try(block(resource)) match {
      case Success(result) =>
        closeResource(resource)
        result
      case Failure(blockEx) =>
        throw closeResource(resource, Some(blockEx)).get
    }
  }

  private def closeResource[R <: AutoCloseable](resource: R, srcEx: Option[Throwable] = None): Option[Throwable] = {
    resource match {
      case null => srcEx
      case _ =>
        Try(resource.close()) match {
          case Success(_) => srcEx
          case Failure(closeEx) => srcEx.
              map { ex =>
                closeEx.addSuppressed(ex)
                closeEx
              }.
              orElse(Some(closeEx))
        }
    }
  }
}
