...it's the next assignment... You need to check for and throw exceptions with specific messages in your statemachine.DFAGen read methods. Checking the messages text is a little gross; usually there will be a different type of exception for each condition which makes it easier to write catch statements that recover from the error. Templates for the message that must be used for each case each are given below.

The exceptions.InvalidStateMachineException has 3 constructors. For the read(InputStream) you won't have access to the file name but you can know how many lines have been read from the InputStream; use the constructor that takes 2 arguments. For the read(File) you should use the constructor that takes 3 arguments. The line number should be the first line where the defect can be detected. If a file has multiple defects, the first detectable within the file should set the line number and message text.

################
# Exception Templates
################
Line has a known type
    "Line is not blank, a state, a transition, or a comment"

A line representing a state has no state name
    "statemachine.State line missing the state label"

A line representing a state has information after the attributes
    "statemachine.State line contains too many fields"

A line representing a state has unknown attributes (any attributes in addition to "accept" and "start")
    "Unsupported attribute "+unknownAttribute

There is more than one state declared to be the start state
    "Duplicate start state"

Multiple states have the same name
    "statemachine.State with label "+stateLabel+" already exists"

A line representing a transition that has too many parts
    "Too many fields for transition line"

A line representing a transition that has too few parts
    "Insufficient fields for transition line"

A line representing a transition where the symbol isn't a single character
    "statemachine.Transition symbol is not a single character"

A line representing a transition involving a state not defined on an earlier line
    "Source ("+sourceLabel+") for the transition is unknown."
    "Sink ("+destinationLabel+") for the transition is unknown"

A line representing a transition that would cause the DFA to be nondeterministic
    "statemachine.Transition from "+sourceLabel+" on "+c+" already exists"

No start state was given
    "No start state found before EOF"


#################
# Files
#################
README.txt - this file
README_dfa.txt - Discussion of the .dfa file format

test/GenericTests.java - A suite of unit tests to verify functionality
test/ExceptionalTests.java - A suite of unit tests to verify exception messages
