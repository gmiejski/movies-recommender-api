package org.miejski.movies.recommender.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.miejski.movies.recommender.performance.myhttp.HttpGetRequest

import scala.collection.Iterator
import scala.concurrent.duration._
import scala.language.postfixOps

class RatingsSimulation extends Simulation
  with PropertyReader {

  val applicationUrl = System.getProperty("applicationUrl")

  val waitInterval = readIntProperty("waitInterval", 1000) milliseconds
  val runTime = readIntProperty("runTime", 1) minutes
  val maxUsers = readIntProperty("maxUsers", 10000)

  val httpConf = http
    .baseURL(applicationUrl)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")


  def usersRepository = new IdsRepository(new HttpGetRequest(applicationUrl).getUsersIds)

  def moviesRepository = new IdsRepository(new HttpGetRequest(applicationUrl).getMoviesIds)

  def ratingsFeeder = Iterator.continually()

  val feeder = Iterator.continually(Map("userId" -> usersRepository.getNextId, "movieId" -> moviesRepository.getNextId))

  val ratingsScenario = scenario("ratingsScenario")
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

  setUp(ratingsScenario.inject(rampUsers(maxUsers) over (runTime / 2)))
    .maxDuration(runTime)
    .protocols(httpConf)
}

