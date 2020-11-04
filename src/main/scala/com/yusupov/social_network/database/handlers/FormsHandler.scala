package com.yusupov.social_network.database.handlers

import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database._
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class FormsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def getForm(userId: String) =
    sql"SELECT first_name, last_name, age, gender, interests, city FROM Users WHERE id='#$userId' LIMIT 1"
      .as[(Option[String], Option[String], Option[Int], Option[String], Option[String], Option[String])]

  def updateForm(userId: String, form: UserForm) =
    if (form.firstName.isEmpty && form.lastName.isEmpty && form.age.isEmpty && form.gender.isEmpty && form.interests.isEmpty && form.city.isEmpty) {
      sqlu"SELECT 1"
    } else {
      val firstName = form.firstName.map(fn => s"first_name='$fn'").getOrElse("")
      val lastName = form.lastName.map(ln => s"last_name='$ln'").getOrElse("")
      val age = form.age.map(a => s"age=$a").getOrElse("")
      val gender = form.gender.map(g => s"gender='$g'").getOrElse("")
      val interests = form.interests.map(i => s"interests='$i'").getOrElse("")
      val city = form.city.map(c => s"city='$c'").getOrElse("")
      sqlu"""UPDATE Users SET #$firstName, #$lastName, #$age, #$gender, #$interests, #$city WHERE id='#$userId'"""
    }

  override def handle(implicit ec: ExecutionContext) = {
    case GetForm(userId) =>
      logger.debug(s"Getting form for $userId")
      getForm(userId).map {
        case Vector((firstName, lastName, age, gender, interests, city), _*) => RequestedForm(UserForm(firstName, lastName, age, gender, interests, city))
        case _ => FormNotFound
      }

    case UpdateForm(userId, form) =>
      logger.debug(s"Updating form for $userId")
      updateForm(userId, form)
        .map(_ => FormUpdated)
  }
}
