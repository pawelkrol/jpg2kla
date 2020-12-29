lazy val root = (project in file(".")).settings(
  javaOptions += "-Xmx1G",
  name := "jpg2kla",
  scalaVersion := "2.13.4",
  scalacOptions ++= Seq("-deprecation", "-feature"),
  version := "0.01"
)

fork in run := true

maxErrors := 1

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.8.0",
  "org.c64.attitude" % "afterimage" % "0.08-SNAPSHOT",
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.2.3" % "test"
)

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
    artifact.name + "-" + module.revision + "." + artifact.extension
}

enablePlugins(SbtProguard)

Proguard / proguardFilteredInputs ++= ProguardOptions.noFilter((Compile / packageBin).value)

Proguard / proguardInputs := (Compile / dependencyClasspath).value.files

Proguard / proguardOptions += ProguardOptions.keepMain("com.github.pawelkrol.Converter.Main")

Proguard / proguardOptions += ProguardConf.jpg2kla

Proguard / proguardVersion := "7.0.0"
