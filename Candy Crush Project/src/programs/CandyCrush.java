package programs;

import gui.Menu;
import gui.MainGame;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Runs the code
 * Outputs to "winCount.txt" number of wins
 * Formats menu panel on startup
 */

public class CandyCrush {

    public static Menu menuPanel = new Menu ("menuPanel",new GridLayout(4,1)); //formatting menu

    public static void main(String[] args) throws FileNotFoundException {
        // instantiating new gui.Menu object menuPanel
        PrintStream out = new PrintStream(new File("src/winCount.txt"));
        out.print(0); //wins start at 0
        menuPanel.setupPanel();

    }
}
