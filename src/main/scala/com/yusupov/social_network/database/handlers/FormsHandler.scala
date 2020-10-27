package com.yusupov.social_network.database.handlers

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{FormNotFound, FormUpdated, GetForm, RequestedForm, UpdateForm}
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

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

  override def handle(sender: ActorRef) = {
    case GetForm(userId) =>
      logger.debug(s"Getting form for $userId")
      val query = getForm(userId)
      val result = databaseProvider.exec(query)
      result match {
        case Vector((firstName, lastName, age, gender, interests, city), _*) => sender ! RequestedForm(UserForm(firstName, lastName, age, gender, interests, city))
        case _ => sender ! FormNotFound
      }

    case UpdateForm(userId, form) =>
      logger.debug(s"Updating form for $userId")
      val query = updateForm(userId, form)
      databaseProvider.exec(query)
      sender ! FormUpdated
  }
}
