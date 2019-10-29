name := "scope-demo-akka-http-scala"

version := "0.1"

scalaVersion := "2.12.10"

resolvers += Resolver.mavenLocal

lazy val akkaHttpVersion = "10.1.10"
lazy val akkaVersion    = "2.6.0-M7"

lazy val root = (project in file("."))
  .enablePlugins(JavaAgent)
  .settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.8"
    )),
    name := "akka-http-quickstart-scala",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.squareup.okhttp3" % "okhttp" % "3.14.3",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.10",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.10",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,
    )
  )

javaAgents += "com.undefinedlabs.scope" % "scope-agent" % "0.2.1-beta.6" % "test"