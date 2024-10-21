package lines;

import java.util.HashMap;

/** Class implements the Line interface. Makes up the basic functionality of line objects */
public abstract class Line implements LineI {
    int srcLineNum;
    int instrNum;
    LineI nextLine;
    String txt;

    /** Constructor for non-instruction lines. Takes in the lines source line number and
     * text as written in the .lbl file as parameters.
     * @param sourceLineNum - .lbl source line number
     * @param text - .lbl text for line
     */
    public Line(int sourceLineNum, String text) {
        this.srcLineNum = sourceLineNum;
        this.txt = text;
    }

    /** Constructor for instruction lines. Takes in the lines source line number and
     * text as written in the .lbl file as parameters. Also takes in one additional parameter
     * representing the instruction number in relation to other instruction lines.
     * @param sourceLineNum - .lbl source line number
     * @param instructionNumber - number of the instruction line
     * @param text - .lbl text for line
     */
    public Line(int sourceLineNum, int instructionNumber, String text) {
        this.srcLineNum = sourceLineNum;
        this.instrNum = instructionNumber;
        this.txt = text;
    }

    /** Produces the output string for this line
     *  <p>Causes this line to generate its representation for the output file.
     *  Since this line might require looking up a label target, the HashMap
     *  containing this information is provided as an argument</p>
     * @param labellookup A hashtable mapping label text to a LineLabel instance
     * @return text for this line in the output file
     */
    @Override
    public String outputText(HashMap<String, LineI> labellookup) {
        return "";
    }

    /** Produces the address of this line in the output text
     * <p>Comments and Labels don't have their own addresses, only instructions have
     * their own addresses. For a comment or label the address returned should be the
     * address of the next instruction to appear in the source text. If there is no
     * next instruction, then null should be returned.</p>
     * @return an address for this line
     */
    @Override
    public String address() {
        return "";
    }

    /** Produces the line number for this line in the source text
     *
     * @return the line number in the source where this line occurred
     */
    @Override
    public int lineNumber() {
        return srcLineNum;
    }

    /** Produces the line exactly as it appears in the source text
     *
     * @return the line as it appears in the source text
     */
    @Override
    public String sourceText() {
        return txt;
    }

    /** Produces the lines.LineI for the next line in the source file
     * @return the next line from the source file
     */
    @Override
    public LineI getNextLine() {
        return nextLine;
    }

    /** Sets a lines.LineI instance as the next line in the source file
     * @param line the line instance that is next
     */
    @Override
    public void setNextLine(LineI line) {
        nextLine = line;
    }
}
