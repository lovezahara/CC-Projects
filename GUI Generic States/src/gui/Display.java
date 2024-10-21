package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/** Class representing the main window/display screen of the GUI.
 */
public class Display {
    JFrame frame;
    JPanel panel;
    GridBagConstraints c;
    JButton[] fileLoaders = new JButton[2];
    DFAFileReader dfaReader;
    TextFileReader textReader;
    File selected;
    JTextField lastChecked;
    JTextField enterText;
    boolean dfaSelected;

    /** Constructor for Display instance
     */
    public Display() {}

    /** Method creates a frame representing the display, then creates and adds all necessary
     *  components to the display. It then displays the screen.
     */
    public void setUpDisplay(){
        frame = new JFrame("Select File");
        panel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        selected = null;

        enterText = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 100;

        panel.add(enterText, c);

        lastChecked = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 100;
        lastChecked.setEditable(false);
        panel.add(lastChecked, c);

        JButton dfa = new JButton(".dfa");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        dfa.addActionListener(new ButtonActionListener(dfa,this));
        panel.add(dfa, c);
        fileLoaders[0] = dfa;

        JButton txt = new JButton(".txt");
        c.fill = GridBagConstraints.NONE;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        txt.addActionListener(new ButtonActionListener(txt,this));
        panel.add(txt, c);
        fileLoaders[1] = txt;

        JLabel enterLbl = new JLabel("enter text");
        c.gridx = 1;
        c.gridy = 2;
        panel.add(enterLbl, c);

        JLabel lastCheckedLbl = new JLabel("last checked");
        c.gridx = 1;
        c.gridy = 3;
        panel.add(lastCheckedLbl, c);

        JButton check = new JButton("check");
        c.gridx = 5;
        c.gridy = 4;
        c.weightx = 0.5;
        c.weighty = 0;
        check.addActionListener(new ButtonActionListener(check,this));
        panel.add(check, c);

        textReader = new TextFileReader(selected,this);
        dfaReader = new DFAFileReader(selected,this);
        JRootPane rootPane = frame.getRootPane();
        rootPane.setDefaultButton(check);
        setEnabled(false);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** Method controls the "greying out" and enabling/disabling of components of the display.
     * Since the file loading buttons are always enabled, the method does not alter these components.
     * @param enabled - boolean representing whether components are enabled
     */
    public void setEnabled(boolean enabled) {
        for(Component component: panel.getComponents()){
            if(!component.equals(fileLoaders[0]) && !component.equals(fileLoaders[1])){
                if(!enabled){
                    component.setEnabled(enabled);
                    component.setBackground(Color.lightGray);
                }
                else{
                    component.setEnabled(enabled);
                    component.setBackground(Color.WHITE);
                }
            }
        }
    }
    /** Method used to update the currently selected file, to ensure that the correct
     * file is always being read by the appropriate reader class
     * @param f - file to update selected file
     */
    public void updateFile(File f){
        if(dfaSelected){
        selected = f;
        dfaReader = new DFAFileReader(selected,this);
        }
        else{selected = f;
            textReader = new TextFileReader(selected,this);
        }
    }
    /** Method alters the checked text field to display the lasted checked word in the appropriate color
     * (red for not a match and green for matches)
     * @param s - string that represents the last checked word
     * @param c - color that is either green or red
     */
    public void updateChecked(String s,Color c){
        Font font = new Font("Nimbus",Font.BOLD,14);
        lastChecked.setFont(font);
        lastChecked.setText(s);
        lastChecked.setForeground(c);
    }
}


