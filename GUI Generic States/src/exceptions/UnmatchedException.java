package exceptions;

/**
 * Exception to indicate that there was no valid transition during matching
 */
public class UnmatchedException extends Exception {
    /** Default constructor */
    public UnmatchedException() {
        super();
    }
}
