package statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/** Interface for the representation of a statemachine.State in a state machine
 * @param <T> The type of the symbol used for transitions
 */
public class State<T> implements StateI<T> {
    String lbl;
    boolean accept;
    boolean frozen;
    Collection<TransitionI<T>> transitions;

    /** Constructor for statemachine.State Objects
     * @param lbl -label state was constructed with
     * @param accept -true if an accept state, otherwise false
     * variable transitions is an ArrayList that holds this instance of a statemachine.State Object's available transitions
     */
    public State(String lbl, boolean accept){
        this.lbl = lbl;
        this.accept = accept;
        transitions = new ArrayList<>();
    }
    /** Label text for this state, may be null
     *@return label state was constructed with
     */
    @Override
    public String getLabel() {
        return lbl;
    }
    /** True if this state is an accept state
     *@return true if an accept state, otherwise false
     */
    @Override
    public boolean isAccept() {
        return accept;
    }
    /** All transitions leading out of this state
     * <p>Changes to the returned list (e.g. adding/removing items in the returned list) must
     * not change the available transitions from this state instance.</p>
     * @return state transitions headed out of this node, the list may be empty
     */
    @Override
    public List<TransitionI<T>> getTransitions() {
        ArrayList<TransitionI<T>> copy = new ArrayList<>();
        copy.addAll(transitions);
        return copy;
    }
    /** Adds a new transition from this state
     *
     * @param s the symbol for the new transition
     * @param nxt the state to transition to on symbol s
     * @throws UnsupportedOperationException if this state has been frozen
     */
    @Override
    public void addTransition(T s, StateI<T> nxt) throws UnsupportedOperationException {
        if(frozen) throw new UnsupportedOperationException();
        else{
            TransitionI<T> transition = new Transition<>(s,nxt);
            transitions.add(transition);
        }
    }
    /** Makes the state immutable
     * <p>After freeze() is called no additional transitions may be added to the
     * state.</p>
     */
    @Override
    public void freeze() {
        frozen = true;
    }
}
