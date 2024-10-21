package exceptions;

import java.io.File;

/** Used to alert about problems with state machines */
public class InvalidStateMachineException extends Exception {
    /** Line where the Exception condition was detected; -1 if line numbers are not available */
    private int linenum=-1;
    /** File instance containing the exception; null if the file name is not available (e.g. reading from
     *  an InputStream
     */
    private File file=null;
    /** Message text to help troubleshoot the issue */
    private String message=null;

    /** Create an instance with only the message set
     *
     * @param message text to help troubleshoot the issue
     */
    public InvalidStateMachineException(String message) {
        super(message);
        this.message = message;
    }

    /** Create an instance with line number and message set
     *
     * @param linenum which line from the beginning
     * @param message text to help troubleshoot the issue
     */
    public InvalidStateMachineException(int linenum, String message) {
        super("Stream line "+linenum+": "+message);
        this.linenum = linenum;
        this.message = message;
    }

    /** Create an instance with file, line number, and message st
     *
     * @param linenum which line from the beginning
     * @param f the file instance
     * @param message text to help troubleshoot the issue
     */
    public InvalidStateMachineException(int linenum, File f, String message) {
        super("In "+f.getName()+" at line "+linenum+": "+message);
        this.linenum = linenum;
        this.file = f;
        this.message = message;
    }

    /** Getter for the line number
     *
     * @return the line number
     */
    public int getStateMachineLinenum() { return linenum; }

    /** Getter for the file
     *
     * @return the file
     */
    public File getStateMachineFile() { return file; }

    /** Getter for the message text
     *
     * @return the message
     */
    public String getStateMachineMessage() {return message; }
}
