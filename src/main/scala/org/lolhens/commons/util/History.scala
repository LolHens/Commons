package org.lolhens.commons.util

/**
  * Created by Pierre on 02.03.2016.
  */


object History {
  def apply[A](history: (Timestamp, A)*): History[A] = history

  type History[+A] = Seq[(Timestamp, A)]

  def Empty[A]: History[A] = Nil
}
