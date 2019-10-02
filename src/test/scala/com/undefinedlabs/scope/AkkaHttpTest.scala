package com.undefinedlabs.scope

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import okhttp3.{MediaType, OkHttpClient, Request, RequestBody}
import org.scalatest.{BeforeAndAfterAll, WordSpec}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

class AkkaHttpTest extends WordSpec
  with BeforeAndAfterAll {

  implicit val globalSystem: ActorSystem = ActorSystem("helloAkkaHttpServerTest")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = globalSystem.dispatcher

  val routes : Route = new UserRoutes(globalSystem).userRoutes

  override def beforeAll(): Unit = {
    val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 19998)

    serverBinding.onComplete {
      case Success(bound) =>
        println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
      case Failure(e) =>
        Console.err.println(s"Server could not start!")
        e.printStackTrace()
        globalSystem.terminate()
    }
  }

  override def afterAll(): Unit = {
    Await.result(globalSystem.terminate, Duration.apply(5, TimeUnit.SECONDS))
  }

  "A HTTP Client" must {
    "invoke Akka HTTP GET and POST endpoints" in {
      //Given
      val httpClient = new OkHttpClient.Builder().build
      val objMapper = new ObjectMapper with ScalaObjectMapper
      objMapper.registerModule(DefaultScalaModule)

      //When

      val body = RequestBody.create(MediaType.parse("application/json"), objMapper.writeValueAsString(User("John Doe", 42, "SomeCountry")))
      val reqBuilder = new Request.Builder().url("http://localhost:19998/users").post(body)
      val response = httpClient.newCall(reqBuilder.build).execute

      val reqBuilderTwo = new Request.Builder().url("http://localhost:19998/users?q=queryparam")
      val responseTwo = httpClient.newCall(reqBuilderTwo.build).execute

      //Then
      assert(response.isSuccessful)
      assert(responseTwo.isSuccessful)
    }
  }


}

