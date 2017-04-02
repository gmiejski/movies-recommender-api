MATCH (p:Person {user_id : 298 })-[s:SIMILARITY]-(neighbour:Person)-[r:Rated]-(m:Movie {movie_id: 325})
where s.similarity > 0.6 and not (p)-[:Rated]-(m)
with p,s,neighbour, r,m
ORDER BY s.similarity DESC
with m,p,
sum(s.similarity) as denominator,
REDUCE(pr=0, a IN COLLECT( (r.rating - neighbour.avg_rating ) * s.similarity) | pr + a) as counter,
count(s) as movie_neighbours_ratings
return m.movie_id as movie,  (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings
