import collections

import itertools


class RatingsInCommon:
    def results(self, data, resuts_file_path):
        users_movies = collections.defaultdict(set)
        results = []
        for x in data:
            users_movies[x[0]].add(x[1])

        user_pairs = list(itertools.combinations(users_movies.keys(), 2))
        for (p1, p2) in user_pairs:
            common_movies_count = len(users_movies[p1].intersection(users_movies[p2]))
            results.append((p1, p2, common_movies_count))
            pass
        self.save_results_to_file(results, resuts_file_path)
        pass

    def save_results_to_file(self, results, resuts_file_path):
        with open(resuts_file_path, mode="w") as file:
            file.write("p1\tp2\tcount\n")
            for r in results:
                file.write("{}\t{}\t{}\n".format(r[0], r[1], r[2]))
