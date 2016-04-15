package processmonitor.util.enrich

import java.io.InputStream

import scala.io.{BufferedSource, Codec, Source}

/**
  * Created by Pierre on 04.11.2015.
  */
class RichSourceObject(sourceObject: Source.type) {
  def fromByteIterator(i: Iterator[Byte])(implicit codec: Codec): BufferedSource = {
    val in = new InputStream() {
      override def read(): Int = if (i.hasNext) i.next() else -1
    }

    Source.fromInputStream(in)(codec)
  }

  def fromByteIterator(i: Iterator[Byte], enc: String): BufferedSource =
    fromByteIterator(i)(Codec(enc))
}

object RichSourceObject {
  implicit def fromSourceObject(sourceObject: Source.type): RichSourceObject =
    new RichSourceObject(sourceObject)
}