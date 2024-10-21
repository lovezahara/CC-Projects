package statemachine;

/** Represents a transition in a state machine
 * @param <T> The type of the symbol used for transitions
 */
public interface TransitionI<T> {

    /** Gets the destination of this transition
    *@return state reached by taking this edge
    */
    public StateI<T> nextState();

    /** Gets the symbol that this transition can occur on
    *@return symbol for this edge
    */
    public T getSymbol();
}
