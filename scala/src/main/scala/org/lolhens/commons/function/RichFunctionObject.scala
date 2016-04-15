package org.lolhens.commons.function

import org.lolhens.commons._
import shapeless.HList
import shapeless.ops.function.{FnFromProduct, FnToProduct}

/**
  * Created by Pierre on 26.10.2015.
  */
class RichFunctionObject(val self: Function.type) extends AnyVal {
  def Self[A] = (value: A) => value

  def SelfOption[A] = (value: A) => Some(value)

  def returnType[F, L <: HList, R](f: F)
                                  (implicit fp: FnToProduct.Aux[F, L => R]): R =
    default[R]

  def withReturnType[F, L <: HList, R, T, Out](f: F, returnType: T)
                                              (implicit fp: FnToProduct.Aux[F, L => R],
                                               pf: FnFromProduct.Aux[L => T, Out]): Out =
    default[Out]

  def identity[F, P, L <: HList, R, Out](f: F)
                                        (implicit fp: FnToProduct.Aux[F, L => R],
                                         pf: FnFromProduct.Aux[L => R, Out]): Out =
    pf { (l: L) =>
      returnType(f)
    }

  def foldLeft[F, P, L <: HList, R, B, Out](seq: Seq[F])
                                           (z: B)
                                           (f: (B, R) => B)
                                           (implicit fp: FnToProduct.Aux[F, L => R],
                                            pf: FnFromProduct.Aux[L => B, Out]): Out =
    pf { (l: L) =>
      seq.foldLeft(z) { (z, e) =>
        f(z, fp(e)(l))
      }
    }

  /** Seq[p => r] => (p => Seq[r])
    *
    * @param seq
    * @return
    */
  def sequence[F, P, L <: HList, R, Out](seq: Seq[F])
                                        (implicit fp: FnToProduct.Aux[F, L => R],
                                         pf: FnFromProduct.Aux[L => Seq[R], Out]): Out =
    pf { (l: L) =>
      seq.map { e =>
        fp(e)(l)
      }
    }
}

object RichFunctionObject {
  implicit def fromFunctionObject(functionObject: Function.type): RichFunctionObject = new RichFunctionObject(functionObject)
}