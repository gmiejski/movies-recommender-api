MATCH (p:Person )-[s:Similarity]-(neighbour:Person)-[r:Rated]-(m:Movie)
where p.user_id = user and m.movie_id = movie and s.similarity > 0.1 and not (p)-[:Rated]-(m)
with p,s,neighbour, r, m, original_rating
ORDER BY s.similarity DESC
with m,p,
sum(s.similarity) as denominator,
REDUCE(pr=0, a IN COLLECT( (r.rating - neighbour.avg_rating ) * s.similarity) | pr + a) as counter,
count(s) as movie_neighbours_ratings,
original_rating
return p.user_id as user, m.movie_id as movie, original_rating, (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings