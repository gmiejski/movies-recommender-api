create index on :Person(user_id)
create index on :Movie(movie_id)

LOAD CSV WITH HEADERS FROM "file:///Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_0" AS line FIELDTERMINATOR
  '\t'
WITH line
MERGE (b:Person {user_id: TOINT(line.user_id)} )
MERGE (m:Movie {movie_id: TOINT(line.movie_id)} )
MERGE (b)-[r:Rated {rating: TOFLOAT(line.rating)}]->(m)
return count(b)