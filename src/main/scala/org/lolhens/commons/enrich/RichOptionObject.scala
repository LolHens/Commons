package org.lolhens.commons.enrich

import scala.collection.generic.CanBuildFrom

/**
  * Created by Pierre on 03.11.2015.
  */
class RichOptionObject(val self: Option.type) extends AnyVal {
  def sequence[A, M[X] <: TraversableOnce[X]](in: M[Option[A]])
                                             (implicit bf1: CanBuildFrom[M[Option[A]], Option[A], List[Option[A]]],
                                              bf2: CanBuildFrom[List[A], A, M[A]]): Option[M[A]] = {
    val list = in.to[List]

    if (list.exists(_.isEmpty)) return None
    Some(list.map(_.get).to[M])
  }

  def batch[A, M[X] <: TraversableOnce[X]](in: M[Option[A]])
                                          (implicit bf1: CanBuildFrom[M[Option[A]], Option[A], List[Option[A]]],
                                           bf2: CanBuildFrom[List[A], A, M[A]]): M[A] = {
    in.to[List].filter(_.isDefined).map(_.get).to[M]
  }
}

object RichOptionObject {
  implicit def fromOptionObject(optionObject: Option.type): RichOptionObject = new RichOptionObject(optionObject)
}