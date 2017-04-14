MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
WITH SUM((x.rating-p1.avg_rating) * (y.rating-p2.avg_rating)) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating - p1.avg_rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating - p2.avg_rating) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity = xyDotProduct / (xLength * yLength)