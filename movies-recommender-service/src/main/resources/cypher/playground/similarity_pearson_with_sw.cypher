Match (m:Movie)
with m.movie_id as m, SIZE(()-[:Rated]-(m)) as c
with avg(c) as average_common_ratings
MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2, count(m) as movie_ratings_in_common, average_common_ratings
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct,
sqrt(sum((p1_rate_diff)^2) * sum((p2_rate_diff)^2)) AS denom,
p1, p2, movie_ratings_in_common, average_common_ratings
where denom <> 0
MERGE (p1)-[s:Similarity]-(p2),
CASE movie_ratings_in_common
WHEN movie_ratings_in_common > average_common_ratings  THEN average_common_ratings
ELSE average_common_ratings
END as minimum_for_sw
SET s.pearson_with_sw = (xyDotProduct / denom) * ( min(average_common_ratings,movie_ratings_in_common ) / average_common_ratings)







# from previous pearson similarity


Match (m:Movie)
with m.movie_id as m, SIZE(()-[:Rated]-(m)) as c
with avg(c) as average_common_ratings
match (p1:Person)-[s:Similarity]-(p2:Person)
with average_common_ratings,
size( (p1)-[:Rated]->(:Movie)<-[:Rated]-(p2)) as movie_ratings_in_common,
s
with s, movie_ratings_in_common,
CASE
WHEN movie_ratings_in_common < average_common_ratings  THEN movie_ratings_in_common
ELSE average_common_ratings
END as minimum_for_sw, average_common_ratings
set s.pearson_with_sw = s.similarity * (minimum_for_sw / average_common_ratings)
return s, movie_ratings_in_common, average_common_ratings, minimum_for_sw, (minimum_for_sw / average_common_ratings) as sw


# no UPDATE
explain
with 50 as  average_common_ratings
match (p1:Person)-[s:Similarity]-(p2:Person)
with average_common_ratings,
size( (p1)-[:Rated]->(:Movie)<-[:Rated]-(p2)) as movie_ratings_in_common,
s
with s, movie_ratings_in_common,
CASE
WHEN movie_ratings_in_common < average_common_ratings  THEN movie_ratings_in_common
ELSE average_common_ratings
END as minimum_for_sw, average_common_ratings
return s, movie_ratings_in_common, average_common_ratings, minimum_for_sw, (minimum_for_sw / average_common_ratings) as sw



with 50 as  average_common_ratings
match (p1:Person)-[s:Similarity]-(p2:Person)
with average_common_ratings,
size( (p1)-[:Rated]->(:Movie)<-[:Rated]-(p2)) as movie_ratings_in_common,
s
with s, movie_ratings_in_common,
CASE
WHEN movie_ratings_in_common < average_common_ratings  THEN movie_ratings_in_common
ELSE average_common_ratings
END as minimum_for_sw, average_common_ratings
return s, movie_ratings_in_common, average_common_ratings, minimum_for_sw, (minimum_for_sw / average_common_ratings) as sw



# from nothing


Match (m:Movie)
with m.movie_id as m, SIZE(()-[:Rated]-(m)) as c
with avg(c) as average_common_ratings
MATCH (p1:Person)-[x:Rated]->(m:Movie)<-[y:Rated]-(p2:Person)
with p1, x.rating-p1.avg_rating as p1_rate_diff, y.rating-p2.avg_rating as p2_rate_diff, p2, count(m) as movie_ratings_in_common, average_common_ratings
WITH SUM(p1_rate_diff * p2_rate_diff) AS xyDotProduct,
sqrt(sum((p1_rate_diff)^2) * sum((p2_rate_diff)^2)) AS denom,
p1, p2, movie_ratings_in_common, average_common_ratings
where denom <> 0
with xyDotProduct, denom, p1, p2, movie_ratings_in_common, average_common_ratings,
CASE
WHEN movie_ratings_in_common > average_common_ratings  THEN average_common_ratings
ELSE movie_ratings_in_common
END as minimum_for_sw
MATCH (p1)-[s:Similarity]-(p2)
return (xyDotProduct / denom) as pearson,
(minimum_for_sw / average_common_ratings) as significance_weighting,
((xyDotProduct / denom) * (minimum_for_sw / average_common_ratings)) as pearson_with_sw,
s.similarity, movie_ratings_in_common, minimum_for_sw, average_common_ratings
limit 100
