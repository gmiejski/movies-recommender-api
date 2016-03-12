package org.miejski.movies.recommender.recommendations

import org.miejski.movies.recommender.helper.castTo
import org.miejski.movies.recommender.movies.MovieRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.template.Neo4jOperations
import org.springframework.stereotype.Service

@Service
class RecommendationsService @Autowired constructor(val movieRepository: MovieRepository,
                                                    val neo4jOperations: Neo4jOperations) {
    fun findRecommendedMovies(userId: String): List<CypherResult> {

        val result = neo4jOperations.query("MATCH (a:Person)-[r:Rated]->(m:Movie)<-[r2:Rated]-(b:Person) WHERE a.user_id = 34 AND abs(r.rating - r2.rating) <= 1 RETURN b.user_id AS neighbour, COUNT(b.user_id) AS sharedRatings, a.user_id as a ORDER BY sharedRatings DESC LIMIT 30", emptyMap<String, String>())

        val results = result.castTo(CypherResult::class.java)

        results.forEach {
            println(it)
        }
        return results
    }
}

data class CypherResult(val neighbour: Int, val sharedRatings: Int, val a: Int)