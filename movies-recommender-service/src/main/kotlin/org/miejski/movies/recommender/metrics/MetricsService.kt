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
        val splittedLine = line.split("\t")
        return RealRating(splittedLine[0].toLong(),
            splittedLine[1].toLong(),
            splittedLine[2].toDouble(),
            splittedLine[3].toLong())
    }

    protected fun <T, D> runAsyncAndGather(realRatings: List<RealRating>,
                                           f: (RealRating) -> () -> (Pair<T, D>)): List<Pair<T, D>> {
        val tasks = realRatings.map {
            Callable<Pair<T, D>>(f(it))
        }

        val newFixedThreadPool = Executors.newFixedThreadPool(15)
        val predictions = newFixedThreadPool.invokeAll(tasks)
        newFixedThreadPool.shutdown()

        val predictedRatings = predictions.map { it.get() }
        return predictedRatings
    }

    abstract fun run(realRatings: List<RealRating>): Double
}