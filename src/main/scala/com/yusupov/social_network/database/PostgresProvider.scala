package com.yusupov.social_network.database

import slick.jdbc.PostgresProfile

object PostgresProvider extends PostgresProfile

class PostgresProvider extends DatabaseProvider[PostgresProfile] {
  override val profile = PostgresProvider

  override val database = profile.api.Database.forConfig("database.postgres")

  override def hex(value: String) = s"encode($value, 'hex')"

  override def unhex(value: String) = s"decode('$value', 'hex')"
}
