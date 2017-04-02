MATCH (a:Person)-[r:Rated]->(m:Movie)<-[r2:Rated]-(b:Person)
WHERE a.user_id = {userId} and abs(r.rating - r2.rating) <= 1
WITH b AS neighbour, r2.rating as rating, COUNT(b.user_id) AS sharedRatings
ORDER BY sharedRatings DESC
LIMIT 30
match (neighbour:Person)-[rr:Rated]->(mm:Movie)
where mm.movie_id = {movieId}
return avg(rr.rating) as predictedRating

// old version with similarity
MATCH (target:Person {user_id: 3})-[s:Similarity]-(p:Person)-[r:Rated]-(m:Movie {movie_id: 5})
WITH s.similarity as similarity,r.rating as rating, s.similarity * r.rating as SingleUserPrediction
ORDER BY similarity DESC
LIMIT 30
RETURN sum(SingleUserPrediction) / sum(similarity) as prediction
