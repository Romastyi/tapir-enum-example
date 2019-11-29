lazy val root = project
  .in(file("."))
  .settings(
    name := "tapir-enum-example",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-model" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % "0.12.4",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % "0.12.4",
      "com.beachape" %% "enumeratum" % "1.5.13",
      "com.beachape" %% "enumeratum-circe" % "1.5.22"
    )
  )
