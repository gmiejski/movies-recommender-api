package org.miejski.movie.recommender.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.miejski.movie.recommender.performance.myhttp.HttpGetRequest

import scala.collection.Iterator
import scala.concurrent.duration._

class MainSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val time = 5 * 60
  val waitInterval = 1000.0 / 300

  def usersRepository = new UsersRepository(new HttpGetRequest("http://localhost:8080").getUsersIds())

  def a = Map(("userId", usersRepository.getNextId()))

  val feeder = Iterator.continually(a)

  val recommendationScenarion = scenario("scenario")
    .feed(feeder)
    .during(5 seconds) {
      pace(waitInterval millisecond)
        .exec(
          http("get_recommendations")
            .get("/recommendations/user/${userId}")
          //            .check(status in TestConfig().expectedResultCodes)
        )
    }

  setUp(
    recommendationScenarion.inject(atOnceUsers(1))
  ).protocols(httpConf)
}

