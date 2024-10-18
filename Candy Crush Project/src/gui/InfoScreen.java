package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * gui.InfoScreen option displays a new screen option from the menu
 * Displays information on how to play the game
 * Inherits from abstract gui.Panel class
 * Reads from "info.txt"
 */
public class InfoScreen extends Panel{
    public static JFrame infoPanel;
    public JFrame menu;

    /**
     * @param name name
     * @param layout formatting
     */
    public InfoScreen(String name, LayoutManager layout, JFrame menu) {
        super(name, layout);
        this.menu = menu;
    }

    /**
     * Sets up gui.InfoScreen as gui.Panel extension
     * @throws FileNotFoundException
     */
    @Override
    public void setupPanel() throws FileNotFoundException {
        Scanner reader = new Scanner(new File("src/img/info.txt")); //reading from "info.txt"
        JTextArea infoText = new JTextArea();
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            infoText.append(line + "\n");
        }

        //formatting
        addTextArea(infoText,15,"Dialog",BorderLayout.NORTH);
        infoPanel = new JFrame("Info");
        infoPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        infoPanel.setSize(new Dimension(900,800));
        //adding exit button to return to menu
        addTextButton("exit","EXIT",BorderLayout.SOUTH);

        //returns to menu if "exit" button is clicked
        getButton("exit").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoPanel.setVisible(false);
                menu.setVisible(true);
            }
        });
        infoPanel.add(this.panel); //more formatting (hides menu)
        menu.setVisible(false);
        infoPanel.setVisible(true);

    }
}

