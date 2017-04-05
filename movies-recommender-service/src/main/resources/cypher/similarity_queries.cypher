match (n:Person)-[s:Similarity]-(m:Person)
where not n.user_id = m.user_id
return avg(s.similarity)



// add mean rating for users
Match (p:Person)-[r:Rated]-(m:Movie)
with p, avg(r.rating) as avg_rating
set p.avg_rating = avg_rating
return count(p)


// Spearman rank correlation:
profile MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
WITH SUM(x.rating * y.rating) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating) | yDot + b^2)) AS yLength,
p1, p2
MERGE (p1)-[s:Similarity_spearman]-(p2)
SET s.similarity = xyDotProduct / (xLength * yLength)

// Pearson correlation
profile MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
WITH SUM((x.rating-p1.avg_rating) * (y.rating-p2.avg_rating)) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating - p1.avg_rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating - p2.avg_rating) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
MERGE (p1)-[s:Similarity]-(p2)
SET s.similarity = xyDotProduct / (xLength * yLength)


// Pearson correlation for 2 users
MATCH (p1:Person {user_id: 298})-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person {user_id: 290})
WITH SUM((x.rating-p1.avg_rating) * (y.rating-p2.avg_rating)) AS xyDotProduct,
SQRT(REDUCE(xDot = 0.0, a IN COLLECT(x.rating - p1.avg_rating) | xDot + a^2)) AS xLength,
SQRT(REDUCE(yDot = 0.0, b IN COLLECT(y.rating - p2.avg_rating) | yDot + b^2)) AS yLength,
p1, p2
where (not xLength = 0) and (not yLength = 0)
return xyDotProduct / (xLength * yLength)

// recommendations for user xxx
MATCH (b:Person)-[r:Rated]->(m:Movie), (b)-[s:Similarity]-(a:Person {user_id: 115})
WHERE NOT((a)-[:Rated]->(m)) and s.similarity > 0.8
WITH m, s.similarity AS similarity, r.rating AS rating, b
ORDER BY m.movie_id, similarity DESC
with { movie: m.movie_id, neighbours: collect(b.user_id)[0..30], similarities: collect(similarity)[0..30], ratings: collect(rating)[0..30]} as e
return e.movie,  size(e.ratings) as s
order by s desc

