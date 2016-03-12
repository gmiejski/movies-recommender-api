MATCH (a:Person {user_id: 34})-[r:Rated]->(m:Movie)<-[r2:Rated]-(b:Person)
WHERE abs(r.rating - r2.rating) <= 1
WITH b AS neighbour, COUNT(b.user_id) AS sharedRatings, a
ORDER BY sharedRatings DESC
LIMIT 30
MATCH (neighbour)-[r:Rated]->(m:Movie), (a)-[:Rated]->(m2:Movie)
WHERE r.rating >=4 AND NOT m.movie_id = m2.movie_id
RETURN AVG(r.rating) AS prediction, m.movie_id AS movie_id, COUNT(r) AS ratings_count

