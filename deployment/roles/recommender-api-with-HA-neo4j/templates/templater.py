import sys

args = sys.argv

logs = open("/home/ec2-user/programming/haproxy/templater.log", mode='w')

master_node_ip = args[1].split('a')
slave_ips = args[2].split('a')

slave_ips = list(filter(lambda x: len(x) > 0, slave_ips))

logs.writelines("Master nodes: {}\n".format(master_node_ip))
logs.writelines("Slave nodes: {}\n".format(str(slave_ips)))
logs.flush()
all_nodes = master_node_ip + slave_ips
nodes_ids = list(range(0, len(all_nodes)))

static_port = 7474

zipped = zip(nodes_ids ,all_nodes)

prepared_strings = list(map(lambda x: "server {}:{};".format(x[1], static_port), zipped))
text = "\n".join(prepared_strings) + "\n"

with open("/etc/nginx/conf.d/nginx.conf.template") as template:
    lines = template.readlines()
    final = ''.join(lines).replace("${servers}", text)

    with open("/etc/nginx/conf.d/nginx.conf", mode="w") as target:
        target.write(final)
