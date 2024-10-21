package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/** Class implements ActionListener and adds appropriate implementation to each button
 */
public class ButtonActionListener implements ActionListener {
    JButton b;
    JFileChooser fc;
    Display display;

    /** Constructor for class takes in the display and the button this instance was added to.
     * @param button - Jbutton that action listener was added to
     * @param d - Display instance
     */
    public ButtonActionListener(JButton button, Display d) {
        this.b = button;
        this.display = d;
        this.fc = new JFileChooser("data");
    }

    /** Method runs when action event occurs and calls the appropriate method for each button type
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(b.getText().equals(".dfa")) {
            dfaButtonPressed();
        }
        else if(b.getText().equals(".txt")) {
           txtButtonPressed();
        }
        else if(b.getText().equals("check")) {
            checkButtonPressed();
        }
    }

    /** Method runs when .dfa file loader button is pressed. Instantiates new file chooser and handles the selection process, updating the selected file and reads
     * the .dfa if the file. If no file was selected then the buttons remain disabled and "greyed out" until a file is selected.
     */
    public void dfaButtonPressed() {
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("DFA Files", "dfa");
        fc = new JFileChooser("data");
        fc.setFileFilter(fileFilter);
        int returnVal = fc.showOpenDialog(b);
        System.out.println(returnVal);
        if(returnVal == JFileChooser.CANCEL_OPTION) {fc.setSelectedFile(null);}
            try {
                display.setEnabled(true);
                display.enterText.requestFocusInWindow();
                display.dfaSelected = true;
                display.frame.setTitle(fc.getSelectedFile().getName());
                display.updateFile(fc.getSelectedFile());
                display.dfaReader.readDFA();
            } catch (NullPointerException npe) {
                display.frame.setTitle("Select File");
                display.updateChecked(null,null);
                display.setEnabled(false);
            }
    }
    /** Method runs when .txt file loader button is pressed. Instantiates new file chooser and handles the selection process, updating the selected file and reads
     * the .txt. If no file was selected then the buttons remain disabled and "greyed out" until a file is selected.
     */
    public void txtButtonPressed() {
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Text Files", "txt");
        fc = new JFileChooser("data");
        fc.setFileFilter(fileFilter);
        int returnVal = fc.showOpenDialog(b);
        System.out.println(returnVal);
        if(returnVal == JFileChooser.CANCEL_OPTION) {fc.setSelectedFile(null);}
            try {
                display.setEnabled(true);
                display.enterText.requestFocusInWindow();
                display.dfaSelected = false;
                display.frame.setTitle(fc.getSelectedFile().getName());
                display.updateFile(fc.getSelectedFile());
                display.textReader.readText();
            } catch (NullPointerException npe) {
                display.frame.setTitle("Select File");
                display.updateChecked(null,null);
                display.setEnabled(false);

            }

    }

    /** Method runs when check button or enter key is pressed. Method checks for which type of file is currently selected
     * and runs the appropriate reader class. It then resets the behavior of the enter text field.
     */
    public void checkButtonPressed() {
        if(display.dfaSelected) {
            display.dfaReader.readDFA();
            display.enterText.requestFocusInWindow();
            display.enterText.setText("");}
        else{
            display.textReader.readText();
            display.enterText.requestFocusInWindow();
            display.enterText.setText("");
        }
    }
}


