/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-11
  */
package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class TodoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index(path: String) = Action { implicit req: Request[AnyContent] =>
    Ok(views.html.todo())
  }
}
