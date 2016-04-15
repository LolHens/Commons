package org.lolhens.commons.enrich

/**
  * Created by Pierre on 04.03.2016.
  */
class RichOption[A](val self: Option[A]) extends AnyVal {
  def Or[B >: A](option: => Option[B]): Option[B] = self.orElse(option)

  def And[B](option: => Option[B]): Option[(A, B)] = self.flatMap(e => option.map(e2 => (e, e2)))

  def XOr[B](option: => Option[B]): Option[Either[A, B]] =
    self.map(e => option.map(e => None).getOrElse(Some(Left(e))))
      .getOrElse(option.map(e => Right(e)))

  def Then[B](f: A => B): Option[B] = self.map(f)

  def Maybe[B](f: A => Option[B]): Option[B] = self.flatMap(f)

  def Else[B >: A](value: => B): B = self.getOrElse(value)

  def As[T <: A]: Option[T] = self.flatMap {
    case e: T => Some(e)
    case _ => None
  }
}

object RichOption {
  implicit def fromOption[A](option: Option[A]): RichOption[A] = new RichOption(option)
}