package org.lolhens.commons.concurrent

import org.lolhens.commons._
import shapeless.ops.function.{FnFromProduct, FnToProduct}
import shapeless.{HList, HNil}

/**
  * Created by Pierre on 26.10.2015.
  */
class Callback[F, L <: HList, R, O](f: F)
                                   (implicit fp: FnToProduct.Aux[F, L => R],
                                    pf: FnFromProduct.Aux[L => Seq[R], O]) {
  @volatile
  private var listeners = List[F]()

  def on(func: F): Unit = listeners = func +: listeners

  def onAll(funcList: Seq[F]): Unit = listeners = funcList.toList ::: listeners

  def call: O = Function.sequence(listeners)
}

object Callback {
  def apply[F, L <: HList, R, O](f: F)
                                (implicit fp: FnToProduct.Aux[F, L => R],
                                 pf: FnFromProduct.Aux[L => Seq[R], O]) =
    new Callback(f)

  def apply() = new Func0Unit()

  class Func0Unit extends Callback[() => Unit, HNil, Unit, () => Seq[Unit]](() => ()) {
    def on(f: => Unit) =
      super.on(() => f)
  }

}
