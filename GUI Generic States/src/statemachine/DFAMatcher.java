package statemachine;

import exceptions.InvalidStateMachineException;
import exceptions.UnmatchedException;

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
}
