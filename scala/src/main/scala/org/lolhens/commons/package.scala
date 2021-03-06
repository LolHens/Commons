package org.lolhens

import org.lolhens.commons.enrich._
import org.lolhens.commons.function.RichFunctionObject
import org.lolhens.commons.util.Default

import scala.collection.immutable.LinearSeq
import scala.collection.{Iterator, TraversableLike}
import scala.concurrent.{Await, Future}
import scala.io.Source

/**
  * Created by Pierre on 26.10.2015.
  */
package object commons {
  def default[T]: T = Default[T]

  implicit def richLinearSeq[A](value: LinearSeq[A]): RichLinearSeq[A] = RichLinearSeq.fromLinearSeq[A](value)

  implicit def richTraversableLike[A, Repr <: TraversableLike[A, Repr]]
  (value: TraversableLike[A, Repr]): RichTraversableLike[A, Repr] =
    RichTraversableLike.fromTraversableLike[A, Repr](value)

  implicit def richAny[A](value: A): RichAny[A] = RichAny.fromAny[A](value)

  implicit def richIteratorObject(value: Iterator.type): RichIteratorObject = RichIteratorObject.fromIteratorObject(value)

  implicit def richString(value: String): RichString = RichString.fromString(value)

  implicit def richSourceObject(value: Source.type): RichSourceObject = RichSourceObject.fromSourceObject(value)

  implicit def richAwaitObject(value: Await.type): RichAwaitObject = RichAwaitObject.fromAwaitObject(value)

  implicit def richOption[A](value: Option[A]): RichOption[A] = RichOption.fromOption(value)

  implicit def richFuture[A](value: Future[A]): RichFuture[A] = RichFuture.fromFuture(value)

  implicit def richMap[A, B](value: Map[A, B]): RichMap[A, B] = RichMap.fromMap(value)

  implicit def richBoolean[A](value: Boolean): RichBoolean = RichBoolean.fromBoolean(value)

  implicit def richFunctionObject(functionObject: Function.type): RichFunctionObject = RichFunctionObject.fromFunctionObject(functionObject)

  implicit def richOptionObject(optionObject: Option.type): RichOptionObject = RichOptionObject.fromOptionObject(optionObject)
}
