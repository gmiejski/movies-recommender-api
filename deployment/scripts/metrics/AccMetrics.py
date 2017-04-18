import math


class AccMetrics():
    @staticmethod
    def calculate_rmse(predictions_and_ratings):
        "predictions_and_ratings is list of pairs of predicts and real rating"
        if len(predictions_and_ratings) == 0:
            return 0
        diffs = map(lambda x: (x[0] - x[1]) * (x[0] - x[1]), predictions_and_ratings)
        return math.sqrt(sum(diffs) / float(len(predictions_and_ratings)))

    @staticmethod
    def calculate_mae(predictions_and_ratings):
        "predictions_and_ratings is list of pairs of predicts and real rating"
        if len(predictions_and_ratings) == 0:
            return 0
        diffs = map(lambda x: abs(x[0] - x[1]), predictions_and_ratings)
        return sum(diffs) / float(len(predictions_and_ratings))


if __name__ == "__main__":

    mae = AccMetrics.calculate_mae([(4, 5), (3, 5), (2, 5)])
    assert mae == 2
    print(mae)

    rmse = AccMetrics.calculate_rmse([(4, 5), (3, 5), (2, 5)])
    print(rmse)