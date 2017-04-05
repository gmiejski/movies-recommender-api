LOAD CSV WITH HEADERS FROM {trainingDataFile} AS line FIELDTERMINATOR
  '\t'
WITH line
MERGE (b:Person {user_id: TOINT(line.user_id)} )
MERGE (m:Movie {movie_id: TOINT(line.movie_id)} )
MERGE (b)-[r:Rated {rating: TOFLOAT(line.rating)}]->(m)