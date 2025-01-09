public class CopyMap {
    public static void main(String[] argv) {
        if(argv.length!=2) {
            System.out.println("Usage: java CopyMap <infile> <outfile>\n");
            System.exit(1);
        }

        Grid map = FileAccess.read_map(argv[0]);
        if(map==null) {
            System.out.println("Unable to read "+argv[0]);
            System.exit(1);
        }
        int err = FileAccess.write_map(argv[1],map);
        if(err!=0) {
            System.out.println("Unable to write "+argv[1]);
            System.exit(1);
        }
    }
}
