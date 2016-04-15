package org.lolhens.commons.enrich

/**
  * Created by Pierre on 03.11.2015.
  */
class RichString(val self: String) extends AnyVal {
  def firstContainedIn(pre: String, post: String): Option[String] = {
    val preIndex = self.indexOf(pre)
    if (preIndex == -1) return None
    val dropped = self.drop(preIndex + pre.length)
    val postIndex = dropped.indexOf(post)
    if (postIndex == -1 || post.isEmpty) return Some(dropped)
    Some(dropped.take(postIndex))
  }
}

object RichString {
  implicit def fromString(str: String): RichString = new RichString(str)
}