package org.miejski.movies.recommender.metrics

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
open class MetricsController(@Autowired val accuracyMetricService: AccuracyMetricService) {

    @RequestMapping(
        value = "/metrics/accuracy",
        method = arrayOf(RequestMethod.POST),
        consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    open fun accuracyMetrics(@RequestBody runMetricsRequest: RunMetricsRequest): ResponseEntity<Double> {
        val resultRmse = accuracyMetricService.run(runMetricsRequest.testFilePath)
        return ResponseEntity.ok(resultRmse)
    }

    @RequestMapping(
        value = "/metrics/accuracy/result",
        method = arrayOf(RequestMethod.GET)
    )
    open fun getAccuracyMetricResult(): ResponseEntity<MetricsResult<Double>> {
        return ResponseEntity.ok(accuracyMetricService.finish())
    }
}