package org.miejski.movies.recommender.helper

import com.google.common.primitives.Primitives
import org.neo4j.ogm.model.Result

class QueryResultMapper {
    fun <T> convert(type: Class<T>, result: Result): List<T> {
        val a = result.map { singleResult -> type.declaredFields.map { field -> Pair(field.name, field.type) }
            .map { field -> singleResult.getOrElse(field.first, {Primitives.wrap(field.second).newInstance()}) } }
            .map { args -> type.constructors.first().newInstance(*args.toTypedArray())
        }
        return a.toList() as List<T>
    }
}

fun <T> Result.castTo(castedClass: Class<T>) : List<T> {
    return QueryResultMapper().convert(castedClass, this)
}