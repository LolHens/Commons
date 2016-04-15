package processmonitor.util

import java.util.concurrent.locks.ReentrantLock


/**
  * Created by Pierre on 30.03.2016.
  */
class CachedValue[A](func: => A) {
  @volatile private var value: Option[A] = None

  private val lock = new ReentrantLock()

  def apply(): A = {
    lock.lock()
    val r = value.getOrElse(func)
    value = Some(r)
    lock.unlock()
    r
  }

  def invalidate(): Unit = {
    lock.lock()
    value = None
    lock.unlock()
  }

  override def equals(o: scala.Any): Boolean = o match {
    case cachedValue: CachedValue[A] => value.equals(cachedValue.value)
    case _ => false
  }
}

object CachedValue {
  def apply[A](func: => A) = new CachedValue[A](func)

  case class Immutable[A](cachedValue: CachedValue[A]) extends CachedValue[A](cachedValue()) {
    override def apply(): A = cachedValue()

    override def invalidate(): Unit = ()
  }

  case class Mutable[A](cachedValue: CachedValue[A]) extends CachedValue[A](cachedValue.apply()) {
    override def apply(): A = cachedValue.apply()

    override def invalidate(): Unit = cachedValue.invalidate()

    def set(value: A): Unit = {
      cachedValue.lock.lock()
      cachedValue.value = Some(value)
      cachedValue.lock.unlock()
    }
  }

}