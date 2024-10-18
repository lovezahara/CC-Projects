package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * Game mechanics activate when "play now" is pressed on menu screen
 * Candy behavior, possible user interactions, "exit" button,
 * and formatting are described in the javadocs for the methods
 * below
 */
public class MainGame {

    int WINS = 0;
    int SCORE = 0;
    JButton[][] grid;
    Menu menuPanel;
    JFrame gameFrame;

    public MainGame(Menu menuPanel){
        this.menuPanel = menuPanel;
        this.grid = generateRandom(6, 4);
        this.gameFrame = new JFrame();
    }

    /**
     * Sets up game
     * Sets SCORE = 0
     * Formats play screen
     */
    public void gameSetup() {
        JPanel board = new JPanel(new GridLayout(6, 4));
        JButton scoreButton = new JButton("SCORE : 0");
        JPanel scoreBoard = new JPanel(new FlowLayout());
        JButton exit = new JButton("Exit");

        exit.addActionListener(new ActionListener() {


            /**
             * Returns to menu, closes game screen
             * @param e user action (used for exit)
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                grid = generateRandom(6, 4);
                board.removeAll();
                scoreBoard.removeAll();
                gameFrame.setVisible(false);
                menuPanel.menu.setVisible(true);
            }
        });
        SCORE = 0;
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(new Dimension(600, 900));
        setupGUI(board,scoreBoard,exit,scoreButton);
        gameFrame.add(scoreBoard, BorderLayout.NORTH);
        gameFrame.add(board, BorderLayout.CENTER);
        gameFrame.setVisible(true);


        startGame(board,scoreBoard,scoreButton); //initializes gameplay, calls on game mechanic methods
    }


    /**
     * Graphics formatting with score, goal, and exit displayed on top of screen
     * Adds buttons to grid
     */
    public void setupGUI(JPanel board,JPanel scoreBoard, JButton exit, JButton scoreButton) {
        //formats entire screen with labels on top
        JButton goalButton = new JButton("GOAL : 5");

        board.setForeground(Color.BLACK);
        scoreBoard.setBackground(Color.BLACK);
        scoreBoard.add(goalButton);
        scoreBoard.add(scoreButton);
        scoreBoard.add(exit, BorderLayout.EAST);

        //formats grid with background color and adds buttons to each cell
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].setOpaque(true);


                JButton button = grid[i][j];
                board.add(button);
            }
        }
    }


    /**
     * Swaps two selected candies
     * @param matches Arraylist of button matches
     */
    public static void swapIcons( ArrayList<JButton> matches) {
        Icon ph = matches.get(0).getIcon();
        matches.get(0).setIcon(matches.get(1).getIcon());
        matches.get(1).setIcon(ph);
    }


