import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/** Interface for classes that instantiate state machines from files or byte streams
 * @param <T> The type of the symbol used for transitions
 */
public interface StateMachineGenI<T> {

    /** Instantiates a state machine from the contents of a file
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param f the file containing the machine description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws FileNotFoundException when the file cannot be read
     * @throws IOException when the underlying libraries throw IOException
     */
    public StateI<T> read(File f) throws InvalidStateMachineException, FileNotFoundException, IOException;

    /** Instantiates a state machine from the contents of a stream
     * <p>The stream is read from until the end of the stream is reached.</p>
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param in the byte stream containing the description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws IOException when the underlying libraries throw IOException
     */
    public StateI<T> read(InputStream in) throws InvalidStateMachineException, IOException;

}
