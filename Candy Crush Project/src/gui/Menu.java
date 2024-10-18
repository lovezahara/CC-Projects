package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
/**This method inherits from gui.Panel and has one unique method assignAction that is used to assign action listeners to
 * JButton Objects in gui.Menu and implements the abstract method setupPanel() from its parent class to initialize a new JFrame
 * and the frame's and Panels functionality.
 */
public class Menu extends Panel {
    JFrame menu = new JFrame("");
    MainGame mainGame = new MainGame(this);
    /**
     *
     * @param name name
     * @param layout formatting
     */
    public Menu(String name, LayoutManager layout) {
        super(name, layout);
    }
    /**
     * Sets up menu from abstract panel class
     */
    @Override
    public void setupPanel(){

        //formats gui.Menu Screen
        menu = new JFrame("Menu");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setSize(new Dimension(600, 900));

        this.addIconButton("menuIcon","src/img/menuIcon.png");
        this.addIconButton("infoIcon","src/img/Info.png");
        this.addIconButton("playIcon","src/img/Play.png");
        this.addIconButton("scoreIcon","src/img/Score.png");

        for(JButton b: this.buttons){
            assignAction(b);
        }
        //set to visible when called
        menu.add(this.panel);
        menu.setVisible(true);

    }
    /**
     * Makes menu interactable by user
     * Three options displayed on menu => PLAY, INFO, and SCORE
     * @param button allows user interaction by adding button
     */
    public void assignAction(JButton button)  {
        button.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                //if user clicks play => open game screen and close menu
                if (button.getName().equals("playIcon")) { // runs main game
                    mainGame.gameSetup();
                    menu.setVisible(false);
                }
                else if (button.getName().equals("infoIcon")) {
                    //if user clicks info => open info screen
                    InfoScreen infoPanel = new InfoScreen("info", new FlowLayout(),menu);
                    try {
                        infoPanel.setupPanel(); // setting up menu// runs info panel
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if (button.getName().equals("scoreIcon")) { // prints score
                    //if user clicks score => open score screen
                    ScoreScreen scorePanel = new ScoreScreen("score", new BorderLayout(),menu);
                    try {
                        scorePanel.setupPanel();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
