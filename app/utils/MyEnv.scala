package utils

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import model.User

/**
  * Created by faiaz on 26.04.17.
  */
trait MyEnv extends Env {
  type I = User
  type A = CookieAuthenticator
}
