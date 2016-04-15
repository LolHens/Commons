package org.lolhens.commons.concurrent

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Future, Promise}

/**
  * Created by Pierre on 29.10.2015.
  */
object Wait {
  def apply(delay: FiniteDuration)(implicit actorSystem: ActorSystem): Future[Unit] = {
    val promise = Promise[Unit]

    actorSystem.scheduler.scheduleOnce(delay, new Runnable {
      override def run(): Unit = promise.success(())
    })

    promise.future
  }
}
