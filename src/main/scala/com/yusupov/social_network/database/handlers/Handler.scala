package com.yusupov.social_network.database.handlers

import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

trait Handler {
  def handle(implicit ec: ExecutionContext): PartialFunction[Any, DBIO[_]]
}
