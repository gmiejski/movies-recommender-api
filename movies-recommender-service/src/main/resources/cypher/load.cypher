import-cypher -d"\t" -i /u.data.csv -o out.csv MERGE (b:Person {user_id: TOINT({user_id})} ) MERGE (m:Movie {movie_id: TOINT({movie_id})} ) MERGE (b)-[r:Rated {rating: TOFLOAT({rating}), timestamp: TOINT({timestamp})}]->(m) return count(b)

import-cypher -o out.csv Match (p:Person) return p limit 10