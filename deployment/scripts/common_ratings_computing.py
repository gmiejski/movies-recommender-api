from ratings_in_common.ratings_in_common import RatingsInCommon


def loadData(path):
    with open(path) as f:
        lines = f.readlines()
        return list(
            map(lambda line_splited: (line_splited[0], line_splited[1]),
                map(lambda x: x.split("\t"),
                    lines[1:])))


if __name__ == "__main__":
    data = loadData(
        "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_4")
    result_file = "/Users/grzegorz.miejski/home/workspaces/datasets/movielens/prepared/ml-100k/cross_validation/ml-100k_train_4-ratingsInCommon"
    RatingsInCommon().results(data, result_file)
