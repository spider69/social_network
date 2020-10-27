package com.yusupov.social_network.data

case class UserForm(
  firstName: Option[String],
  lastName: Option[String],
  age: Option[Int],
  gender: Option[String],
  interests: Option[String],
  city: Option[String]
)