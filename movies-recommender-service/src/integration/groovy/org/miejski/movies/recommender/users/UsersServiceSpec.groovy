package org.miejski.movies.recommender.users

import org.miejski.movies.recommender.domain.Movie
import org.miejski.movies.recommender.domain.Person
import org.miejski.movies.recommender.infrastructure.IntegrationSpec
import org.miejski.movies.recommender.movies.MovieRepository
import org.miejski.movies.recommender.users.api.MovieRatingRequest
import org.springframework.beans.factory.annotation.Autowired

class UsersServiceSpec extends IntegrationSpec {

    @Autowired
    UsersRepository usersRepository

    @Autowired
    MovieRepository movieRepository

    @Autowired
    UsersService usersService

    def "should save and load graph node from repository"() {
        given:
        usersRepository.save(new Person(null, 10L, []))
        def savedPerson2 = usersRepository.save(new Person(null, 10L, []))

        when:
        def loadedPerson = usersRepository.findOne(savedPerson2.id)

        then:
        print(usersRepository.findAll().size())
        usersRepository.findAll().size() == 2
        with(loadedPerson) {
            id == savedPerson2.id
            user_id == savedPerson2.user_id
        }
    }

    def "should create rating if doesn't exist for given movie"() {
        given:
        def savedUser = usersRepository.save(new Person(null, 10L, []))
        def savedMovie = movieRepository.save(new Movie(null, 100))

        when:
        usersRepository.addMovieRating(savedUser.user_id, 4.0, savedMovie.movie_id)

        then:
        def foundUser = usersRepository.findOne(savedUser.id)
        with(foundUser.ratedMovies.first()) {
            rating == 4.0 as Double
            movie.movie_id == savedMovie.movie_id
            person.user_id == savedUser.user_id
        }
    }

    def "should update rating if user already rated given movie"() {
        given:
        def savedUser = usersRepository.save(new Person(null, 10L, []))
        def savedMovie = movieRepository.save(new Movie(null, 100))
        usersService.rateMovie(savedUser.user_id, new MovieRatingRequest(savedMovie.movie_id, 4.0))

        when:
        usersService.rateMovie(savedUser.user_id, new MovieRatingRequest(savedMovie.movie_id, 5.0))

        then:
        def foundUser = usersRepository.findOne(savedUser.id)
        foundUser.ratedMovies.size() == 1
        with(foundUser.ratedMovies.first()) {
            rating == 5.0 as Double
            movie.movie_id == savedMovie.movie_id
            person.user_id == savedUser.user_id
        }
    }
}
