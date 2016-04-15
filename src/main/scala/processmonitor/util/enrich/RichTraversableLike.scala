package processmonitor.util.enrich

import scala.collection.TraversableLike
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ListBuffer

/**
  * Created by Pierre on 02.11.2015.
  */
class RichTraversableLike[A, Repr <: TraversableLike[A, Repr]](val self: TraversableLike[A, Repr]) extends AnyVal {
  def mapOption[B, That](f: A => Option[B])
                        (implicit bf: CanBuildFrom[Repr, B, That]): That = {
    val b = bf(self.repr)

    self.foreach(e => f(e).foreach(e1 => b += e1))

    b.result()
  }

  def transform[B, That, C](z: C)(f: (C, A) => (Option[B], C))
                           (implicit bf: CanBuildFrom[Repr, B, That]): (That, C) = {
    val b = bf(self.repr)

    val c = self.foldLeft(z) { (last, e) =>
      val (map, fold) = f(last, e)
      map.foreach(e1 => b += e1)
      fold
    }

    (b.result(), c)
  }

  def mapOrAdd[B, Repr2 <: TraversableLike[B, Repr2], That](alt: (A) => B)(t: Repr2, p: (A, B) => Boolean)
                                                           (implicit bf: CanBuildFrom[Repr, B, That]): That = {
    val b = bf(self.repr)

    val remaining = ListBuffer[B]()

    remaining ++= t

    self.foreach { e =>
      b += t.find(p(e, _)).map { e =>
        remaining -= e
        e
      }.getOrElse(alt(e))
    }

    b ++= remaining

    b.result()
  }
}

object RichTraversableLike {
  implicit def fromTraversableLike[A, Repr <: TraversableLike[A, Repr]]
  (value: TraversableLike[A, Repr]): RichTraversableLike[A, Repr] =
    new RichTraversableLike[A, Repr](value)
}