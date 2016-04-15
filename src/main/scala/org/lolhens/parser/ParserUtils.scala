package org.lolhens.parser

import java.util.regex.Pattern

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.{Position, Reader}

/**
  * Created by LolHens on 25.07.2015.
  */
class ParserUtils extends RegexParsers {
  override def skipWhitespace: Boolean = false

  def optFrame[T](pre: Parser[Any], main: Parser[T], post: Parser[Any]): Parser[T] = (pre ~> main <~ post) | main

  def lowerCase[T](parser: Parser[T]) = new Parser[T] {
    def apply(in: Input) = {
      class LowerCaseReader(val self: Reader[Char]) extends Reader[Char] {
        override def source: CharSequence = self.source.toString.toLowerCase

        override def offset: Int = self.offset

        override def first: Char = self.first.toLower

        override def atEnd: Boolean = self.atEnd

        override def pos: Position = self.pos

        override def rest: Reader[Char] = new LowerCaseReader(self)
      }

      parser.apply(new LowerCaseReader(in))
    }
  }

  def decimalNumber: Parser[String] = """-?\d+""".r

  def binaryNumber: Parser[String] = "0b" ~> """[01]+""".r

  def hexNumber: Parser[String] =
    """-?""".r ~ ("0x" ~> """[0-9a-fA-F]+""".r) ^^ {
      case a ~ b => s"$a$b"
    }

  def intType: Parser[Int] = (
    binaryNumber ^^ (Integer.parseInt(_, 2))
      | hexNumber ^^ ((hexNum) => Integer.parseInt(if (hexNum.startsWith("-")) "-0" + hexNum.drop(1) else "0" + hexNum, 16))
      | decimalNumber ^^ (_.toInt)
    )

  def shortType: Parser[Short] = intType ^^ (_.toShort)

  def byteType: Parser[Byte] = intType ^^ (_.toByte)

  def doubleType: Parser[Double] = """-?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?[dD]?""".r ^^ (_.toDouble)

  def floatType: Parser[Float] = """-?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?[fF]?""".r ^^ (_.toFloat)

  def booleanType: Parser[Boolean] = ("true" | "false") ^^ {
    case "true" => true
    case "false" => false
  }

  def stringType: Parser[String] = ("\"" + """([^"\p{Cntrl}\\]|\\[\\'"bfnrt]|\\u[a-fA-F0-9]{4})*+""" + "\"").r

  def wordType: Parser[String] = """(\w)+""".r

  def textType(pre: List[String], post: List[String]): Parser[String] = s"${except(pre).getOrElse("")}${except(post).getOrElse(".")}+".r

  protected def except(list: List[String]): Option[String] = {
    val except = list.map(quote).mkString("")
    if (except == "") None else Some(s"[^$except]")
  }

  protected def quote(string: String) = if (string == "") "" else Pattern.quote(string)

  implicit def parserToRichParser[V](parser: Parser[V]) = new RichParser[V](parser)

  implicit def literalToRichParser(s: String): RichParser[String] = new RichParser(literal(s))

  implicit def regexToRichParser(r: Regex): RichParser[String] = new RichParser(regex(r))

  class RichParser[V](val self: Parser[V]) {
    def ~~[U](q: => Parser[U]): Parser[~[V, U]] = self ~ (whiteSpace ~> q)

    def ~~>[U](q: => Parser[U]): Parser[U] = (self ~ whiteSpace) ~> q

    def <~~[U](q: => Parser[U]): Parser[V] = self <~ (whiteSpace ~ q)


    /*def ~?~[U](q: => Parser[U]): Parser[~[V, U]] = self ~ (opt(whiteSpace) ~> q)

    def ~?>[U](q: => Parser[U]): Parser[U] = (self ~ opt(whiteSpace)) ~> q

    def <?~[U](q: => Parser[U]): Parser[V] = self <~ (opt(whiteSpace) ~ q)*/
  }

}