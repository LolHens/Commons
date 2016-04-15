package org.lolhens.commons.util

import java.time.LocalDateTime

/**
  * Created by Pierre on 13.10.2015.
  */
case class Timestamp(dateTime: LocalDateTime) {
  val date = s"${padLeft(dateTime.getDayOfMonth)}.${padLeft(dateTime.getMonthValue)}.${dateTime.getYear}"

  val time = s"${padLeft(dateTime.getHour)}:${padLeft(dateTime.getMinute)}:${padLeft(dateTime.getSecond)}"

  private def padLeft(int: Int, len: Int = 2, elem: String = "0") = int.toString.reverse.padTo(len, elem).reverse.mkString

  override def toString: String = s"$date $time"
}

object Timestamp {
  def now = Timestamp(LocalDateTime.now())

  def parse(date: String) = {
    val dateTime = date.split(" ")
    val ddmmyyyy = dateTime(0).split("\\.").map(_.toInt)
    val hhmmss = dateTime(1).split(":").map(_.toInt)
    val localDateTime = LocalDateTime.of(
      ddmmyyyy(2), ddmmyyyy(1), ddmmyyyy(0),
      hhmmss(0), hhmmss(1), hhmmss(2))
    new Timestamp(localDateTime)
  }
}