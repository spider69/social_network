package com.yusupov.social_network.api

import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}

import scala.concurrent.Future

trait SessionChecker {
  def checkSession(action: String => Route): RequestContext => Future[RouteResult]
}
