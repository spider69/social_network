package com.yusupov.social_network.database.handlers

import akka.actor.ActorRef
import com.typesafe.scalalogging.LazyLogging
import com.yusupov.social_network.actors.Database.{CreateForm, FormCreated, FormNotFound, FormUpdated, GetForm, RequestedForm, UpdateForm}
import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class FormsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) extends Handler with LazyLogging {
  import databaseProvider.profile.api._

  def createForm(userId: String, form: UserForm) =
    sqlu"""INSERT INTO UserForms(id, first_name, last_name, age, gender, interests, city)
          VALUES('#$userId','#${form.firstName}','#${form.lastName}','#${form.age}','#${form.gender}','#${form.interests}','#${form.city}')"""

  def getForm(userId: String) =
    sql"SELECT first_name, last_name, age, gender, interests, city FROM UserForms WHERE id='#$userId' LIMIT 1"
      .as[(String, String, Int, String, String, String)]

  def updateForm(userId: String, form: UserForm) =
    sqlu"""UPDATE UserForms SET first_name='#${form.firstName}', last_name='#${form.lastName}', age='#${form.age}',
          gender='#${form.gender}', interests='#${form.interests}', city='#${form.city}' WHERE id='#$userId'"""

  override def handle(sender: ActorRef) = {
    case CreateForm(userId, form) =>
      logger.debug(s"Creating form for $userId")
      val query = createForm(userId, form)
      databaseProvider.exec(query)
      sender ! FormCreated

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
