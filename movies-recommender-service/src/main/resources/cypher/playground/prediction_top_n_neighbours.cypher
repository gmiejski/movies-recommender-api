# complete:

LOAD CSV WITH HEADERS FROM 'file:///Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_test_0' AS line FIELDTERMINATOR '\t'
WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
limit 2
MATCH (p:Person)-[s:Similarity]-(neighbour:Person)-[r:Rated]-(m:Movie)
where p.user_id = user and m.movie_id = movie and s.similarity > 0.0 and not (p)-[:Rated]-(m)
with p,s, neighbour, r.rating as rating, m, original_rating
ORDER BY s.similarity DESC
with p,
m.movie_id as movie,
original_rating,
collect(rating)[..3] as neighbours_ratings,
collect(s.similarity)[..3] as neighbours_similarities,
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
return p.user_id as user, movie, original_rating, (p.avg_rating + counter/denominator ) as prediction , movie_neighbours_ratings
