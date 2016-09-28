package org.miejski.movies.recommender.configuration

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

class ClassLoggingFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent): FilterReply {
        if (event.loggerName.contains("org.neo4j.ogm.drivers.http.request")) {
            return FilterReply.DENY
        }
        return FilterReply.ACCEPT
    }
}