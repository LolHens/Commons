package processmonitor.util.process

import java.nio.file.Path

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

/**
  * Created by Pierre on 02.11.2015.
  */
class Process private(val process: java.lang.Process) {
  private var _result: Option[Future[Int]] = None

  def result: Future[Int] = _result.getOrElse {
    val future = Future(process.waitFor())
    _result = Some(future)
    future
  }

  def onResult(listener: => Unit) = result.onSuccess {
    case _ => listener
  }

  def isAlive = process.isAlive

  val outputStream = process.getInputStream

  val errorStream = process.getErrorStream

  val inputStream = process.getOutputStream

  val output = Source.fromInputStream(outputStream)

  val error = Source.fromInputStream(errorStream)

  def close(force: Boolean = false) =
    if (force)
      process.destroyForcibly()
    else
      process.destroy()
}

object Process {
  def apply(process: java.lang.Process) = new Process(process)

  def apply(command: List[String], path: Option[Option[Path]] = None, environment: Option[Map[String, String]] = None) = {
    val tmp1 = ProcessBuilder(command) match {
      case value if path.isDefined => value.setPath(path.get)
      case value => value
    }
    val tmp2 = tmp1 match {
      case value if environment.isDefined => value.setEnvironment(environment.get)
      case value => value
    }
    tmp2.start()
  }
}