package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import play.api.i18n.MessagesApi
import play.api.mvc.{Controller, Result}
import utils.MyEnv

import scala.concurrent.Future

/**
  * Created by faiaz on 26.04.17.
  */
class ApplicationController @Inject()(
                                       val messagesApi: MessagesApi,
                                       silhouette: Silhouette[MyEnv]
                                     ) extends Controller {

  def index = silhouette.UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(_) => Future.successful(Ok(views.html.index("Hi auth")))
      case None => Future.successful(Ok(views.html.index("Hi not auth")))
    }
  }
}
