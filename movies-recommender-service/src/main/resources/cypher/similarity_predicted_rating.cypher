MATCH (target:Person {user_id: 3})-[s:Similarity]-(p:Person)-[r:Rated]-(m:Movie {movie_id: 5})
WITH s.similarity as similarity,r.rating as rating, s.similarity * r.rating as SingleUserPrediction
ORDER BY similarity DESC
LIMIT 30
RETURN sum(SingleUserPrediction) / sum(similarity) as prediction