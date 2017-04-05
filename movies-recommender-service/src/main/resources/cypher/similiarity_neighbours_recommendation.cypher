MATCH (p:Person {user_id : {userId} })-[s:Similarity]-(neighbour:Person)
where s.similarity > {min_similarity}
with p, s, neighbour
ORDER BY s.similarity DESC
MATCH (neighbour)-[r:Rated]->(m:Movie)
where not (p)-[:Rated]-(m)
with p,s,neighbour, r, m
with m,p,
sum(s.similarity) as denominator,
REDUCE(pr=0, a IN COLLECT( (r.rating - neighbour.avg_rating ) * s.similarity) | pr + a) as counter,
count(s) as movie_neighbours_ratings
return m.movie_id as movieId,  (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings as movieNeighboursRatings
order by prediction desc