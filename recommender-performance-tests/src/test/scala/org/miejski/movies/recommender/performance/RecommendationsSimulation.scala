package org.miejski.movies.recommender.performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.miejski.movies.recommender.performance.myhttp.HttpGetRequest

import scala.collection.Iterator
import scala.concurrent.duration._
import scala.language.postfixOps

class RecommendationsSimulation extends Simulation
  with PropertyReader{

  val applicationUrl = System.getProperty("applicationUrl")
  val similarityMethod = System.getProperty("similarityMethod")
  val minSimilarity = System.getProperty("minSimilarity")
  val neighboursCount = System.getProperty("neighboursCount")

  val waitInterval = readIntProperty("waitInterval", 500) milliseconds
  val runTime = readIntProperty("runTime", 1) minutes
  val maxUsers = readIntProperty("maxUsers", 30)

  val httpConf = http
    .baseURL(applicationUrl)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  def usersRepository = new IdsRepository(new HttpGetRequest(applicationUrl).getUsersIds)

  val feeder = Iterator.continually(Map(
    "userId" -> usersRepository.getNextId,
    "minSimilarity" -> minSimilarity,
    "similarityMethod" -> similarityMethod,
    "neighboursCount" -> neighboursCount))

  val recommendationScenarion = scenario("recommendationScenario")
    .feed(feeder)
    .during(runTime) {
      pace(waitInterval)
        .exec(
          http("get_recommendations")
            .get("/recommendations/user/${userId}?minSimilarity=${minSimilarity}&similarityMethod=${similarityMethod}&neighboursCount=${neighboursCount}")
        )
    }

  setUp(recommendationScenarion.inject(rampUsers(maxUsers) over (runTime / 2)))
    .maxDuration(runTime)
    .protocols(httpConf)
}

