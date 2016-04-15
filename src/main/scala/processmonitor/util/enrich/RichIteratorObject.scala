package processmonitor.util.enrich

import scala.collection.{AbstractIterator, Iterator}

/**
  * Created by Pierre on 02.11.2015.
  */
class RichIteratorObject(val self: Iterator.type) extends AnyVal {
  def continuallyWhile[A](elem: => Option[A]): Iterator[A] = new AbstractIterator[A] {
    private var _next: Option[Option[A]] = None

    def hasNext = _next.getOrElse {
      val result = elem
      _next = Some(result)
      result
    }.isDefined

    def next = {
      val result = _next.map { e =>
        _next = None
        e
      }.getOrElse(elem).getOrElse(Iterator.empty.next)
      result
    }
  }
}

object RichIteratorObject {
  implicit def fromIteratorObject(value: Iterator.type): RichIteratorObject = new RichIteratorObject(value)
}
