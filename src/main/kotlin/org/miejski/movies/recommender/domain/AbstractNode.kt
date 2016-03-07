package org.miejski.movies.recommender.domain

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
open class AbstractNode(@GraphId var id: Long? = null)