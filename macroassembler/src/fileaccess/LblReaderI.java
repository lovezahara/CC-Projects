package fileaccess;

import lines.DuplicateLabelException;
import lines.LineI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface LblReaderI {
    /** Adds the lines in a file to the lines read
     *
     * @param f the file to read
     *
     * @throws BadFileTypeException if the file does not end in .lbl
     * @throws DuplicateLabelException if a label is declared more than once
     * @throws FileNotFoundException if the file can't be found
     * @throws IOException if there is an underlying IO issue
     */
    public void read(File f) throws BadFileTypeException, DuplicateLabelException, FileNotFoundException, IOException;

    /** Returns all lines read since the LblReader was constructed
     *  <p>Lines are in the same order they were read</p>
     * @return the line instances
     */
    public List<LineI> getLines();

    /** Returns the label line lookup
     *
     * @return the label line lookup
     */
    public HashMap<String, LineI> getLabelLookup();
}
