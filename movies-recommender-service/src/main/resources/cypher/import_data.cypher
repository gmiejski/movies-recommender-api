LOAD CSV WITH HEADERS FROM "file:///Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-computing/src/main/resources/u.data" AS line
WITH line

MERGE (b:Person {user_id: TOINT(line.user_id)} )
MERGE (m:Movie {movie_id: TOINT(line.mv_id)} )
MERGE (b)-[r:Rated {rating: TOFLOAT(line.rating)}]->(m)
return count(b)