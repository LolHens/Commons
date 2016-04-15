package org.lolhens.commons.iterator

import java.io.InputStream

import org.lolhens.commons._

/**
  * Created by Pierre on 02.11.2015.
  */
object InputStreamIterator {
  def apply(inputStream: InputStream, bufferSize: Int = 128): Iterator[Byte] = {
    val buffer = new Array[Byte](bufferSize)
    var size = 0
    var pos = 0

    def read: Option[Byte] = {
      if (pos >= size - 1) {
        size = inputStream.read(buffer)
        if (size == -1) return None
        pos = 0
      }

      val result = buffer(pos)
      pos += 1
      Option(result)
    }

    Iterator.continuallyWhile(read)
  }
}
