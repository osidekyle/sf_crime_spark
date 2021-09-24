name := "sf_geo_crime_project"

version := "0.1"

scalaVersion := "2.12.10"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.jfree" % "jfreechart" % "1.5.3"

)