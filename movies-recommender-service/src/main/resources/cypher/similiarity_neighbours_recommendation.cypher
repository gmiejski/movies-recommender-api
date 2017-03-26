MATCH (b:Person)-[r:Rated]->(m:Movie), (b)-[s:Similarity]-(a:Person {user_id: 115})
WHERE NOT((a)-[:Rated]->(m))
WITH m, s.similarity AS similarity, r.rating AS rating
ORDER BY m.movie_id, similarity DESC
WITH m.movie_id AS movie, COLLECT(rating)[0..3] AS ratings
WITH movie, REDUCE(s = 0, i IN ratings | s + i)*1.0 / size(ratings) AS reco
ORDER BY reco DESC
RETURN movie AS Movie, reco AS Recommendation