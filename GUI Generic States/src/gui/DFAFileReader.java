package gui;

import exceptions.InvalidStateMachineException;
import statemachine.DFAGen;
import statemachine.DFAMatcher;
import statemachine.StateI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/** Class evaluates selected .dfa files with DFAGen instance and updates the GUI screen according to match results of
 * the DFAMatcher instance
 */
public class DFAFileReader {
    File file;
    DFAMatcher matcher;
    Display display;
    boolean match;

    /** Constructor takes in the currently selected .dfa file and the Display instance
     * @param f - selected file
     * @param display - Display instance
     */
    public DFAFileReader( File f, Display display) {
        this.file = f;
        this.display = display;
    }

    /** Method creates new DFAGen and DFAMatched instances and uses them to read the selected file, instantiate a new state machine, and check if the entered
     * text is a match. If the file is incorrectly structured an error message will appear and a new file will need to
     * be selected to continue.
     */
    public void readDFA(){
        try{
            String text = display.enterText.getText();
            display.setEnabled(true);
            DFAGen gen = new DFAGen();
            StateI<Character> start = gen.read(file);
            matcher = new DFAMatcher(start);
            match = matcher.doesMatch(text);
            match();
        }
        catch(InvalidStateMachineException ex){
            JFrame errorFrame = new JFrame("Error");
            JPanel panel = new JPanel();
            JTextArea label = new JTextArea("File Name: " + file.getName() +"\n"+ "Line Number: "+ex.getStateMachineLinenum()+"\n" + ex.getStateMachineMessage());
            label.setEditable(false);
            display.frame.setTitle("Select File");
            display.updateChecked(null,null);
            display.setEnabled(false);
            panel.add(label);
            errorFrame.add(panel);
            errorFrame.pack();
            errorFrame.setLocation(400,350);
            errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            errorFrame.setVisible(true);}

        catch (IOException e1) {throw new RuntimeException(e1);}
    }

    /** Method checks the value of match and updates the last checked field on the display to reflect the results appropriately.
     */
    public void match(){
        if(this.match) {
            display.updateChecked(display.enterText.getText(),Color.GREEN);
        }
        else if(!this.match) {
            display.updateChecked(display.enterText.getText(),Color.RED);
        }
    }
}
