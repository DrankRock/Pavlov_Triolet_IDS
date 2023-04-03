import argparse
import subprocess

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


args = args()
nodes = []
with open(args.input[0], 'r') as graph_file:
    lines = [line.strip() for line in graph_file.readlines() if line.strip()]
    for line in lines :
        nodes.append(line.split(" "))
        print(line)
subprocess.run(["ls", "-l"]) 


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
