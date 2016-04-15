package processmonitor.util

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

/**
  * Created by Pierre on 04.11.2015.
  */
class Iteratee[-In, Out](map: In => Option[A] forSome {type A <: Out}) {
  private val promise = Promise[Out]

  def apply(in: In): Unit = {
    try {
      map(in) match {
        case Some(value) =>
          promise.success(value)
        case None =>
      }
    } catch {
      case e: Throwable =>
        promise.failure(e)
    }
  }

  val result: Future[Out] = promise.future

  def newInstance: Iteratee[In, Out] = new Iteratee[In, Out](map)
}

object Iteratee {
  def apply[In, Out](map: In => Option[A] forSome {type A <: Out}) =
    new Iteratee[In, Out](map)

  def sequence[In, Out](in: Seq[In], map: In => Option[A] forSome {type A <: Out}): Future[Seq[Out]] = {
    val promise = Promise[Seq[Out]]

    val buffer = ListBuffer[Out]()

    promise.completeWith(Future {
      var iteratee = Iteratee[In, Out](map)

      def register: Unit = {
        iteratee.result.onSuccess {
          case e =>
            buffer += e
            iteratee = iteratee.newInstance
            register
        }

        iteratee.result.onFailure {
          case e: Throwable =>
            promise.failure(e)
        }
      }

      in.foreach(iteratee(_))

      buffer.toList
    })

    promise.future
  }
}