package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;


/**
 * Sets up gui.WinScreen using abstract gui.Panel class
 * after winning, congratulatory message and options to see score or exit are displayed
 */
public class WinScreen extends Panel {
    public JFrame winFrame;
    public JFrame menu;
    public MainGame mainGame;

    /**
     * makes gui.WinScreen callable
     * @param name name
     * @param layout formatting
     */
    public WinScreen(String name, LayoutManager layout,JFrame menu,MainGame main) {
        super(name, layout);
        this.mainGame = main;
        this.menu = menu;
    }
    /**
     * sets up gui.WinScreen using abstract gui.Panel class
     * formatting, formatting, formatting
     * @throws FileNotFoundException
     */
    @Override
    public void setupPanel() throws FileNotFoundException {
        winFrame = new JFrame("WinFrame");
        setupButtons();

        winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winFrame.setSize(new Dimension(600, 900));

        //closes menu and game, only win frame visible
        winFrame.add(this.panel);
        winFrame.setVisible(true);
        menu.setVisible(false);
        mainGame.gameFrame.setVisible(false);
    }
    /**
     * Allows user to click seeScore and playAgain buttons
     * displays winHeader
     */
    public void setupButtons() {
        this.addIconButton("winHeader","src/img/winHeader.png");
        this.addIconButton("seeScore","src/img/seeScore.png");
        this.addIconButton("playAgain","src/img/playAgain.png");

        //exits to score screen and displays current amount of wins closes winFrame
        this.getButton("seeScore").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                winFrame.setVisible(false);
                mainGame.gameFrame.setVisible(false);
                ScoreScreen scorePanel = new ScoreScreen("score", new BorderLayout(),menu);
                try {
                    scorePanel.setupPanel();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //reenters game with new board, closes winFrame
        this.getButton("playAgain").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                winFrame.setVisible(false);
                mainGame.gameSetup();
            }
        });
    }
}

