package gui;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**This is an abstract class that is the parent class to the gui.Menu, gui.ScoreScreen, gui.InfoScreen and gui.WinScreen classes.
 * This class includes one abstract method and three regular methods that make up the bulk of the
 * functionality of the classes that inherent from Product.
 */
public abstract class Panel {
    public  String name;
    public  ArrayList<JButton> buttons; // keeps track of JButtons added to JPanel panel for getter method
    public  JPanel panel; // represents the actual JPanel object associated with the gui.Panel object
    public  LayoutManager layout;


    /** This is the base constructor for gui.Panel objects.
     * @param name - String that represents name of gui.Panel object
     * @param layout - LayoutManager used to set the layout of JPanel panel
     */
    public Panel(String name, LayoutManager layout) {
        this.layout = layout;
        this.buttons = new ArrayList<>();
        this.name = name;
        panel = new JPanel(layout);
        panel.setBackground(Color.black);
    }


    /** This abstract method is only partially implemented and allows each gui.Panel object to have a unique method for
     * setting up their JPanel and JFrame
     * @throws FileNotFoundException - implemented for gui.Panel objects that make use of Scanner or PrintStream in method
     */
    public abstract void setupPanel() throws FileNotFoundException;

    /** This getter method takes in a string as a parameter that represents a button name and searches through the buttons ArrayList
     * returning the JButton with a matching name.
     * @param buttonName - String that represents desired JButtons name
     * @return - returns JButton with associated button name
     */
    public JButton getButton(String buttonName){
        JButton returnButton = new JButton(buttonName);
        for (JButton b : this.buttons){
            if(b.getName().equals(buttonName)){
                returnButton = b;
            }
        }
        return returnButton;
    }
    /** Method creates a new JButton and sets the name, text and layout of the button according to the given parameters.
     * Method also create a new Font and sets the font, background color and opacity of the JButton.
     * It then adds that button to the panel and the ArrayList buttons.
     * @param buttonName - String that represents JButtons name
     * @param buttonText - String that represents JButtons displayed text
     * @param layout - String that will be used to set the location of the button on the JPanel
     */
    public void addTextButton(String buttonName, String buttonText, String layout){
        JButton button = new JButton(buttonText);
        button.setName(buttonName);
        Font font = new Font("Serif", Font.BOLD, 20);
        button.setFont(font);
        button.setBackground(Color.BLACK);

        panel.add(button,layout);
        this.buttons.add(button);
    }
    // creates a new JButton and ImageIcon with file name provided in parameter. Sets the icon of the button to the ImageIcon and button name to buttonName
    //it then adds that button to the panel and the ArrayList buttons.
    /**Method creates a new JButton and sets the name and image file name of the button according to the given parameters.
     * Method also create a new ImageIcon and uses it to set the JButtons icon. This method also sets background color of the JButton.
     * It then adds that button to the panel and the ArrayList buttons.
     * @param buttonName -String that represents JButtons name
     * @param imageFileName -String that represents ImageIcons file name that will be used to instantiate JButton
     */
    public void addIconButton(String buttonName, String imageFileName){
        ImageIcon icon = new ImageIcon(imageFileName);
        JButton button = new JButton(icon);
        button.setBackground(Color.BLACK);
        button.setName(buttonName);
        panel.add(button);
        this.buttons.add(button);

    }
    // takes in a JTextArea as a parameter and adds it to the panel
    /** Method takes in a JTextArea as a parameter and creates and sets a new Font with its font style/type and size according to the parameters.
     * It also sets the location on the panel according to the parameter layout. It sets the background, foreground,
     * editable status. It then adds the JTextArea to the JPanel associated with gui.Panel.
     * @param textArea - JTextArea object that will be modified and added to gui.Panel object
     * @param fontSize - int that represents size of font
     * @param fontType - String that represents style/type of font
     * @param layout - String that will be used to set the location of the button on the JPanel
     */
    public void addTextArea(JTextArea textArea, int fontSize, String fontType, String layout){
        Font font = new Font(fontType, Font.BOLD, fontSize);
        textArea.setFont(font);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        panel.add(textArea,layout);

    }
}
