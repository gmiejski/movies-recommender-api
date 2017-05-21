package org.miejski.movies.recommender.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.miejski.movies.recommender.performance.myhttp.HttpGetRequest

import scala.collection.Iterator
import scala.concurrent.duration._
import scala.language.postfixOps

class RatingsSimulation extends Simulation {

  val applicationUrl = System.getProperty("applicationUrl")
  val maxUsers =  if (System.getProperty("maxUsers") != null) {
    System.getProperty("maxUsers").toInt
  }  else 10000

  val waitInterval =  if (System.getProperty("waitInterval") != null) {
    System.getProperty("waitInterval").toInt milliseconds
  }  else 1000 milliseconds

  val runTime =  if (System.getProperty("runTime") != null) {
    System.getProperty("runTime").toInt minutes
  }  else 1 minutes

  val httpConf = http
    .baseURL(applicationUrl)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val warmupTime = 2 minutes

  def usersRepository = new IdsRepository(new HttpGetRequest(applicationUrl).getUsersIds)

  def moviesRepository = new IdsRepository(new HttpGetRequest(applicationUrl).getMoviesIds)

  def ratingsFeeder = Iterator.continually()

  val feeder = Iterator.continually(Map("userId" -> usersRepository.getNextId, "movieId" -> moviesRepository.getNextId))

  val recommendationScenario = scenario("recommendationScenario")
    .feed(feeder)
    .during(runTime) {
      pace(waitInterval)
        .exec(
          http("produce_rating")
            .post("/users/${userId}/ratings")
            .body(StringBody("{\n\t\"movieId\": ${movieId},\n\t\"rating\": 3.5\n}"))
            .asJSON
        )
    }

  setUp(recommendationScenario.inject(rampUsers(maxUsers) over warmupTime))
    .maxDuration(runTime + warmupTime)
    .protocols(httpConf)
}

