MATCH (p1:Person)-[x:Rated]->(:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct,
sqrt(sum((p1_rate_diff)^2) * sum((p2_rate_diff)^2)) AS denom,
p1, p2
where denom <> 0
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity = xyDotProduct / denom
