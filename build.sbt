import sbt.Keys._

lazy val projectSettings = Seq(
    version := "0.8.1",
    scalaVersion := "2.12.4"
)

val timeLib     = "joda-time" % "joda-time" % "2.9.3"

val logLib      = Seq(  "ch.qos.logback" % "logback-classic" % "1.1.7",
                        "ch.qos.logback" % "logback-core" % "1.1.7",
                        "ch.qos.logback" % "logback-access" % "1.1.7",
                        "org.slf4j" % "slf4j-api" % "1.7.21")

val formatLib   = Seq(  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
                        "net.sf.opencsv" % "opencsv" % "2.3",
                        "commons-io" % "commons-io" % "2.5")


val chartsLib   = Seq(  "org.jfree" % "jfreechart" % "1.0.19",
                        "org.jfree" % "jcommon" % "1.0.23")

val mathLib     = "org.apache.commons" % "commons-math3" % "3.0"

val testsLib    = Seq(  "junit" % "junit" % "4.12",
                        "org.scalatest" % "scalatest_2.11" % "3.0.0-M15",
                        "org.scala-lang" % "scala-reflect" % "2.12.4")


lazy val root = (project in file("."))
.aggregate("ta4j", "ta4jExamples")
.settings(
    name := "ta4jTechyE",
    projectSettings)

lazy val ta4j = (project in file("ta4j"))
.settings(
    libraryDependencies += timeLib,
    libraryDependencies ++= logLib,
    libraryDependencies ++= testsLib,
    libraryDependencies += mathLib,
    libraryDependencies ++= chartsLib)
.settings(projectSettings)

lazy val ta4jExamples = (project in file("ta4j-examples"))
.settings(
    libraryDependencies += timeLib,
    libraryDependencies ++= formatLib,
    libraryDependencies ++= chartsLib)
.dependsOn(ta4j)
.settings(projectSettings)



//libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
