MATCH (p:Person )-[s:Similarity]-(neighbour:Person)-[r:Rated]-(m:Movie)
where p.user_id = {userId} and m.movie_id = {movieId} and s.similarity > 0 and not (p)-[:Rated]-(m)
with p,s,neighbour, r,m
ORDER BY s.similarity DESC
with p,
m.movie_id as movie,
original_rating,
collect(rating)[..3] as neighbours_ratings,
collect(s.{similarity_method})[..3] as neighbours_similarities,
collect(neighbour.avg_rating)[..3] as neighbours_avg_ratings,
range(0,3-1,1) as indexes
unwind indexes as i
with p, movie, original_rating,
neighbours_ratings[i] as rating,
neighbours_similarities[i] as similarity,
neighbours_avg_ratings[i] as avg_rating
with p, movie, original_rating,
sum(similarity) as denominator,
sum((rating - avg_rating ) * similarity) as counter,
count(similarity) as movie_neighbours_ratings
return m.movie_id as movie,  (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings
