package org.lolhens.commons.enrich

/**
  * Created by Pierre on 02.11.2015.
  */
class RichAny[A](val self: A) extends AnyVal {
  def as[T <: A]: Option[T] = self match {
    case e: T => Some(e)
    case _ => None
  }

  //def toFuture: Future[A] = Future.successful(self)

  def |>(f: (A) => Any): A = {
    f(self)
    self
  }
}

object RichAny {
  implicit def fromAny[A](value: A): RichAny[A] = new RichAny[A](value)
}