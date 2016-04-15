package org.lolhens.commons.cache

import java.time.LocalDateTime

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.lolhens.commons._
import org.lolhens.commons.cache.AsynchCache.InnerActor
import org.lolhens.commons.concurrent.Callback

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by Pierre on 18.12.2015.
  */
class AsynchCache[A](equals: (A, A) => Boolean,
                     timeout: (A) => Duration)(implicit actorSystem: ActorSystem) {
  private val innerActor = actorSystem.actorOf(Props(new InnerActor[A](equals, timeout)))

  private implicit val askTimeout = Timeout(10 minutes)

  def update(elements: List[A]) = innerActor ! InnerActor.Update[A](elements)

  def elements: Future[List[A]] = (innerActor ? InnerActor.Elements).mapTo[List[A]]

  def onUpdate(listener: List[A] => Unit) = innerActor ! InnerActor.OnUpdate[A](listener)
}

object AsynchCache {

  private class InnerActor[A](equals: (A, A) => Boolean, timeout: (A) => Duration) extends Actor {
    var cache: List[Entry[A]] = Nil
    val _update = Callback((a: List[A]) => ())

    override def receive: Receive = {
      case InnerActor.Update(elements) =>
        elements match {
          case elements: List[A] =>
            val now = LocalDateTime.now()

            cache = cache.mapOrAdd(e => e)(elements.map {
              e => Entry[A](e, timeout(e) match {
                case finiteDuration: FiniteDuration =>
                  Some(now.plusNanos(finiteDuration.toNanos))

                case _ =>
                  None
              })
            }, (a, b) => equals(a.value, b.value)).filterNot(_.timeout match {
              case Some(timeout) =>
                now.isAfter(timeout)

              case None =>
                false
            })

            Future {
              _update.call(elements)
            }
        }

      case InnerActor.Elements =>
        sender ! cache.map(_.value)

      case InnerActor.OnUpdate(listener) =>
        _update.on(listener.asInstanceOf[List[A] => Unit])
    }
  }

  private object InnerActor {

    case class Update[A](elements: List[A])

    case object Elements

    case class OnUpdate[A](listener: (List[A]) => Unit)

  }

  private case class Entry[A](value: A,
                              timeout: Option[LocalDateTime])

}
