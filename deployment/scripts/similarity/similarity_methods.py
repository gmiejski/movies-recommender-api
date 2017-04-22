class AvailableSimilarities():
    @staticmethod
    def available_methods():
        return ["similarity", "pearson_with_sw", "cosine"]

    @staticmethod
    def available_methods_string():
        return "[{}]".format(",".join(AvailableSimilarities.available_methods()))

    @staticmethod
    def is_available(method):
        return method in AvailableSimilarities.available_methods()
