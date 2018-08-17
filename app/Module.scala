/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
import javax.inject.{Inject, Provider}

import com.google.inject.{AbstractModule, Scopes}
import play.api.db.Database
import play.db.NamedDatabase
import scalikejdbc.DataSourceConnectionPool

import com.agat.todo.core.infrastructure.persistence.PersistService
import com.agat.todo.core.infrastructure.persistence.scalikejdbc.ScalikejdbcPersistService
import com.agat.todo.core.model.TodoListRepo
import com.agat.todo.core.persistence.scalikejdbc.h2.ScalikejdbcH2TodoListRepo

class Module extends AbstractModule {
  import Module._

  override def configure(): Unit = {
    bind(classOf[PersistService]).toProvider(classOf[PersistServiceProvider]).in(Scopes.SINGLETON)
    bind(classOf[TodoListRepo]).to(classOf[ScalikejdbcH2TodoListRepo]).in(Scopes.SINGLETON)
  }
}

object Module {
  class PersistServiceProvider @Inject()(
      @NamedDatabase("v2") private val db: Database) extends Provider[PersistService] {
    override def get(): PersistService = {
      new ScalikejdbcPersistService(
        new DataSourceConnectionPool(db.dataSource, closer = () => /* do nothing */ Unit)
      )
    }
  }
}
