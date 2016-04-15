package processmonitor.util.enrich

import processmonitor.util.enrich.RichLinearSeq._

import scala.collection.immutable.LinearSeq

/**
  * Created by Pierre on 02.11.2015.
  */
class RichLinearSeq[A](val self: LinearSeq[A]) extends AnyVal {
  def commonSlice(seq: LinearSeq[A]): Option[((Int, Int), Int)] =
    self.indices.map(i => (seq.findSliceFuzzyGreedy(self.drop(i)), i)).collect {
      case (Some(slice), i) => (slice, i)
    }.sortWith((a, b) => a._1._2 > b._1._2)
      .headOption.map(e => ((e._2, e._1._1), e._1._2))

  def findSliceFuzzyGreedy(slice: LinearSeq[A]): Option[(Int, Int)] =
    self.indices.map(i => (self.drop(i), i)).map(s => (s._1.zip(slice).takeWhile(z => z._1 == z._2).size, s._2))
      .sortWith((a, b) => a._1 > b._1).headOption.map(s => (s._2, s._1))
}

object RichLinearSeq {
  implicit def fromLinearSeq[A](value: LinearSeq[A]): RichLinearSeq[A] = new RichLinearSeq[A](value)
}
