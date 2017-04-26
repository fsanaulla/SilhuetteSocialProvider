package services

import java.util.UUID

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import model.User
import model.User._
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by faiaz on 25.04.17.
  */
class UserService @Inject()(reactiveMongoApi: ReactiveMongoApi) extends IdentityService[User]{

  private def collection = reactiveMongoApi.database.map(_.collection[JSONCollection](COLLECTION_NAME))

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    val criteria = Json.obj("loginInfo" -> loginInfo)

    collection.flatMap(_.find(criteria).one[User])
  }

  def retrieve(uuid: UUID): Future[Option[User]] = {
    val criteria = Json.obj("uuid" -> uuid)

    collection.flatMap(_.find(criteria).one[User])
  }

  def save(user: User): Future[User] = {
    collection.flatMap(_.insert(user)).map(_ => user)
  }

  def save(profile: CommonSocialProfile): Future[User] = {
    retrieve(profile.loginInfo).flatMap {

      case Some(user) =>
        val updateUSer = user.copy(
          firstName = profile.firstName,
          lastName = profile.lastName,
          fullName = profile.fullName,
          email = profile.email,
          avatarURL = profile.avatarURL)

        update(profile.loginInfo, updateUSer)

      case None =>
        val newUser = User(
          uuid = UUID.randomUUID(),
          loginInfo = profile.loginInfo,
          firstName = profile.firstName,
          lastName = profile.lastName,
          fullName = profile.fullName,
          email = profile.email,
          avatarURL = profile.avatarURL,
          activated = true
        )

        save(newUser)
    }
  }

  def update(uuid: UUID, userUpdate: User): Future[User] = {
    val criteria = Json.obj("uuid" -> uuid)

    collection.flatMap(_.update(criteria, userUpdate)).map(_ => userUpdate)
  }

  def update(loginInfo: LoginInfo, userUpdate: User): Future[User] = {
    val criteria = Json.obj("loginInfo" -> loginInfo)

    collection.flatMap(_.update(criteria, userUpdate)).map(_ => userUpdate)
  }

  def remove(uuid: UUID): Future[Unit] = {
    val criteria = Json.obj("uuid" -> uuid)

    collection.flatMap(_.remove(criteria)).map(_ => {})
  }

  def remove(loginInfo: LoginInfo): Future[Unit] = {
    val criteria = Json.obj("loginInfo" -> loginInfo)

    collection.flatMap(_.remove(criteria)).map(_ => {})
  }
}
