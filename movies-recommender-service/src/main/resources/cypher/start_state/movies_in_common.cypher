USING PERIODIC COMMIT 10000
LOAD CSV WITH HEADERS FROM {moviesInCommonFile} AS line FIELDTERMINATOR '\t'
match (p1:Person {user_id: TOINT(line.p1)})-[s:Similarity]-(p2:Person {user_id: TOINT(line.p2)})
using index p1:Person(user_id)
using index p2:Person(user_id)
set s.movies_in_common = TOINT(line.count)