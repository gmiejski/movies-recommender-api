package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.users.api.MovieRatingRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class UsersController @Autowired constructor(val usersService: UsersService) {

    @RequestMapping(value = "/users/{userId}")
    fun getUser(@PathVariable("userId") userId: Long): Person {
        return usersService.findUser(userId)
    }

    @RequestMapping(value = "/users")
    fun getAllUsers(): List<Person> {
        return usersService.findAll()
    }

    @RequestMapping(value = "/users/ids")
    fun getAllUsersIds(): List<Int> {
        return usersService.findAllIds()
    }

    @RequestMapping(value = "/users/{userId}/ratings",
        method = arrayOf(RequestMethod.POST),
        consumes = arrayOf("application/json"))
    fun rateMovie(@RequestBody movieRating: MovieRatingRequest, @PathVariable("userId") userId: Long) {
        return usersService.rateMovie(userId, movieRating)
    }
}