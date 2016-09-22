from itertools import chain

import collections


def flatten(listOfLists):
    return list(chain.from_iterable(listOfLists))


class FoldsCreator():
    def __init__(self, folds_filename_prefix="", output_directory=".", with_asserts=True):
        self.folds_filename_prefix = folds_filename_prefix
        self.output_directory = output_directory
        self.data_separator = "\t"
        self.with_asserts = with_asserts

    def create(self, ratings_file_path, k=5):
        if self.folds_filename_prefix is "":
            self.folds_filename_prefix = ratings_file_path[ratings_file_path.rindex("/"):]

        with open(ratings_file_path) as ratingsFile:
            usersRatings = collections.defaultdict(list)

            for line in ratingsFile:
                if line.startswith("user_id"):
                    continue
                split = line.replace("\n", '').split(self.data_separator)
                user_hash = split[0].__hash__() % k
                usersRatings[user_hash].append(split)

            print("Number of ratings in each hash bucket")
            print(list(map(lambda x: len(x[1]), usersRatings.items())))
            print("Number of users in each hash bucket")
            print(list(map(lambda y: len(collections.Counter(list(map(lambda t: t[0], y))).keys()),
                           map(lambda x: x[1], usersRatings.items()))))

            self.createFolds(usersRatings, k)

    def split_test_for_query(self, test_set, query_ratio):

        query_index = int(query_ratio * len(test_set))
        sorted_by_date = list(sorted(test_set, key=lambda x: int(x[3])))
        query_ratings = sorted_by_date[:query_index]
        real_test_ratings = sorted_by_date[query_index:]
        return query_ratings, real_test_ratings

    def createFolds(self, hashed_users_ratings, k, query_ratio=0.8):
        users_ratings_buckets = list(map(lambda x: x[1], hashed_users_ratings.items()))
        for i in range(0, k):
            query_ratings, real_test_ratings = self.split_test_for_query(hashed_users_ratings[i], query_ratio)
            point_in_time = int(query_ratings[-1][3])

            base_training_ratings = flatten(users_ratings_buckets[:i] + users_ratings_buckets[i + 1:])
            real_training_ratings = list(
                filter(lambda r: int(r[3]) < point_in_time, base_training_ratings)) + query_ratings

            self.assert_proper_folds(real_training_ratings, real_test_ratings)

            self.write_training_data(real_training_ratings, i)
            self.write_test_data(real_test_ratings, i)

    def get_output_file(self, output_file_name):
        if self.output_directory != "":
            return open(self.output_directory + "/" + output_file_name, mode="w")
        return open(output_file_name, mode="w")

    def write_training_data(self, real_training_ratings, i):
        output_file_name = self.folds_filename_prefix + "_train_" + str(i)
        self.write_data(output_file_name, real_training_ratings)

    def write_data(self, output_file_name, real_training_ratings):
        with self.get_output_file(output_file_name) as ratingsFile:
            ratingsFile.write("user_id\tmovie_id\trating\ttimestamp\n")
            for x in real_training_ratings:
                ratingsFile.write(self.data_separator.join(x) + "\n")

    def write_test_data(self, real_training_ratings, i):
        output_file_name = self.folds_filename_prefix + "_test_" + str(i)
        self.write_data(output_file_name, real_training_ratings)

    def assert_proper_folds(self, real_training_ratings, real_test_ratings):
        if self.with_asserts:
            latest_training = max(map(lambda x: int(x[3]), real_training_ratings))
            earliest_test = min(map(lambda x: int(x[3]), real_test_ratings))
            assert latest_training <= earliest_test


if __name__ == "__main__":
    # prefix = "ml-100k"
    # prefix = "ml-1m"
    # prefix = "ml-10m"
    prefix = "ml-20m"
    FoldsCreator(prefix,
                 "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/" + prefix + "/cross_validation") \
        .create("/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/" + prefix + "/full.data")
