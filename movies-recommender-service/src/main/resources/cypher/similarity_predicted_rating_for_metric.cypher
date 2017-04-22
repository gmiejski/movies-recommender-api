MATCH (p:Person )-[s:Similarity]-(neighbour:Person)-[r:Rated]-(m:Movie)
where p.user_id = user and m.movie_id = movie and s.{similarity_method} > {similarity} and not (p)-[:Rated]-(m)
with p,s,neighbour, r, m, original_rating
ORDER BY s.{similarity_method} DESC
with m,p,
sum(s.{similarity_method}) as denominator,
REDUCE(pr=0, a IN COLLECT( (r.rating - neighbour.avg_rating ) * s.{similarity_method}) | pr + a) as counter,
count(s) as movie_neighbours_ratings,
original_rating
return p.user_id as user, m.movie_id as movie, original_rating, (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings