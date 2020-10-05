package com.yusupov.social_network.database

import slick.jdbc.MySQLProfile

object MySqlProvider extends MySQLProfile

class MySqlProvider extends DatabaseProvider[MySQLProfile] {
  override val profile = MySqlProvider
}

