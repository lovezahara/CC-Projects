import java.util.List;

/** Interface for the representation of a State in a state machine
 * @param <T> The type of the symbol used for transitions
 */
public interface StateI<T> {
    /** Label text for this state, may be null
    *@return label state was constructed with
    */
    public String getLabel();


    /** True if this state is an accept state
    *@return true if an accept state, otherwise false
    */
    public boolean isAccept();


    /** All transitions leading out of this state
     * <p>Changes to the returned list (e.g. adding/removing items in the returned list) must
     * not change the available transitions from this state instance.</p>
     * @return state transitions headed out of this node, the list may be empty
     */
    public List<TransitionI<T>> getTransitions();


    /** Adds a new transition from this state
     *
     * @param s the symbol for the new transition
     * @param nxt the state to transition to on symbol s
     * @throws UnsupportedOperationException if this state has been frozen
     */
    public void addTransition(T s, StateI<T> nxt) throws UnsupportedOperationException;

    /** Makes the state immutable
     * <p>After freeze() is called no additional transitions may be added to the
     * state.</p>
     */
    public void freeze();
}
