val projectName = "marketing"
val projectVersion = "1.0.0"
val scalaV = "2.11.8"


val scalaXmlV = "1.0.6"
val akkaV = "2.4.17"
val akkaHttpV = "10.0.4"
val hikariCpV = "2.5.1"
val slickV = "3.2.0"
val logbackV = "1.1.7"
val nscalaTimeV = "2.14.0"
val codecV = "1.10"
val postgresJdbcV = "9.4.1208"
val mysqlConnectorV = "5.1.31"

val scalaJsDomV = "0.9.1"
val scalatagsV = "0.6.3"
val sessionV = "0.2.7"


val circeVersion = "0.6.1"
val asyncHttpClientV = "2.0.24"

val playComponentV = "2.5.4"
val playJsonForAkkaHttp = "1.7.0"

val diodeV = "1.1.0"

val projectMainClass = "com.wangchunze.marketing.Boot"

def commonSettings = Seq(
  version := projectVersion,
  scalaVersion := scalaV
)



lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(name := "shared")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion
    )
  )


lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


// Scala-Js frontend
lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(name := "frontend")
  .settings(commonSettings: _*)
  .settings(inConfig(Compile)(
    Seq(
      fullOptJS,
      fastOptJS,
      packageJSDependencies,
      packageMinifiedJSDependencies
    ).map(f => (crossTarget in f) ~= (_ / "pc" / "sjsout"))
  ))
  .settings(skip in packageJSDependencies := false)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV withSources(),
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "me.chrons" %%% "diode" % diodeV,
      "com.lihaoyi" %%% "scalatags" % scalatagsV withSources()
    )
  ).dependsOn(sharedJs)


// Akka Http based backend
lazy val backend = (project in file("backend"))
  .settings(commonSettings: _*)
  .settings(
    Revolver.settings.settings,
    mainClass in Revolver.reStart := Some(projectMainClass)
  ).settings(name := projectName + "_backend")
  .settings(
    //pack
    // If you need to specify main classes manually, use packSettings and packMain
    packSettings,
    // [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String])
    packMain := Map("marketing" -> projectMainClass),
    packJvmOpts := Map("marketing" -> Seq("-Xmx1024m", "-Xms256m")),
    packExtraClasspath := Map("marketing" -> Seq("."))
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scala-lang" % "scala-reflect" % scalaV,
      "org.scala-lang.modules" % "scala-xml_2.11" % scalaXmlV,
      "com.typesafe.akka" %% "akka-actor" % akkaV withSources() withSources(),
      "com.typesafe.akka" %% "akka-remote" % akkaV,
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
      "com.typesafe.akka" %% "akka-stream" % akkaV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
      "com.typesafe.slick" %% "slick" % slickV withSources(),
      "com.typesafe.slick" %% "slick-codegen" % slickV,
      "com.lihaoyi" %% "scalatags" % scalatagsV,
      "com.zaxxer" % "HikariCP" % hikariCpV,
      "ch.qos.logback" % "logback-classic" % logbackV withSources(),
      "com.github.nscala-time" %% "nscala-time" % nscalaTimeV,
      "commons-codec" % "commons-codec" % codecV,
      "org.postgresql" % "postgresql" % postgresJdbcV,
      "mysql" % "mysql-connector-java" % mysqlConnectorV,
      "org.asynchttpclient" % "async-http-client" % asyncHttpClientV,
      "com.typesafe.play" %% "play-cache" % playComponentV,
      "com.softwaremill.akka-http-session" %% "core" % sessionV,
      "com.softwaremill.akka-http-session" %% "jwt"  % sessionV,
      "net.glxn.qrgen" % "javase" % "2.0"
    )
  )
  .settings( // fastOptJS generate
    (resourceGenerators in Compile) += Def.task {
      val fastJsOut = (fastOptJS in Compile in frontend).value.data
      val fastJsSourceMap = fastJsOut.getParentFile / (fastJsOut.getName + ".map")
      Seq(
        fastJsOut,
        fastJsSourceMap
      )
    }.taskValue
  )
  .settings(
  (resourceGenerators in Compile) += Def.task {
    Seq(
      (packageScalaJSLauncher in Compile in frontend).value.data,
      (packageJSDependencies in Compile in frontend).value,
      (packageMinifiedJSDependencies in Compile in frontend).value
    )
  }.taskValue
)
  .settings(
    (resourceDirectories in Compile) += (crossTarget in frontend).value,
    watchSources ++= (watchSources in frontend).value
  )
  .dependsOn(sharedJvm)