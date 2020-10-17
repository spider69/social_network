package com.yusupov.social_network.utils

import java.security.SecureRandom

import org.apache.commons.codec.digest.DigestUtils

object SecurityUtils {
  def passwordHash(password: String, salt: Array[Byte]): String =
    DigestUtils.sha512Hex(password.getBytes ++ salt)

  def checkPassword(password: String, salt: Array[Byte], passwordHash: String): Boolean =
    DigestUtils.sha512Hex(password.getBytes ++ salt) == passwordHash

  def generateSalt(length: Int = 64) = {
    val salt = Array.fill(length)(0.byteValue())
    new SecureRandom().nextBytes(salt)
    salt
  }
}
