/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-14
  */
import com.google.inject.{AbstractModule, Scopes}
import model.TodoListRepo
import persistence.slick.h2.SlickH2TodoListRepo

class Module extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[TodoListRepo]).to(classOf[SlickH2TodoListRepo]).in(Scopes.SINGLETON)
  }
}
