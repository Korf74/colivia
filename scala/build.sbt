name := "colivia"

version := "1.0"

scalaVersion := "2.11.8"

mainClass in assembly := Some("Client.MainClient")


resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "org.scalafx" %% "scalafx" % "8.0.92-R10"
)

unmanagedJars in Compile += Attributed.blank(file(scala.util.Properties.javaHome)/"lib"/"jfxrt.jar")

fork in run := true

connectInput in run := true



    
