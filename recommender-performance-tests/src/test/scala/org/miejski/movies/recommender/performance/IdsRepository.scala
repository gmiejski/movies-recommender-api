package org.miejski.movies.recommender.performance

import scala.util.Random

class IdsRepository(ids: Array[Int]) {

  def getNextId = {
    def nextId = ids(Random.nextInt(ids.length))
    print(s"IdsRepository: $nextId")
    nextId
  }
}

