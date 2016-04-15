package processmonitor.util.iterator

import processmonitor.util._

import scala.io.{Codec, Source}

/**
  * Created by Pierre on 03.11.2015.
  */
object CharStreamIterator {
  def apply(i: Iterator[Byte])(implicit codec: Codec): Iterator[Char] =
    Source.fromByteIterator(i)(codec).iter

  def apply(i: Iterator[Byte], enc: String): Iterator[Char] =
    apply(i)(Codec(enc))
}
