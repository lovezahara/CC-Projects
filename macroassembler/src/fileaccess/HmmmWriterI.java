package fileaccess;

import lines.LineI;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface HmmmWriterI {
    /** Writes a .hmmm file from a list of lines
     *
     * @param f file to write to
     * @param lines the lines to be written
     * @throws BadFileTypeException if the file suffix is not .hmmm
     * @throws IOException if the underlying stream has an IO problem
     */
    public void write(File f, List<LineI> lines) throws BadFileTypeException, IOException;
}
