LOAD CSV WITH HEADERS FROM "file:///u.data" AS line FIELDTERMINATOR
  '\t'
WITH line
MERGE (b:Person {user_id: TOINT(line.user_id)} )
MERGE (m:Movie {movie_id: TOINT(line.movie_id)} )
MERGE (b)-[r:Rated {rating: TOFLOAT(line.rating), timestamp: TOINT(line.timestamp)}]->(m)
return count(b)
