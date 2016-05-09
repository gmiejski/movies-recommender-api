package org.miejski.movies.recommender.performance.myhttp

import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpHeaders, HttpStatus}
import org.json4s.native.Serialization._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.util.Try

class HttpGetRequest[A](url: String = "http://localhost:8080")(implicit mf: Manifest[A]) {
  private implicit val formats = org.json4s.DefaultFormats
  private val DefaultTimeout = Duration(5, SECONDS).toMillis.toInt
  private val logger = LoggerFactory.getLogger(classOf[HttpGetRequest[A]])
  val httpClient: HttpClient = HttpClients.createDefault()
  val parser: String => A = read[A]

  def getUsersIds = {
    getIds(s"$url/users/ids")
  }

  def getMoviesIds = {
    getIds(s"$url/movies/ids")
  }

  def getIds(path: String) = {
    val httpGet = new HttpGet(path)
    httpGet.setConfig(requestConfigs)
    httpGet.addHeader(HttpHeaders.ACCEPT, "application/json")

    Try(httpClient.execute(httpGet)).flatMap {
      response =>
        response.getStatusLine.getStatusCode match {
          case HttpStatus.SC_OK => Try(readIdsFromResponse(EntityUtils.toString(response.getEntity)))
          case _ => throw new Exception(s"invalid status code ${response.getStatusLine.getStatusCode} for ${httpGet.getURI} ")
        }
    }.get
  }


  def readIdsFromResponse(response: String) = {
    response.replace("[", "").replace("]", "").split(",").map(_.toInt)
  }

  private def requestConfigs: RequestConfig =
    RequestConfig.custom()
      .setConnectionRequestTimeout(DefaultTimeout)
      .setConnectTimeout(DefaultTimeout)
      .setSocketTimeout(DefaultTimeout)
      .build()
}

object HttpGetRequest {

  def main(args: Array[String]) {
    val usersIds = new HttpGetRequest[UsersIds]().getUsersIds()
    usersIds.foreach(println)
  }
}

case class UsersIds(usersIds: List[Int])