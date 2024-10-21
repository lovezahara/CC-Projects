package statemachine;

/** Represents a transition in a state machine
 * @param <T> The type of the symbol used for transitions
 */
public class Transition<T> implements TransitionI<T> {
    T symbol;
    StateI<T> nextState;
    /** Constructor for statemachine.Transition Objects
     * @param sym - represents the symbol for this statemachine.Transition instance
     * @param n -  represents the destination statemachine.State Object of this statemachine.Transition instance
     */
    public Transition(T sym, StateI<T> n){
        symbol = sym;
        nextState = n;
    }
    /** Gets the destination of this transition
     *@return state reached by taking this edge
     */
    @Override
    public StateI<T> nextState() {
        return nextState;
    }
    /** Gets the symbol that this transition can occur on
     *@return symbol for this edge
     */
    @Override
    public T getSymbol() {
        return symbol;
    }
}
