package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 *
 */
public class ScoreScreen extends Panel{
    public JFrame scorePanel;
    public JFrame menu;

    public ScoreScreen(String name, LayoutManager layout,JFrame menu) {
        super(name, layout);
        this.menu = menu;
    }
    /**
     *
     * @throws FileNotFoundException
     */
    @Override
    public void setupPanel() throws FileNotFoundException {
        Scanner reader = new Scanner(new File("src/winCount.txt"));
        JTextArea scoreText = new JTextArea("\n CURRENT WINS : " + reader.nextLine());
        JTextArea scoreHeader = new JTextArea("==========SCORE===========");
        addTextArea(scoreHeader,45,"Dialog",BorderLayout.NORTH);
        addTextArea(scoreText,35,"Dialog",BorderLayout.CENTER);


        scorePanel = new JFrame("Score");

        scorePanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scorePanel.setSize(new Dimension(900,300));

        addTextButton("exit","EXIT", BorderLayout.SOUTH);

        getButton("exit").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scorePanel.setVisible(false);
                menu.setVisible(true);
            }
        });
        scorePanel.add(this.panel);
        menu.setVisible(false);
        scorePanel.setVisible(true);

    }
}
