package org.miejski.movies.recommender.performance

import scala.util.Random

class UsersRepository(usersIds: Array[Int]) {

  def getNextId() = {
    def nextUserId = usersIds(Random.nextInt(usersIds.length))
    print(s"UsersRepository: $nextUserId")
    nextUserId
  }
}

