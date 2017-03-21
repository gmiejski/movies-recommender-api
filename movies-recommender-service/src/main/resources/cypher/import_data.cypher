USING PERIODIC COMMIT 10000
LOAD CSV WITH HEADERS FROM "file:///Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/full.data" AS line
WITH line

MERGE (b:Person {user_id: TOINT(line.user_id)} )
MERGE (m:Movie {movie_id: TOINT(line.mv_id)} )
MERGE (b)-[r:Rated {rating: TOFLOAT(line.rating)}]->(m)
return count(b)