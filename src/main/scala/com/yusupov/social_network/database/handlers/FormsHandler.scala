package com.yusupov.social_network.database.handlers

import com.yusupov.social_network.data.UserForm
import com.yusupov.social_network.database.DatabaseProvider
import slick.jdbc.JdbcProfile

class FormsHandler[T <: JdbcProfile](databaseProvider: DatabaseProvider[T]) {
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

}
