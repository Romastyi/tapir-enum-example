package com.github.romastyi.tapir.example

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import enumeratum._
import sttp.tapir._
import sttp.tapir.SchemaType.SString
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

sealed abstract class TestEnum(override val entryName: String) extends EnumEntry

object TestEnum extends Enum[TestEnum] with CirceEnum[TestEnum] {
  case object Value1 extends TestEnum("value1")
  case object VAlue2 extends TestEnum("value2")

  override def values: IndexedSeq[TestEnum] = findValues

  implicit def tapirSchema: Schema[TestEnum] = Schema(SString).format("enum").description("Test enumeration")
  implicit def tapirValidator: Validator[TestEnum] = Validator.`enum`(values.toList, i => Some(i.entryName))

}

final case class TestPart1(enumValue: Option[TestEnum])
final case class TestRequest(part1: Option[TestPart1])

object Main extends IOApp {

  import io.circe.Codec
  import io.circe.generic.semiauto._
  import sttp.tapir.json.circe._
  import sttp.tapir.server.http4s._
  import sttp.tapir.docs.openapi._
  import sttp.tapir.openapi.circe.yaml._
  import cats.syntax.applicative._
  import cats.syntax.functor._
  import cats.syntax.either._
  import cats.syntax.semigroupk._
  import org.http4s.syntax.kleisli._

  implicit val codecTestPart1: Codec[TestPart1] = deriveCodec
  implicit val codecTestRequest: Codec[TestRequest] = deriveCodec

  val testEndpoint = endpoint.post.in("test").in(jsonBody[TestRequest])

  override def run(args: List[String]): IO[ExitCode] = {
    val endpoints = testEndpoint.toRoutes(_ => ().asRight[Unit].pure[IO])
    val openApi = new SwaggerHttp4s(testEndpoint.toOpenAPI("Example", "0.0.1").toYaml)
    val routes = Router("/" -> (endpoints <+> openApi.routes[IO])).orNotFound
    BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(routes).resource.use(_ => IO.never).as(ExitCode.Success)
  }
}
