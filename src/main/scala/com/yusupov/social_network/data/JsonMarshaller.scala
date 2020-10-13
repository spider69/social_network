package com.yusupov.social_network.data

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonMarshaller extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val formMarshaller = jsonFormat6(UserForm)
  implicit val userMarshaller = jsonFormat3(User)
}
