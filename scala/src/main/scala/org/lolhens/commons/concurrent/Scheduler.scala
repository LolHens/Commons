package org.lolhens.commons.concurrent

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.ref.WeakReference

/**
  * Created by Pierre on 29.10.2015.
  */
case class Scheduler(implicit val actorSystem: ActorSystem) {
  private val innerActor = actorSystem.actorOf(Props[Scheduler.InnerActor])

  private val callback = Callback()

  def schedule(initialDelay: FiniteDuration, interval: FiniteDuration) =
    innerActor ! Scheduler.InnerActor.Start(this, initialDelay, interval)

  def stop =
    innerActor ! Scheduler.InnerActor.Stop

  def onTick(listener: => Unit) = callback.on(listener)
}

object Scheduler {

  private class InnerActor extends Actor {
    override def receive: Receive = {
      case InnerActor.Start(scheduler, initialDelay, interval) =>
        context.become(started)
        scheduler.actorSystem.scheduler.scheduleOnce(initialDelay, self, InnerActor.Tick(WeakReference(scheduler), interval))
    }

    def started: Receive = {
      case tick@InnerActor.Tick(scheduler, interval) =>
        scheduler.get match {
          case Some(scheduler) =>
            try {
              scheduler.callback.call()
            } catch {
              case e: Throwable =>
                e.printStackTrace()
            }

            scheduler.actorSystem.scheduler.scheduleOnce(interval, self, tick)

          case None =>
            context.become(receive)
        }

      case InnerActor.Stop =>
        context.become(receive)
    }
  }

  private object InnerActor {

    case class Start(scheduler: Scheduler, initialDelay: FiniteDuration, interval: FiniteDuration)

    case class Tick(scheduler: WeakReference[Scheduler], interval: FiniteDuration)

    case object Stop

  }

}
