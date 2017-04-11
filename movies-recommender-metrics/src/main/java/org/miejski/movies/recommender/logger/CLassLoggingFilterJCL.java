package org.miejski.movies.recommender.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class CLassLoggingFilterJCL extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLoggerName().contains("JarClassLoader")) {
            return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;
    }
}