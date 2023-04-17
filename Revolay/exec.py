import argparse
import subprocess

slf4j = "jars/slf4j-api-1.7.36.jar"
slf4j_simple = "jars/slf4j-simple-1.7.36.jar"
amqp = "jars/amqp-client-5.16.0.jar"


def args():
    parser = argparse.ArgumentParser(description='Generation de noeuds')
    parser.add_argument(
        '-i',
        '--input',
        help="Fichier d'input contenant le graph à générer",
        required=True,
        action='store',
        nargs=1
    )
    return parser.parse_args()

commands = {}
args = args()
with open(args.input[0], 'r') as graph_file:
    lines = [line.strip() for line in graph_file.readlines() if line.strip()]
    number = 0
    n_elements = len(lines[0].split(" "))
    print(n_elements)
    for line in lines :
        if number >= n_elements :
            # these lines concern path
            connection = line.split(',')
            commands[int(connection[0])].append("{},{}".format(connection[1], connection[2]))
        else :
            nodes = line.split(" ")
            connected = []
            for j in range(len(nodes)):
                if nodes[j] == '1':
                    connected.append("{}".format(j))
            print("{} connected to : {}".format(number, connected));
            commands[int(number)] = [
                    "java", 
                    "-cp", 
                    ".:{}:{}:{}:src/".format(amqp, slf4j, slf4j_simple), 
                    "src/Main.java", 
                    "{}".format(number),
                    "{}".format(n_elements),
                ] + connected
            
            number += 1

for cmd in commands.values() :
    subprocess.Popen(cmd) 

'''
# single node
# syntax : bash exec.sh single nElements currentElementNumber <connected 1> <...>
# would like : bash exec.sh single nElem currentElem <connected> <path>
# with path = 2-1 -> "pour acceder à 2, passe par 1"
if [ "${mode}" == "single" ]; then
  java -cp .:${amqp}:${slf4j}:${slf4j_simple}:src/ src/Main.java $3 ${numberOfElements} ${@:4} &
  exit 1
fi
'''
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
