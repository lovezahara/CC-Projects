Documentation regarding the .dfa file format

.dfa files are used to describe deterministic finite automata for string matching. This allows the set of strings a program matches to be changed without requiring the program to be modified.

The file format is line based. Each line represents one thing:
    blank - a line containing only whitespace characters
    comment - a line where the first non-whitespace character is '#'
    state - a line where the first non-whitespace character is 's'
            following 's', separated by whitespace, must be a state label/name
            following the state label, separated by whitespace, may be attributes
                Attributes are given separated by commas and whitespace may not occur in the attribute list
     transition - a line where the first non-whitespace character is 't'
                  following 't', separated by whitespace, must be the source state label
                  following the source state label, separated by whitespace, must be the destination state label
                  following the destination state label, separated by whitespace, must be the character for the transition.

Sample .dfa files can be found in the data directory and will have names like 'goodSampleXX.dfa'. Negative sample .dfa files are also in the data directory and will have names like 'badSampleXX.dfa'.
