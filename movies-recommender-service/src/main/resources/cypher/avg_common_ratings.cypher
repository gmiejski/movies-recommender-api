Match (p:Person)-[s:Similarity]-(p2:Person)
with s.movies_in_common as c
return avg(c)