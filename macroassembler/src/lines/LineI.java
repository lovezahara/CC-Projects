package lines;

import java.util.HashMap;

/** Interface to describe what a Line should be able to do */
public interface LineI {
    /** Produces the output string for this line
     *  <p>Causes this line to generate its representation for the output file.
     *  Since this line might require looking up a label target, the HashMap
     *  containing this information is provided as an argument</p>
     * @param labellookup A hashtable mapping label text to a LineLabel instance
     * @return text for this line in the output file
     */
    public String outputText(HashMap<String,LineI> labellookup);

    /** Produces the address of this line in the output text
     * <p>Comments and Labels don't have their own addresses, only instructions have
     * their own addresses. For a comment or label the address returned should be the
     * address of the next instruction to appear in the source text. If there is no
     * next instruction, then null should be returned.</p>
     * @return an address for this line
     */
    public String address();

    /** Produces the line number for this line in the source text
     *
     * @return the line number in the source where this line occurred
     */
    public int lineNumber();

    /** Produces the line exactly as it appears in the source text
     *
     * @return the line as it appears in the source text
     */
    public String sourceText();

    /** Produces the lines.LineI for the next line in the source file
     * @return the next line from the source file
     */
    public LineI getNextLine();

    /** Sets a lines.LineI instance as the next line in the source file
     * @param line the line instance that is next
     */
    public void setNextLine(LineI line);
}