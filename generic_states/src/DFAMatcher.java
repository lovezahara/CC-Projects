import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.io.File;

/** Uses a DFA ta verify that a DFA accepts the string */
public class DFAMatcher {
    StateI<Character> start;
    TraversalHelpers<Character> helpers;

    /** Construct a matcher backed by a DFA
     *
     * @param s start state for the DFA
     */
    public DFAMatcher(StateI<Character> s) {
        start = s;
        helpers = new TraversalHelpers<Character>();
    }

    /** Check that the DFA accepts a string
     *
     * @param s the string to check
     * @return true if accepted, otherwise false
     */
    public boolean doesMatch(String s) {
        try {
            return endState(s).isAccept();
        } catch (UnmatchedException e) {
            return false;
        }
    }

    /** The accept state reached
     *
     * @param s the string to match
     * @return the accept state, if accepted
     * @throws UnmatchedException when the string is rejected
     */
    public StateI<Character> endState(String s) throws UnmatchedException {
        StateI<Character> cur = start;
        for(int i=0; i<s.length(); i++) {
            Character c = s.charAt(i);
            List<TransitionI<Character>> txs = cur.getTransitions();
            cur = helpers.deterministicEqNextFor(c, cur);
            if(cur==null) {
                throw new UnmatchedException();
            }
        }
        return cur;
    }

    /** Execution starts here for reading a DFA file and checking several strings
     * <p>The first argument should be the .dfa file to read. Each additional argument is
     * a string to check.</p>
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        if(args.length<1) {
            System.err.println("Usage: java DFAMatcher <file.dfa> <string1> [string2]...");
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
