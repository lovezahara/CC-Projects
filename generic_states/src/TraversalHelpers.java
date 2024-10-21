import java.util.*;

/** A class containing functions to help navigate state machine transitions
 * @param <T> The type of the symbol used for transitions
 */
public class TraversalHelpers<T> {
    /** Default constructor */
    public TraversalHelpers() {}

    /** Helper function for finding the state to transition to
     *
     * @param s symbol read
     * @param cur current state
     * @return if a matching transition exists the state reached by taking the transition, otherwise null
     */
    public StateI<T> deterministicEqNextFor(T s, StateI<T> cur) {
        List<TransitionI<T>> txs = cur.getTransitions();
        for(TransitionI<T> t:txs) {
            if(t.getSymbol().equals(s)) {
                return t.nextState();
            }
        }
        return null;
    }

}
