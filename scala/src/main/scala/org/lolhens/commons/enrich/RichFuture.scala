package org.lolhens.commons.enrich

import org.lolhens.commons._
import org.lolhens.commons.enrich.RichFuture.NoneException

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

/**
  * Created by Pierre on 31.03.2016.
  */
class RichFuture[A](val self: Future[A]) extends AnyVal {
  def Or[B >: A](future: Future[B])(implicit executionContext: ExecutionContext): Future[B] = {
    val promise = Promise[B]

    @volatile var failure: Option[Throwable] = None

    self.onComplete {
      case Success(result) => promise.trySuccess(result)
      case f: Failure[_] => failure Then promise.failure Else (failure = Some(f.exception))
    }

    future.onComplete {
      case Success(result) => promise.trySuccess(result)
      case f: Failure[_] => failure Then promise.failure Else (failure = Some(f.exception))
    }

    promise.future
  }

  def And[B](future: Future[B]): Future[(A, B)] = self.zip(future)

  def XOr[B](future: Future[B])(implicit executionContext: ExecutionContext): Future[Either[A, B]] = {
    val promise = Promise[Either[A, B]]

    @volatile var failure: Option[Throwable] = None

    self.onComplete {
      case Success(result) => promise.trySuccess(Left(result))
      case f: Failure[_] => failure Then promise.failure Else (failure = Some(f.exception))
    }

    future.onComplete {
      case Success(result) => promise.trySuccess(Right(result))
      case f: Failure[_] => failure Then promise.failure Else (failure = Some(f.exception))
    }

    promise.future
  }

  def Then[B](f: A => B)(implicit executionContext: ExecutionContext): Future[B] = self.map(f)

  def Then[B](v: => B)(implicit executionContext: ExecutionContext): Future[B] = self.map((e) => v)

  def Maybe[B](f: A => Future[B])(implicit executionContext: ExecutionContext): Future[B] = self.flatMap(f)

  def Maybe[B](v: => Future[B])(implicit executionContext: ExecutionContext): Future[B] = self.flatMap((e) => v)

  def MaybeOption[B](f: A => Option[B])(implicit executionContext: ExecutionContext): Future[B] = {
    val promise = Promise[B]

    self.onComplete {
      case Success(result) => promise.complete(f(result) Then (Success(_)) Else Failure(NoneException))
      case f: Failure[_] => promise.failure(f.exception)
    }

    promise.future
  }

  def Or[B >: A](future: Future[B]): Future[B] = self.fallbackTo(future)
}

object RichFuture {
  implicit def fromFuture[A](future: Future[A]): RichFuture[A] = new RichFuture[A](future)

  object NoneException extends RuntimeException("Option was None")

}