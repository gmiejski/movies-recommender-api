package org.miejski.movies.recommender.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.miejski.movies.recommender.performance.myhttp.HttpGetRequest

import scala.collection.Iterator
import scala.concurrent.duration._
import scala.language.postfixOps

class RecommendationsSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val runTime = 5 minutes
  val maxUsers = 30
  val waitInterval = 500 milliseconds

  def usersRepository = new UsersRepository(new HttpGetRequest("http://localhost:8080").getUsersIds())

  val feeder = Iterator.continually(Map("userId" -> usersRepository.getNextId))

  val recommendationScenarion = scenario("scenario")
    .feed(feeder)
    .during(runTime) {
      pace(waitInterval)
        .exec(
          http("get_recommendations")
            .get("/recommendations/user/${userId}")
        )
    }

  setUp(recommendationScenarion.inject(rampUsers(maxUsers) over (runTime / 2)))
    .maxDuration(runTime)
    .protocols(httpConf)
}

