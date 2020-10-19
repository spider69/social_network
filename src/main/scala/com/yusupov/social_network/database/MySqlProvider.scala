package com.yusupov.social_network.database

import slick.jdbc.MySQLProfile

object MySqlProvider extends MySQLProfile

class MySqlProvider extends DatabaseProvider[MySQLProfile] {
  override val profile = MySqlProvider

  override val database = profile.api.Database.forConfig("database.mysql")

  override def hex(value: String) = s"hex($value)"

  override def unhex(value: String) = s"unhex('$value')"
}

