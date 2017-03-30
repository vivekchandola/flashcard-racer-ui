name := """flashcard-racer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa
)

libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19"

libraryDependencies += "org.powermock" % "powermock-api-mockito" % "1.6.2"  
libraryDependencies += "org.powermock" % "powermock-module-junit4" % "1.6.2"
libraryDependencies += "org.powermock" % "powermock-core" % "1.6.2"

libraryDependencies += "net.sourceforge.cobertura" % "cobertura" % "2.1.1"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-testkit_2.11
libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.5-M2"


EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes

enablePlugins(PlayEbean)

fork in run := true

jacoco.settings