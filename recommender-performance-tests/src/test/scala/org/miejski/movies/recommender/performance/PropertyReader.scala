package org.miejski.movies.recommender.performance

trait PropertyReader {
  def readIntProperty(propertyName: String, default: Int): Int = {
    val property: String = System.getProperty(propertyName)
    if (property != null && !property.equals("null")) {
      property.toInt
    } else default
  }
}
