# dataset - ml-100k
# v1
# runtime: INTERPRETED. 291105 total db hits in 209 ms

profile Match (p:Person)-[r:Rated]-(m:Movie)
with m.movie_id as m, count(r) as c
return avg(c)


# v2
# runtime: INTERPRETED. 6685 total db hits in 22 ms

profile Match (m:Movie)
with m.movie_id as m, SIZE(()-[:Rated]-(m)) as c
return avg(c)