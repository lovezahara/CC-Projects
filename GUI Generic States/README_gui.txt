This codebase has gotten pretty large and some refactoring should be done. A GUI application for string matching has also been requested.

###############
# Do the refactoring
Refactor the code to have 4 packages:
exceptions   - contains the Exception types that might be thrown
statemachine - contains the the logic for state machines including generation
programs     - contains the WordSearcher
gui          - where the GUI classes will go

The "gui" package may contain whatever files you need; the specific number and names of files in this package will not be checked during testing. The other packages will be checked for extra/missing files. Classes in the "gui" package will be checked for JavaDoc however; all classes and methods must be documented.

Refactor DFAMatcher to no longer have a main method. The main method from DFAMatcher should be moved to a new class named StringMatcher in the programs package.

Get the new versions of GenericTests and ExceptionalTests from the Canvas page and verify that your refactored code works with these versions of the test files.

Add a class named GUICheck to the programs package. This will be the program that runs your GUI.

###############
# Make the GUI
The Canvas page has a picture of the GUI that was requested. Your GUI should be substantially similar (e.g. widgets in the same relative locations) to the sketch. Only one window should be in the application.

Initially, all widgets in the window should be grayed out and unusable except the buttons for loading files. The initial window title should be "Select file".

There are two buttons for loading files, '.dfa' and '.txt'. When the '.dfa' button is clicked, a JFileChooser should be presented to the user to select a file ending in '.dfa'. When the '.txt' button is clicked, a JFileChooser should be presnetd to the user to select a file ending in '.txt'. Files ending with different suffixes should not be selectable. After selecting the file, the file should be read with the correct StateMachineGenI; DFAGen for .dfa and DictionaryGen for .txt. If there are problems with the file, an error message should pop-up to let the user know about the problem. For DFAGen, the error message must be informative, including the filename, line number, and useful text about the problem (e.g. the message text from the last assignment). For DictionaryGen, the error message must include the filename and say that the file is incorrectly formatted (no specifics are needed). For files that generate valid DFAs, the title bar should change to reflect the name of file most recently loaded. For files that don't generate valid DFAs, all widgets except for the buttons to load files should be grayed out and unusable; the window title should also be changed to "Select file".

The field labelled "enter text" is where the user will type the string they would like to have matched using the DFA. Typing enter in this text field or clicking the check button should cause the string to be checked. The text should be copied into the "last checked" field as part of the check and the text enter field should be reset to blank with the keyboard focus in that widget (the user should be able to type a string, press enter, and type another string without moving the cursor or using the tab key). Text that matches should be bold and green in the last checked field. Text that does not match should be bold and red in the last checked field.

The user should not be able to edit the text in last checked. When the JFileChooser opens for selecting '.dfa' or '.txt' files, it should default to openning the "data" directory for selecting which file to generate the DFA from.