    /**
     * Creates randomly generated grid of candy
     * @param row number of rows
     * @param col number of columns
     * @return returns randomly generated grid
     */
    public JButton[][] generateRandom(int row, int col) {
        JButton[][] grid = new JButton[row][col];
        String file = "src/img/candy";//candy files all labeled "candy" followed by a number

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int textNUM = (int) (Math.random() * 4);
                Icon candy = new ImageIcon(file + textNUM + ".png"); //selects random candy file
                grid[i][j] = new JButton(candy); //adds random candy button to cell
            }
        }
        return grid;
    }

    /**
     * takes user to win screen if goal is met
     * outputs to "winCount.txt" (win tally)
     * @throws FileNotFoundException
     */
    public void win(JPanel board, JPanel scoreBoard) throws FileNotFoundException { //displays win screen if goal is met
        if (SCORE >= 5){
            PrintStream out = new PrintStream(new File("src/winCount.txt"));
            WINS ++; //adds to total number of wins
            out.print(WINS);

            //clears game board and generates new one for reentry
            board.removeAll();
            scoreBoard.removeAll();
            grid = generateRandom(6,4);

            //formats winScreen
            WinScreen winScreen = new WinScreen("win", new GridLayout(3,1),menuPanel.menu,this);
            try {
                winScreen.setupPanel();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /** Slows down the screen graphics and is called in checkMatches to enable
     * user to see the matching process happen more clearly
     */
    public void displayMatch(){
        //makes program wait in case more matches are made from random replaced candy
        //this way user can see if any additional combos are made
        gameFrame.paint(gameFrame.getGraphics());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
    }

    /**
     * Will check for matches of three and count them as one to score, additional points for
     * every one candy added to match of three (match of 4 will count as 2 points, 5 as 3, etc.)
     * Can match more than three if
     * Checks entire board for matches
     *
     * @param grid  current grid information
     * @param score current score
     */
    public void checkMatches(JButton[][] grid, JButton score,ArrayList<JButton> matches) {

        Icon x = new ImageIcon("src/img/x.png"); //detect this icon for replacement

        JButton[][] newGrid = new JButton[grid.length][grid[0].length];//establishing grid
       // TODO: figure out more efficient code for checkMatches() System.out.println(grid[0][1].getLocation());
        for (int i = 0; i < newGrid.length; i++) {
            for (int j = 0; j < newGrid[i].length; j++) {
                JButton b = new JButton();
                b.setIcon(grid[i][j].getIcon());
                newGrid[i][j] = b;
            }
        }

        for (int i = 0; i < newGrid.length; i++) { //matches rows
            int rowMatchCount = 1;
            for (int j = 0; j < newGrid[i].length - 1; j++) {
                if (newGrid[i][j].getIcon().toString().equals(newGrid[i][j + 1].getIcon().toString())) {
                    rowMatchCount++;
                    if (rowMatchCount >= 3) {
                        SCORE++;
                        score.setText("SCORE : " + SCORE);

                        //sets icons of three in a row to x
                        grid[i][j].setIcon(x);

                        grid[i][j - 1].setIcon(x);

                        grid[i][j + 1].setIcon(x);

                        displayMatch(); // slows down graphics so that matching is visible
                    }
                } else { //if no match is made reset count and do not allow swap
                    rowMatchCount = 1;
                    swapIcons(matches);
                }
            }
        }

        for (int i = 0; i < newGrid[i].length; i++) { //matches columns
            int colMatchCount = 1;
            for (int j = 0; j < newGrid.length - 1; j++) {
                if (newGrid[j][i].getIcon().toString().equals(newGrid[j + 1][i].getIcon().toString())) {
                    colMatchCount++;
                    if (colMatchCount >= 3) {
                        SCORE++;
                        score.setText("SCORE : " + SCORE);

                        //sets icons of three in a row to x
                        grid[j][i].setIcon(x);

                        grid[j + 1][i].setIcon(x);

                        grid[j - 1][i].setIcon(x);

                        displayMatch(); // slows down graphics so that matching is visible

                    }
                } else {//if no match is made reset count and do not allow swap
                    colMatchCount = 1;
                    swapIcons(matches);
                }
            }
        }
    }


    /**
     * Replaces matches with randomly generated icons (detects x icon)
     *
     * @param grid current grid information
     */
    public static void updateRandom(JButton[][] grid) {
        Icon x = new ImageIcon("src/img/x.png");
        String file = "src/img/candy";//candy files all labeled "candy" followed by a number

        //checking for x icon
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getIcon().toString().equals(x.toString())) {

                    //replacing with random candy
                    int textNUM = (int) (Math.random() * 4);
                    Icon candy = new ImageIcon(file + textNUM + ".png");
                    grid[i][j].setIcon(candy);
                }
            }
        }
    }

    /**
     * Runs game by calling other methods
     * Driven by user interaction, interactions start each process
     * also formats based on actions (selected candy background)
     */
    public void startGame(JPanel board, JPanel scoreBoard, JButton scoreButton) {
        ArrayList<JButton> matches = new ArrayList<>();//keeps track of matches

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                //formatting
                grid[i][j].setBackground(Color.WHITE);
                grid[i][j].setOpaque(true);


                JButton button = grid[i][j];

                //user interaction
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent firstClick) {
                        int myX = 0;
                        int myY = 0;
                        // loop sets all buttons backgrounds to white
                        for (int i = 0; i < grid.length; i++) {
                            for (int j = 0; j < grid[i].length; j++) {
                                if (grid[i][j] == button) {
                                    myY = i;
                                    myX = j;
                                }
                                grid[i][j].setBackground(Color.WHITE);
                                grid[i][j].setOpaque(true);
                            }
                        }
                        // sets clicked buttons background to red


                        if (matches.isEmpty()) {
                            matches.add(button);
                        } else {
                            JButton oBut = matches.getFirst();

                            //only adds if buttons are adjacent in grid and not out of bounds or diagonal
                            if (oBut == grid[Math.max(myY - 1, 0)][myX] || oBut == grid[Math.min(myY + 1, 5)][myX]
                                    || oBut == grid[myY][Math.min(myX + 1, 3)] || oBut == grid[myY][Math.max(myX - 1, 0)]) {
                                matches.add(button);
                            }


                        }

                        //sets button background to red
                        for (JButton el : matches) {
                            el.setBackground(Color.RED);
                            el.setOpaque(true);
                        }


                        // once two buttons have been clicked the matching process begins
                        if (matches.size() == 2) {
                            swapIcons(matches);
                            for (JButton[] jButtons : grid) {
                                for (JButton jButton : jButtons) {


                                    jButton.setBackground(Color.WHITE);
                                    jButton.setOpaque(true);
                                }
                            }

                            // checks if match was made
                            checkMatches(grid, scoreButton,matches);
                            // updates board if match is made
                            updateRandom(grid);

                            //calls winScreen
                            try {
                                win(board,scoreBoard);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            matches.clear(); // clears matches array after swapping loop runs
                        }
                    }
                });
                board.add(button);
            }
        }
    }
}






