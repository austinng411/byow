package byow.Core;

/**
 * @source Josh Hug
 * UC Berkeley CS61B Data Structures
 *
 * initial framework provided by 61b for proj3 sp19
 */

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[0]);
            System.out.println(engine.toString());
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
            // do we want to store this ??
        }
    }
}
