name := "AWSGlue"

version := "0.1"

scalaVersion := "2.11.12"

lazy val root = (project in file("."))
    .settings(
      name := "AWSGlue",
      settings,
      libraryDependencies ++= commonDependencies
    )

lazy val settings = Seq(
  scalacOptions ++= Seq(),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    "aws-glue-etl-artifacts" at "https://aws-glue-etl-artifacts.s3.amazonaws.com/release/"
  )
)

lazy val commonDependencies = Seq(
  "com.amazonaws" % "AWSGlueETL" % "1.0.0"
//  , "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
