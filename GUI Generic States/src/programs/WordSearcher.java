package programs;

import exceptions.InvalidStateMachineException;
import statemachine.DFAMatcher;
import statemachine.DictionaryGen;
import statemachine.StateI;
import statemachine.StateMachineGenI;

import java.io.*;

/** Program to read in a dictionary file and use a DFA to verify if words are contained in the dictionary */
public class WordSearcher {
    /** No one should make instances of this class */
    private WordSearcher() {}

    /** Execution starts here
     * <p>There should only be one argument, the dictionary file to read from.</p>
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Usage: java programs.WordSearcher <dictionary.txt>");
            System.err.println("Exiting...");
            System.exit(1);
        }
        StateMachineGenI<Character> smg = new DictionaryGen();

        try {
            StateI<Character> ss = smg.read(new File(args[0]));
            DFAMatcher m = new DFAMatcher(ss);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter words to check. ctrl-d to exit");
            String line = br.readLine();
            while(line!=null) {
                line.strip();
                if(m.doesMatch(line)) {
                    System.out.println(line+" matches");
                } else {
                    System.out.println(line+" does not match");
                }
                line = br.readLine();
            }
        } catch (InvalidStateMachineException e) {
            System.err.println(e.getMessage());
            System.err.println("Exiting...");
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(args[1]+" could not be found/read\nExiting...");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unrecoverable and unexpected IO error\nExiting...");
        }
        System.out.println("done");
    }
}
