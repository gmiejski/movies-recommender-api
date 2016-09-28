package org.miejski.movies.recommender.configuration

import com.codahale.metrics.MetricRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MetricsConfiguration {

    @Bean
    open fun metricRegistry(): MetricRegistry {
        return MetricRegistry()
    }
}