package com.yusupov.social_network.data

case class UserForm(
  firstName: String = "",
  lastName: String = "",
  age: Int = 0,
  gender: String = "",
  interests: String = "",
  city: String = ""
)