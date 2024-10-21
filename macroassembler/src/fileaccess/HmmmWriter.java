package fileaccess;

import lines.LineI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/** Class implements the HmmmWriter Interface. Writes appropriate output to .hmmm file.
 */
public class HmmmWriter implements HmmmWriterI {
    HashMap<String,LineI> lookup;

    /** Constructor takes in the line label lookup. Used to instantiate HmmmWriter instance.
     * @param labelLineLookup - A hashtable mapping label text to a LineLabel instance, used to retrieve Line output.
     */
    public HmmmWriter(HashMap<String,LineI> labelLineLookup) {
        this.lookup = labelLineLookup;
    }
    /** Writes a .hmmm file from a list of lines
     *
     * @param f file to write to
     * @param lines the lines to be written
     * @throws BadFileTypeException if the file suffix is not .hmmm
     * @throws IOException if the underlying stream has an IO problem
     */
    @Override
    public void write(File f, List<LineI> lines) throws BadFileTypeException, IOException {
        if(!f.getName().endsWith(".hmmm")){throw new BadFileTypeException(f,f.getName().substring(f.getName().indexOf(".")));}
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

       for (LineI line : lines) {
            bw.write(line.outputText(lookup));
            bw.newLine();
        }
       bw.close();
    }
}
