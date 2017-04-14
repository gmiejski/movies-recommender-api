

class RMSEMetric():

    @staticmethod
    def calculate(predictions_and_ratings):
        "predictions_and_ratings is list of pairs of predicts and real rating"
        diffs = map(lambda x: (x[0]-x[1]) * (x[0]-x[1]), predictions_and_ratings)
        return abs(sum(diffs) / float(len(predictions_and_ratings)))
