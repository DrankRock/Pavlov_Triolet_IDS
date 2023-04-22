import argparse
import subprocess


# External libraries
slf4j = "jars/slf4j-api-1.7.36.jar"
slf4j_simple = "jars/slf4j-simple-1.7.36.jar"
amqp = "jars/amqp-client-5.16.0.jar"

# Arguments parsing
def args():
    parser = argparse.ArgumentParser(description='Generation de noeuds')
    parser.add_argument(
        '-i',
        '--input',
        help="Fichier d'input contenant le graph à générer",
        required=False,
        action='store',
        nargs=1
    )
    parser.add_argument(
        '-m',
        '--make',
        help="Compile les fichiers java",
        required=False,
        action='store_true',
    )
    parser.add_argument(
        '-k',
        '--kill',
        help="Tue toutes les instances actives de Java",
        required=False,
        action='store_true',
    )

    return parser.parse_args()

def run_cmd(command, name):
    # Use subprocess to run the command
    process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
    output, error = process.communicate()

    # Check if the command was successful
    if process.returncode == 0:
        print("[exec.py] ",name, " Executed normally. ")
    else:
        print("Error executing ", name)
    return output

commands = {}
args = args()

if args.kill :
    get_pss = "ps | grep java | grep -v grep | awk '{print $1}'"
    pss_array = run_cmd(get_pss, "Get running java process").decode().split("\n")
    for pss in pss_array :
        if pss.isdigit() :
            cmd = "kill {}".format(pss)
            run_cmd(cmd, "Kill process {}".format(pss))

if args.make :
    print("[exec.py] Compiling java files ...")
    remove_class = "rm ./src/*.class"
    compile_java = "javac -cp .:jars/slf4j-api-1.7.36.jar:jars/slf4j-simple-1.7.36.jar:jars/amqp-client-5.16.0.jar:src/ ./src/*.java"
    run_cmd(remove_class, "remove.class files")
    run_cmd(compile_java, "Compiling java files")

if args.input :
    # Using Bellman Ford :
    with open(args.input[0], 'r') as graph_file:
        lines = [line.strip() for line in graph_file.readlines() if line.strip()]
        number = 0
        n_elements = len(lines[0].split(" "))
        # print(n_elements)
        for line in lines :
            nodes = line.split(" ")
            connected = []
            for j in range(len(nodes)):
                if nodes[j] == '1' and j != number:
                    connected.append("{}".format(j))
            print("{} connected to : {}".format(number, connected));
            commands[int(number)] = [
                    "java", 
                    "-cp", 
                    ".:{}:{}:{}:src/".format(amqp, slf4j, slf4j_simple), 
                    "src/Main.java", 
                    "{}".format(args.input[0]),
                    "{}".format(connected),
                    "{}".format(number)
                ]
            number += 1

    for cmd in commands.values() :
        subprocess.Popen(cmd) 

