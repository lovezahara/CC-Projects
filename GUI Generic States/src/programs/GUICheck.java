package programs;

import gui.Display;

/** Program that instantiates a new display to and sets up the display to run the GUI
 */
public class GUICheck {
    /** Execution starts here
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        Display newDisplay = new Display();
        newDisplay.setUpDisplay();
    }
}
