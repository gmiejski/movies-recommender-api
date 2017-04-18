class AverageRatingBasedDetails:

    def prepare_metric_cypher(self, testFilePath):
        prediction_cypher = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/average_predicted_rating_for_metric.cypher"
        cypher_template = self.load_file(prediction_cypher)

        prefix = """LOAD CSV WITH HEADERS FROM 'file://{}' AS line FIELDTERMINATOR '\\t'
    WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
            """.format(testFilePath)

        return prefix + cypher_template

    def load_file(self, prediction_cypher):
        with open(prediction_cypher) as c:
            cypher = c.readlines()
        return " ".join(cypher)

    def get_result_file_name(self):
        return "average_rating_based_prediction/accuracy.log"

    def __str__(self):
        return "AverageRatingBasedDetails()"