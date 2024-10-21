package programs;

import exceptions.InvalidStateMachineException;
import statemachine.DFAGen;
import statemachine.DFAMatcher;
import statemachine.StateI;
import statemachine.StateMachineGenI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StringMatcher {
    /** Execution starts here for reading a DFA file and checking several strings
     * <p>The first argument should be the .dfa file to read. Each additional argument is
     * a string to check.</p>
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        if(args.length<1) {
            System.err.println("Usage: java statemachine.DFAMatcher <file.dfa> <string1> [string2]...");
        }
        StateMachineGenI<Character> smg = new DFAGen();
        try {
            StateI<Character> ss = smg.read(new File(args[0]));
            DFAMatcher m = new DFAMatcher(ss);
            for(int i=1; i<args.length; i++) {
                if(m.doesMatch(args[i])) {
                    System.out.println(args[i]+" matches");
                } else {
                    System.out.println(args[i]+" does not match");
                }
            }
            System.out.println("All strings checked.\nExiting...");
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
    }
}
