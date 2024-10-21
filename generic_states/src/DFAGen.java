import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** Class that instantiates state machines from files or byte streams
 */
public class DFAGen implements StateMachineGenI<Character> {
    /*Contains the State Objects with the state labels as the key, unless the State represents the start state in which
    case the key will be " _start_ " to indicate such */
     HashMap<String,StateI<Character>> states;
     StateI<Character> startState;

    /** Instantiates a state machine from the contents of a file
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param f the file containing the machine description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws FileNotFoundException when the file cannot be read
     * @throws IOException when the underlying libraries throw IOException
     */
    @Override
    public StateI<Character> read(File f) throws InvalidStateMachineException, FileNotFoundException, IOException {
        try{
        return read(new FileInputStream(f)); }
        catch (InvalidStateMachineException e){
           String msg = e.getStateMachineMessage();
           int ln = e.getStateMachineLinenum();
           throw new InvalidStateMachineException(ln,f, msg);
        }

    }
    /** Instantiates a state machine from the contents of a stream
     * <p>The stream is read from until the end of the stream is reached.</p>
     * <p>All states in the state machine should be frozen when this function returns.</p>
     *
     * @param in the byte stream containing the description
     * @return The start state of the machine
     * @throws InvalidStateMachineException when there are errors in the description
     * @throws IOException when the underlying libraries throw IOException
     */
    @Override
    public StateI<Character> read(InputStream in) throws InvalidStateMachineException, IOException {
        // reading and parsing information into ArrayList <String{}> lines
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] tokens;
        LinkedHashMap<String[],Integer> lines = new LinkedHashMap<>();

        int linenum = 0;
        while (line != null) {
            linenum ++;
            line = line.stripLeading();
            if(!line.isBlank() && !line.startsWith("#")) {
                tokens = line.split("\\s+|,");
                lines.put(tokens,linenum);
            }
            line = reader.readLine();
        }
        // declaring states inside method so that it resets each time method is called
        states = new HashMap<>();
        startState = null;

