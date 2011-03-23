type -P php &>/dev/null || { echo "PHP needs to be installed to run this program.  Aborting." >&2; exit 1; }
type -P java &>/dev/null || { echo "Java needs to be installed to run this program.  Aborting." >&2; exit 1; }
cd `dirname $0`
java -jar dist/SimpleTestGUI.jar
