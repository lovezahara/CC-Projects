package lines;

import java.util.HashMap;
/** Extends Line Class , implements specific functionality for instruction lines
 */
public class InstrLine extends Line {
    String address = String.valueOf(instrNum);

    /** Constructor takes in the lines source line number and
     * text as written in the .lbl file as parameters. Also takes in instrNum
     * representing the instruction number in relation to other instruction lines.
     * @param srcLineNum - .lbl source line number
     * @param instrNum - number of the instruction line
     * @param txt - .lbl text for line
     */
    public InstrLine (int srcLineNum, int instrNum, String txt){
        super(srcLineNum, instrNum, txt);
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
        String stripedScrText = this.sourceText();
        if(this.sourceText().contains("#")){
            stripedScrText = this.sourceText().split("#")[0];
            stripedScrText = stripedScrText.stripTrailing();
        }
        stripedScrText = stripedScrText.stripTrailing();

        if(stripedScrText.endsWith(":")){
            int i = this.sourceText().indexOf(":");
            String label = stripedScrText.substring(i+1, stripedScrText.length()-1);
            LineI labelLine = labellookup.get(label);
            String llAddress = labelLine.getNextLine().address();
            String strippedTxt = stripedScrText.substring(0, stripedScrText.indexOf(":"));
            return this.address +" "+ strippedTxt + llAddress + " # :"+ label+":";
        }
        return this.address +" "+ stripedScrText;
    }
    /** Produces the address of this line in the output text
     * Instructions have their own addresses.
     * @return an address for this line
     */
    @Override
    public String address() {
        return address;
    }
}
