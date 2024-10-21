package gui;

import exceptions.InvalidStateMachineException;
import statemachine.DFAMatcher;
import statemachine.DictionaryGen;
import statemachine.StateI;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Class evaluates selected .txt files with DFAGen instance and updates the GUI screen according to match results of
 * the DFAMatcher instance
 */
public class TextFileReader {
    File file;
    Display display;
    DFAMatcher matcher;
    boolean match;

    /** Constructor takes in the currently selected .txt file and the Display instance
     * @param f - selected file
     * @param display - Display instance
     */
    public TextFileReader(File f, Display display) {
        this.file = f;
        this.display = display;
    }

    /** Method creates new DictionaryGen and DFAMatched instances and uses them to read the selected file, instantiate a new state machine, and check if the entered
     * text is a match. If the file is incorrectly structured an error message will appear and a new file will need to
     * be selected to continue.
     */
    public void readText(){
        try{
            String text = display.enterText.getText();
            display.setEnabled(true);
            DictionaryGen dg = new DictionaryGen();
            StateI<Character> start = dg.read(file);
            matcher = new DFAMatcher(start);
            match = matcher.doesMatch(text);
            match();
        }
        catch (InvalidStateMachineException e) {
            JFrame errorFrame = new JFrame("Error");
            JPanel panel = new JPanel();
            JTextArea label = new JTextArea("File Name: " + file.getName() +"\n"+ "File is incorrectly formatted");
            label.setEditable(false);
            display.frame.setTitle("Select File");
            display.setEnabled(false);
            panel.add(label);
            errorFrame.add(panel);
            errorFrame.pack();
            errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            errorFrame.setLocation(400,350);
            errorFrame.setVisible(true);}

        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /** Method checks the value of match and updates the last checked field on the display to reflect the results appropriately.
     */
    public void match(){
        if(this.match) {
            display.updateChecked(display.enterText.getText(), Color.GREEN);
        }
        else if(!this.match) {
            display.updateChecked(display.enterText.getText(),Color.RED);
        }
    }
}
