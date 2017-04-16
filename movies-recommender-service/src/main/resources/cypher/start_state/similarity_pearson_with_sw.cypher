USING PERIODIC COMMIT 10000
LOAD CSV WITH HEADERS FROM {moviesInCommonFile} AS line FIELDTERMINATOR '\t'
match (p1:Person {user_id: TOINT(line.p1)})-[s:Similarity]-(p2:Person {user_id: TOINT(line.p2)})
using index p1:Person(user_id)
using index p2:Person(user_id)
with TOINT(line.count) as movie_ratings_in_common, s, 57 as average_common_ratings
with s, movie_ratings_in_common, average_common_ratings,
CASE
WHEN movie_ratings_in_common < average_common_ratings  THEN movie_ratings_in_common
ELSE average_common_ratings
END as minimum_for_sw
set s.pearson_with_sw = s.similarity * (1.0 *  minimum_for_sw / average_common_ratings)
