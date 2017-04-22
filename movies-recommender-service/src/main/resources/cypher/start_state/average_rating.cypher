Match (p:Person)-[r:Rated]-(m:Movie)
with p, avg(r.rating) as avg_rating
set p.avg_rating = avg_rating