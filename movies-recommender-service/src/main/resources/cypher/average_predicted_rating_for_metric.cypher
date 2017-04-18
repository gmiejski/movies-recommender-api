MATCH (others:Person)-[r:Rated]-(m:Movie)
where m.movie_id = movie
with r.rating as rating, m.movie_id as movie, original_rating, user
return user, movie, original_rating, avg(rating) as prediction , count(rating) as movie_neighbours_ratings