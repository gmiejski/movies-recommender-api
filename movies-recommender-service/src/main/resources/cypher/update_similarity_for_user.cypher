MATCH (p1:Person {user_id: 298})-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
WITH SUM((x.rating-p1.avg_rating) * (y.rating-p2.avg_rating)) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating - p1.avg_rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating - p2.avg_rating) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
with p1, p2, xyDotProduct / (xLength * yLength) as similarity
MERGE (p1)-[s:SIMILARITY]-(p2)
with p1, p2, s, similarity
where not s.similarity = similarity
SET s.similarity = similarity