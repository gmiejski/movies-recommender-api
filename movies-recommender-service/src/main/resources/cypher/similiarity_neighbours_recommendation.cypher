MATCH (p:Person {user_id : 298 })-[s:SIMILARITY]-(neighbour:Person)
where s.similarity > 0.6
with p, s, neighbour
ORDER BY s.similarity DESC
MATCH (neighbour)-[r:Rated]->(m:Movie)
where not (p)-[:Rated]-(m)
with p,s,neighbour, r, m
with m,p,
sum(s.similarity) as denominator,
REDUCE(pr=0, a IN COLLECT( (r.rating - neighbour.avg_rating ) * s.similarity) | pr + a) as counter,
count(s) as movie_neighbours_ratings
return m.movie_id as movie,  (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings
order by prediction desc