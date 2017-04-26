package model

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import play.api.libs.json.Json

/**
  * Created by faiaz on 25.04.17.
  */
case class User(uuid: UUID = UUID.randomUUID(),
                loginInfo: LoginInfo,
                firstName: Option[String],
                lastName: Option[String],
                fullName: Option[String],
                email: Option[String],
                avatarURL: Option[String],
                activated: Boolean) extends Identity

object User {
  val COLLECTION_NAME = "users"
  implicit val lgFmt = Json.format[LoginInfo]
  implicit val fmt = Json.format[User]
}
