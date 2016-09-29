package org.miejski.movies.recommender.metrics

import org.miejski.movies.recommender.metrics.accuracy.RealRating
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Callable
import java.util.concurrent.Executors

abstract class MetricsService {

    fun run(testFilePath: String?): Double {
        val allLines = Files.readAllLines(Paths.get(testFilePath))
        val tasks = allLines.drop(1)
            .map { toRating(it) }
        return run(tasks)
    }

    protected fun toRating(line: String): RealRating {
        val splitLines = line.split("\t")
        return RealRating(splitLines[0].toLong(),
            splitLines[1].toLong(),
            splitLines[2].toDouble(),
            splitLines[3].toLong())
    }

    /**
     * Function that accepts list of elements S, that each will produce a resulting pair of types <T,D>
     */
    protected fun <S, R> runAsyncAndGather(inputList: List<S>,
                                           f: (S) -> (R)): List<R> {
        val tasks = inputList.map {
            Callable<R>({ f(it) })
        }

        val newFixedThreadPool = Executors.newFixedThreadPool(15)
        val predictions = newFixedThreadPool.invokeAll(tasks)
        newFixedThreadPool.shutdown()

        val predictedRatings = predictions.map { it.get() }
        return predictedRatings
    }

    abstract fun run(realRatings: List<RealRating>): Double
}