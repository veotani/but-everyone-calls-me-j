package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn


object Server {

	implicit val system = ActorSystem(Behaviors.empty, "ButEveryoneCallsMeJ")
	implicit val executionContext = system.executionContext

	def main(args: Array[String]): Unit = {
		val route: Route = concat(
				get {
					pathPrefix("hello") {
						complete("Hello, world!")
					}
				}
		)

		val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
		println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
		StdIn.readLine()
		bindingFuture
			.flatMap(_.unbind())
			.onComplete(_ => system.terminate())
	}
}
