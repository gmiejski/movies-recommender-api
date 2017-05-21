import sys

args = sys.argv

nodes = args[1].split(';')

servers = list(map(lambda x: "server " + x + ":8080;", nodes))

text = "\n".join(servers)
text = text + "\n"

with open("/etc/nginx/conf.d/movie_recommender_api.conf.template") as template:
    lines = template.readlines()
    final = ''.join(lines).replace("${servers}", text)

    with open("/etc/nginx/conf.d/movie_recommender_api.conf", mode="w") as target:
        target.write(final)
