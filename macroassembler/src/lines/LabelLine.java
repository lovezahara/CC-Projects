package lines;

import java.util.HashMap;

/** Extends Line Class , implements specific functionality for label lines
 */
public class LabelLine extends Line {
    String address;

    /** Constructor takes in the lines source line number and text as written in the .lbl file as parameters.
     * @param srcLineNum - .lbl source line number
     * @param txt - .lbl text for line
     */
    public LabelLine(int srcLineNum, String txt) {
        super(srcLineNum, txt);
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

        return "# " + this.sourceText() ;
    }
    /** Produces the address of this line in the output text
     *  For a label the address returned should be the
     * address of the next instruction to appear in the source text. If there is no
     * next instruction, then null should be returned.</p>
     * @return an address for this line
     */
    @Override
    public String address() {
        if(address == null && nextLine == null){
            return null;
        }
        else if(address == null){
            address = nextLine.address();
        }
        return address;
    }
}
