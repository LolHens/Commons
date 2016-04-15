package processmonitor.util.enrich

import processmonitor.util._

class RichMap[A, B](val self: Map[A, B]) extends AnyVal {
  def getAs[T <: B](key: A): Option[T] = self.get(key) Maybe (_.as[T])

  def as[T <: B](key: A): T = getAs[T](key).get
}

object RichMap {
  implicit def fromMap[A, B](value: Map[A, B]): RichMap[A, B] =
    new RichMap[A, B](value)
}