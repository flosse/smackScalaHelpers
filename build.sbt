name          := "smackScalaHelpers"

version       := "1.0-snapshot"

organization  := "org.smackscalahelpers"

scalaVersion  := "2.9.1"

libraryDependencies ++= Seq(
  "jivesoftware"              % "smack"               % "3.2.1",
  "jivesoftware"              % "smackx"              % "3.2.1",
  "commons-logging"           % "commons-logging"     % "1.1.1"
)

resolvers ++= Seq(
  "New smack Repository" at "http://maven.jenkins-ci.org/content/repositories/artifacts/",
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases"  at "http://scala-tools.org/repo-releases"
)
