package com.yusupov.social_network.utils

object HexUtils {

  private val charValues = Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15)

  def hexToBytes(hex: String): Array[Byte] = {
    val result: Array[Byte] = new Array(hex.length/2)
    for (i <- 0 until hex.length by 2){
      val h = charValues(hex.charAt(i) - '0') << 4
      val l = charValues(hex.charAt(i + 1) - '0')
      result(i/2) = (h+l).toByte
    }
    result
  }

  def bytesToHex(bytes: Array[Byte]): String = {
    bytes.map("%02x".format(_)).mkString
  }

}
