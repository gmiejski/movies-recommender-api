# dataset - ml-100k
# v1
# runtime: INTERPRETED. 887028526 total db hits in 276803

profile MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
WITH SUM((x.rating-p1.avg_rating) * (y.rating-p2.avg_rating)) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating - p1.avg_rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating - p2.avg_rating) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity = xyDotProduct / (xLength * yLength)



# v2
# runtime: INTERPRETED. 823950942 total db hits in 255910 ms.

profile MATCH (p1:Person)-[x:Rated]->(:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(p1_rate_diff) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(p2_rate_diff) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity = xyDotProduct / (xLength * yLength)



# v3
# runtime: INTERPRETED. 823950945 total db hits in 212593 ms

profile MATCH (p1:Person)-[x:Rated]->(:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct,
sqrt(sum((p1_rate_diff)^2) * sum((p2_rate_diff)^2)) AS denom,
p1, p2
where denom <> 0
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity2 = xyDotProduct / denom
