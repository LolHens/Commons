package org.lolhens.commons.util

/**
  * Created by Pierre on 14.12.2015.
  */
case class Log(val lines: List[String]) {
  def mkString = lines.mkString

  def mkString(sep: String) = lines.mkString(sep)

  def mkString(start: String, sep: String, end: String) = lines.mkString(start, sep, end)

  override def toString: String = mkString("\n")

  def map(f: List[String] => List[String]) = copy(lines = f(lines))
}

object Log {
  val Empty = new Log(List(""))
}