package org.lolhens.commons.process

import java.nio.file.Path

import scala.collection.JavaConverters._

/**
  * Created by Pierre on 02.11.2015.
  */
class ProcessBuilder private(_command: List[String] = Nil,
                             var _path: Option[Option[Path]] = None,
                             var _environment: Option[Map[String, String]] = None,
                             val builder: java.lang.ProcessBuilder = new java.lang.ProcessBuilder()) {
  def command: List[String] = _command

  def setCommand(value: List[String]): ProcessBuilder = copy(_command = value)

  def path: Option[Path] = _path.getOrElse {
    val path = Option(builder.directory().toPath)
    _path = Some(path)
    path
  }

  def setPath(value: Option[Path]): ProcessBuilder = copy(_path = Some(value))

  def environment: Map[String, String] = _environment.getOrElse {
    val env = builder.environment().asScala.toMap
    _environment = Some(env)
    env
  }

  def setEnvironment(env: Map[String, String]): ProcessBuilder = copy(_environment = Some(env))

  private def copy(_command: List[String] = _command,
                   _path: Option[Option[Path]] = _path,
                   _environment: Option[Map[String, String]] = _environment,
                   builder: java.lang.ProcessBuilder = builder) = {
    new ProcessBuilder(
      _command,
      _path,
      _environment,
      builder)
  }


  def start(): Process = {
    builder.command(command.asJava)

    _environment.foreach { e =>
      val env = builder.environment()
      env.clear()
      env.putAll(e.asJava)
    }

    _path.foreach { e =>
      builder.directory(e.map(_.toFile).getOrElse(null))
    }

    Process(builder.start())
  }
}

object ProcessBuilder {
  def apply(command: List[String]) = new ProcessBuilder(command)

  def apply() = new ProcessBuilder()
}