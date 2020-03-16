name := "domain-modeling-made-functional"
organization := "com.sebarys"
version := "1.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.sebarys",
      scalaVersion    := "2.13.1"
    )),
    name := "types-to-the-rescue",
    libraryDependencies ++= Seq(

    )
  )