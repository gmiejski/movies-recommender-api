MATCH (p1:Person)-[x:Rated]->(:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct, p1, p2
match (p1)-[r1:Rated]-(:Movie)
with sqrt(sum( (r1.rating - p1.avg_rating)^2)) as p1_denom, xyDotProduct, p1, p2
match (p2)-[r2:Rated]-(:Movie)
with sqrt(sum( (r2.rating - p2.avg_rating)^2)) as p2_denom, p1_denom, xyDotProduct, p1, p2
match (p1)-[s:Similarity]-(p2)
set s.cosine = xyDotProduct / (p1_denom * p2_denom) 