import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Generates DFAs for accepting/rejecting character sequences based on a dictionary file
 * <p>Input file should contain a list of words</p>
 */
public class DictionaryGen implements StateMachineGenI<Character> {
    TraversalHelpers<Character> helpers;

    /** Default constructor */
    public DictionaryGen() {
        helpers = new TraversalHelpers<>();
    }

    /**
     * Instantiates a state machine from the contents of a file
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param f the file containing the machine description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws FileNotFoundException        when the file cannot be read
     * @throws IOException                  when the underlying libraries throw IOException
     */
    @Override
    public StateI<Character> read(File f) throws InvalidStateMachineException, FileNotFoundException, IOException {
        InputStream in = new FileInputStream(f);
        try {
            return read(in);
        } catch (InvalidStateMachineException e) {
            throw new InvalidStateMachineException(e.getStateMachineLinenum(), f, e.getStateMachineMessage());
        }
    }

    /**
     * Instantiates a state machine from the contents of a stream
     * <p>The stream is read from until the end of the stream is reached.</p>
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param in the byte stream containing the description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws IOException                  when the underlying libraries throw IOException
     */
    @Override
    public StateI<Character> read(InputStream in) throws InvalidStateMachineException, IOException {
        // Read in all the lines... short words will need to be added to the state machine first
        // so that the associated accept states can be set when the state is constructed
        ArrayList<String> words = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        while(line != null) {
            line = line.strip();
            if(!line.isBlank() && !line.startsWith("#")) {
                words.add(line);
            }
            line = br.readLine();
        }
        words.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length()-s2.length();
            }
        });
        // words should be sorted in ascending word length

        StateI<Character> start = new State<>(null, false);
        for(String w:words) {
            StateI<Character> cur = start;
            // process the intermediate characters
            for(int i=0; i<w.length()-1; i++) {
                StateI<Character> nxt = helpers.deterministicEqNextFor(w.charAt(i), cur);
                // if the symbol doesn't go to a next state, make one to transition to
                if(nxt==null) {
                    StateI<Character> n = new State<>(null, false);
                    cur.addTransition(w.charAt(i), n);
                    cur = n;
                } else {
                    cur = nxt;
                }
            }
            // process the last character, which should reach an accept state
            StateI<Character> nxt = helpers.deterministicEqNextFor(w.charAt(w.length()-1), cur);
            if(nxt!=null) {
                // If there is a transition, it had better be an accept state since this must be a duplicate word
                // Due to the sorting this should be impossible!
                if(!nxt.isAccept()) {
                    throw new InvalidStateMachineException("This shouldn't happen... "+w+" can't be added due to earlier word added.");
                }
            } else {
                StateI<Character> n = new State<>(null, true);
                cur.addTransition(w.charAt(w.length()-1), n);
            }
        }
        return start;
    }
}
