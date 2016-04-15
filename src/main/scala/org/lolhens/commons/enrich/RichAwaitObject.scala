package org.lolhens.commons.enrich

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable}

/**
  * Created by Pierre on 04.11.2015.
  */
class RichAwaitObject(val self: Await.type) extends AnyVal {
  def until[T](awaitable: Awaitable[T]): T =
    self.result(awaitable, Duration.Inf)
}

object RichAwaitObject {
  implicit def fromAwaitObject(value: Await.type): RichAwaitObject =
    new RichAwaitObject(value)
}