        // iterating through String[]'s in ArrayList lines
        for (String[] arr: lines.keySet()) {
            int curLine = lines.get(arr);
            //Checking for parsed State info.
            if (arr[0].equals("s")) {
                if (arr.length < 2) {
                    throw new InvalidStateMachineException(curLine,"State line missing the state label");
                }
                checkAttributes(arr, curLine);
            }
            //Checking for parsed Transition info.
            else if (arr[0].equals("t")) {
                makeTransitions(arr, curLine);
            }
            else {
                throw new InvalidStateMachineException(curLine,"Line is not blank, a state, a transition, or a comment");
            }
            if(startState == null && curLine == linenum) {throw new InvalidStateMachineException(curLine,"No start state found before EOF");}

        }
        // loops through and freezes states
        for(StateI<Character> s : states.values()) {
            s.freeze();
        }
       // if(startState == null) {throw new InvalidStateMachineException("No start state found before EOF");}
        return states.get(startState.getLabel());
    }

    /**Method takes in a String, a boolean, and an int. Uses parameters to creates a State Object instance and adds it to the
     * "states" HashMap with the State's label as the key.
     * @param stateLbl - String representing the label of the State Object instantiated in the method
     * @param acc - boolean representing whether the state is an accept state
     * @param ln - represents line number of parsed state information, used in case of exception
     * @throws InvalidStateMachineException - exception thrown when state with label passed in as a parameter already exists
     */
    private void setState(String stateLbl, boolean acc, int ln) throws InvalidStateMachineException {
        if(states.containsKey(stateLbl)) { throw new InvalidStateMachineException(ln,"State with label "+stateLbl+" already exists");}
        StateI<Character> s = new State<>(stateLbl,acc);
        states.put(stateLbl,s);
    }

    /**Method takes in a String[] that represents the parsed State information read from the input stream/file.
     * Checks if array contains attributes and loops the attributes searching for keywords "accept" and "start", then
     * initializes the state appropriately.
     * Checks for potential invalid information and throws an exception if any are detected.
     * @param arr - String[] representing parsed State information
     * @param ln - represents line number of parsed state information, used in case of exception
     * @throws InvalidStateMachineException - exception thrown when state information contains too many fields or
     * contains unsupported attribute types
     */
    private void checkAttributes(String[] arr, int ln) throws InvalidStateMachineException {
        boolean accept = false;
        boolean isStart = false;

        if(arr.length> 4){throw new InvalidStateMachineException(ln, "Stata line contains too many fields");}
        else if(arr.length>2) {
            for (int i = 2; i < arr.length; i++) {
                if (arr[i].equals("accept")) {
                    accept = true;
                }
                if (arr[i].equals("start")) {
                    isStart = true;
                }
                if(!arr[i].equals("accept") && !arr[i].equals("start")){throw new InvalidStateMachineException(ln,"Unsupported attribute "+arr[i]);}
            }
            if (isStart) {checkStart(accept, arr[1],ln);}
        }
        if(!isStart) {
        setState(arr[1],accept,ln);}
    }

    /**  Method takes in a boolean and a String. It checks whether the "states" HashMap already contains the start State
     * and if not it creates a new State Object that represent the start State. Then adds it to "states" , unless the start
     * State is already in "states" in which case it throws an exception.
     * @param acc - boolean representing whether state is an accept state
     * @param stateLbl - String representing the label of the State Object instantiated in the method
     * @param ln - represents line number of parsed state information, used in case of exception
     * @throws InvalidStateMachineException - exception thrown if start state already exists
     */
    private void checkStart(boolean acc, String stateLbl,int ln) throws InvalidStateMachineException {
        if(!states.containsValue(startState)) {
            startState = new State<>(stateLbl,acc);
            states.put(stateLbl,startState);
        }
        else{throw new InvalidStateMachineException(ln,"Duplicate start state");}
    }

    /** Method takes in a String[] that represents the parsed Transition information read from the input stream/file.
     * It performs a series of checks to ensure that the transition info. is valid and will throw an appropriate exception if not.
     * If the transition passes the checks it is instantiated and the states the transition is added to the initial state.
     * @param arr - String[] representing parsed Transition information
     * @param ln - current line number
     * @throws InvalidStateMachineException - exception thrown if transition info. is invalid
     */
//    private void makeTransitions(StateI<Character> s, StateI<Character> dest,Character c,int ln) throws InvalidStateMachineException {
//
//        StateI<Character> initState = states.get(arr[1]);
//        StateI<Character> destState = states.get(arr[2]);
//        checkTransitions(initState,destState,c,linenum);
//
//    }
    private void makeTransitions(String[] arr, int ln) throws InvalidStateMachineException {
        boolean existsAlready = true;

        if(arr.length < 4){throw new InvalidStateMachineException(ln,"Insufficient fields for transition line");}
        else if(arr.length > 4){throw new InvalidStateMachineException(ln,"Too many fields for transition line");}
        else if(!states.containsKey(arr[1])){throw new InvalidStateMachineException(ln,"Source ("+arr[1]+") for the transition is unknown");}
        else if(!states.containsKey(arr[2])){throw new InvalidStateMachineException(ln, "Sink ("+arr[2]+") for the transition is unknown");}
        else if(arr[3].length() > 1){throw new InvalidStateMachineException(ln,"Transition symbol is not a single character");}

        else {
            Character c = arr[3].charAt(0);
            StateI<Character> initState = states.get(arr[1]);
            StateI<Character> destState = states.get(arr[2]);
            while(existsAlready) {
                for (TransitionI<Character> t : initState.getTransitions()) {
                    if (t.getSymbol().equals(c)) {
                        throw new InvalidStateMachineException(ln, "Transition from " + initState.getLabel() + " on " + c + " already exists");
                    }
                }
                existsAlready = false;
            }
            initState.addTransition(c,destState);
        }
    }
}

