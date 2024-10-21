In this assignment you will write JAVA code to model state machines. State machines will come up again in CP409, computational theory, and are involved in how compilers make sense of source code. The theory around state machines is pretty neat stuff but out of scope for CP222. State machines involve "states" which represent where the machine is in a computation and "transitions" which represent which state to progress to on a specific input... basically the state machines looks like a graph.

You will need to implement a State class and a Transition class that conform to the StateI and TransitionI interfaces, respectively. Your State and Transition classes will be reused for several programs, not all of which will work on Characters. Your implementations must use JAVA Generics to allow State and Transition instances to be specialized to the correct type of symbol for the program.

The State class must have a constructor with the following signature:
    public State(String lbl, boolean accept)

The Transition class must have a constructor with the following signature:
    public Transition(T sym, StateI<T> n)

To practice working with file formats a bit more, you will also be writing the DFAGen class, which must implement StateMachineGenI. DFAGen reads files and streams conforming to the format described in README_dfa.txt. For the initial assignment, only files/streams with syntactically correct contents will be used for testing. In the next assignment, you'll need to detect a whole bunch of specific errors and throw an InvalidStateMachineException with an informative message. It is strongly recommended you add comments for or add the throw statements as you consider what could go wrong while initially writing the code. It is also worth noting that there is a way to write the code such that read(File) uses read(InputStream) to avoid duplicating a lot of program code/logic.

#################
# Included executables
#################
DFAMatcher - This program uses your DFAGen to read in a DFA and check if several strings are matched by that DFA
WordSearcher - This program represents a list of words as a DFA and checks if input strings are accepted by the DFA

#################
# Thinking ahead for the next assignment...
#################
Exception conditions will not be checked for until the next assignment, but you might want to think about how you'll detect these problems as you work on this assignment. Adding error checking/securty after development is almost always much harder than including it during the first write of the code. A short description of the error conditions with expected message text follows.

Line has a known type
    "Line is not blank, a state, a transition, or a comment"
A line representing a state has no state name
    "State line missing the state label"
A line representing a state has information after the attributes
    "Stata line contains too many fields"
A line representing a state has unknown attributes (any attributes in addition to "accept" and "start")
    "Unsupported attribute "+unknownAttribute
There is more than one state declared to be the start state
    "Duplicate start state"
Multiple states have the same name
    "State with label "+stateLabel+" already exists"
A line representing a transition that has too many parts
    "Too many fields for transition line"
A line representing a transition that has too few parts
    "Insufficient fields for transition line"
A line representing a transition where the symbol isn't a single character
    "Transition symbol is not a single character"
A line representing a transition involving a state not defined on an earlier line
    "Source ("+sourceLabel+") for the transition is unknown."
    "Sink ("+destinationLabel+") for the transition is unknown"
 A line representing a transition that would cause the DFA to be nondeterministic
    "Transition from "+sourceLabel+" on "+c+" already exists"
No start state was given
    "No start state found before EOF"


#################
# Files
#################
README.txt     - this file
README_dfa.txt - Discussion of the .dfa file format
README_exceptional.txt - Discussion for the exceptional work assignment
data/*        - Data files for testing different StateMachineGenI implementations
src/TransitionI.java       - Interface for Transition implementations
src/StateI.java            - Interface for State implementations
src/StateMachineGenI.java  - Interface for things that generate state machines from files
src/DFAMatcher.java        - Detemines if a string is matched by a DFA
src/DictionaryGen.java     - Generates a DFA that recognizes words from a word file
src/WordSearcher.java      - Program using WordSearcher
src/TraversalHelpers.java  - Has some methods to help with state machine traversal
src/UnmatchedException.java - Signals no matching transition for a symbol
src/InvalidStateMachineException.java - Signals there's a problem with the machine
test/GenericTests.java - A suite of unit tests to verify functionality
test/ExceptionalTests.java - A suite of unit tests to verify exception messages
src/DFAGen.java - things that generate state machines from files or input stream
src/State.java - State implementations
src/Transition.java - Transition implementations
