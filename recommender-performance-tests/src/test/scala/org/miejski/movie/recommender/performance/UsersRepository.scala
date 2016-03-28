package org.miejski.movie.recommender.performance

import scala.util.Random

class UsersRepository(usersIds: Array[Int]) {

  def getNextId() = {
    def nextUserId = usersIds(Random.nextInt(usersIds.length))
    print(nextUserId)
    nextUserId
  }
}

