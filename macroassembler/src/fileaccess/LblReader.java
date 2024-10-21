package fileaccess;

import lines.DuplicateLabelException;
import lines.LineFactory;
import lines.LineI;

import javax.sound.sampled.Line;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Class implements the LblReader Interface. Reads .lbl files
 */
public class LblReader implements LblReaderI {
    ArrayList<LineI> lines = new ArrayList<>();
    LineFactory lf = new LineFactory();

    /** Empty constructor used to instantiate LblReader
     */
    public LblReader(){}
    /** Adds the lines in a file to the lines read
     *
     * @param f the file to read
     *
     * @throws BadFileTypeException if the file does not end in .lbl
     * @throws DuplicateLabelException if a label is declared more than once
     * @throws FileNotFoundException if the file can't be found
     * @throws IOException if there is an underlying IO issue
     */
    @Override
    public void read(File f) throws BadFileTypeException, DuplicateLabelException, FileNotFoundException, IOException {
        if(!f.getName().endsWith(".lbl")){throw new BadFileTypeException(f,f.getName().substring(f.getName().indexOf(".")));}
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();

        while (line != null) {
            LineI newLine = lf.createLine(line);
            lines.add(newLine);
            line = br.readLine();
        }

    }
    /** Returns all lines read since the LblReader was constructed
     *  <p>Lines are in the same order they were read</p>
     * @return the line instances
     */
    @Override
    public List<LineI> getLines() {
       for (int i = 0; i < lines.size() -1; i++) {
           lines.get(i).setNextLine(lines.get(i+1));
       }
        return lines;
    }
    /** Returns the label line lookup
     *
     * @return the label line lookup
     */
    @Override
    public HashMap<String, LineI> getLabelLookup() {
        return lf.getLookup();
    }
}
