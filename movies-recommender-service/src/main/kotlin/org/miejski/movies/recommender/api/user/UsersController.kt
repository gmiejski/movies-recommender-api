package org.miejski.movies.recommender.api.user

import org.miejski.movies.recommender.api.user.dto.MovieRatingRequest
import org.miejski.movies.recommender.domain.user.Person
import org.miejski.movies.recommender.domain.user.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
class UsersController @Autowired constructor(val usersService: UsersService) {

    @RequestMapping(value = "/users/{userId}")
    fun getUser(@PathVariable("userId") userId: Long): Person {
        return usersService.findUserById(userId)
    }

    @RequestMapping(value = "/users")
    fun getAllUsers(): List<Person> {
        return emptyList()
        return usersService.findAll()
    }

    @RequestMapping(value = "/users/ids")
    fun getAllUsersIds(): List<Int> {
        return usersService.findAllIds()
    }

    @RequestMapping(value = "/users/{userId}/ratings",
        method = arrayOf(POST),
        consumes = arrayOf(APPLICATION_JSON_VALUE))
    fun rateMovie(@RequestBody movieRating: MovieRatingRequest, @PathVariable("userId") userId: Long) {
        return usersService.rateMovie(userId, movieRating)
    }
}