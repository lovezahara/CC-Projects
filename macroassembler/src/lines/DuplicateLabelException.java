package lines;

import java.io.File;

public class DuplicateLabelException extends Exception {
    private String label;
    public DuplicateLabelException(int linenum, String label) {
        super(label+" redeclared on line "+linenum);
        this.label = label;
    }
    public DuplicateLabelException(File f, int linenum, String label) {
        super(label+" redeclared on line "+linenum+" of "+f.getName());
        this.label = label;
    }

    public String getLabel() { return label; }
}
