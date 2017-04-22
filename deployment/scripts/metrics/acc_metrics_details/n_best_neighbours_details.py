class NeighboursCountBasedDetails:
    def __init__(self, neighbours_min_similarity, similarity_method, top_n_neighbours):
        self.neighbours_min_similarity = neighbours_min_similarity
        self.similarity_method = similarity_method
        self.top_n_neighbours = top_n_neighbours

    def prepare_metric_cypher(self, testFilePath):
        prediction_cypher = "/Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/movies-recommender-service/src/main/resources/cypher/similarity_predicted_rating_best_neighbours_for_metric.cypher"
        cypher_template = self.load_file(prediction_cypher)

        prefix = """LOAD CSV WITH HEADERS FROM 'file://{}' AS line FIELDTERMINATOR '\\t'
    WITH TOINT(line.user_id) as user, TOINT(line.movie_id) as movie, TOFLOAT(line.rating) as original_rating
            """.format(testFilePath)

        ready_cypher = prefix + cypher_template.replace("{neighbours_min_similarity}", str(self.neighbours_min_similarity)) \
            .replace("{similarity_method}", self.similarity_method) \
            .replace("{n_best_neighbours}", str(self.top_n_neighbours))

        return ready_cypher

    def load_file(self, prediction_cypher):
        with open(prediction_cypher) as c:
            cypher = c.readlines()
        return " ".join(cypher)

    def get_result_file_name(self):
        return "top_neighbours_based_prediction/accuracy_similarityMethod:{}_neighboursMinSimilarity:{}_topN:{}.log".format(
            self.similarity_method, self.neighbours_min_similarity, self.top_n_neighbours)

    def __str__(self):
        return "NeighboursCountBasedDetails(neighbours_min_similarity={}, similarity_method={}, top_n_neighbours={})".format(
            self.neighbours_min_similarity, self.similarity_method, self.top_n_neighbours)
