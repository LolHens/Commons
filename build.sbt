name := "Commons"

lazy val settings = Seq(
  version := "0.0.0",

  scalaVersion := "2.11.8",

  resolvers ++= Seq(
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
    Resolver.jcenterRepo
  ),

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % "2.11.8",
    "org.scala-lang" % "scala-reflect" % "2.11.8",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
    "com.typesafe.akka" %% "akka-actor" % "2.4.4",
    "com.typesafe.akka" %% "akka-remote" % "2.4.4",
    "com.chuusai" %% "shapeless" % "2.3.0",
    "io.spray" %% "spray-json" % "1.3.2",
    "com.github.fommil" %% "spray-json-shapeless" % "1.2.0",
    "org.apache.commons" % "commons-lang3" % "3.4"
  ),

  mainClass in Compile := Some(""),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254")
)

lazy val root = Project("Commons", file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalPlugin)
  .settings(settings: _*)