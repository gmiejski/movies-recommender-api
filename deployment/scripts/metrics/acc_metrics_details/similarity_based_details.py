class SimilarityBasedDetails:
    def __init__(self, similarity, similarity_method):
        self.similarity = similarity
        self.similarity_method = similarity_method

    def prepare_metric_cypher(self, testFilePath):
        prediction_cypher = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/similarity_predicted_rating_for_metric.cypher"
        cypher_template = self.load_file(prediction_cypher)

        prefix = """LOAD CSV WITH HEADERS FROM 'file://{}' AS line FIELDTERMINATOR '\\t'
    WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
            """.format(testFilePath)

        ready_cypher = prefix + cypher_template.replace("{similarity}", str(self.similarity)) \
            .replace("{similarity_method}",
                     self.similarity_method)  # TODO inject mean common ratings instead of magic number 18 in cypher
        return ready_cypher

    def load_file(self, prediction_cypher):
        with open(prediction_cypher) as c:
            cypher = c.readlines()
        return " ".join(cypher)

    def get_result_file_name(self):
        return "similarity_based_prediction/accuracy_similarityMethod:{}_neighboursMinSimilarity:{}.log".format(self.similarity_method,
                                                                                         self.similarity)

    def __str__(self):
        return "SimilarityBasedDetails(neighbours_min_similarity={}, similarity_method={})".format(
            self.similarity, self.similarity_method)
