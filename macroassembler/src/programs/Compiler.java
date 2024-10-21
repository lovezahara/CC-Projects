package programs;

import fileaccess.*;
import lines.DuplicateLabelException;
import lines.LineFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class Compiler {
    private Compiler() {}

    public static void main(String[] args) {
        if(args.length<2) {
            System.out.println("Usage: java Compiler <outfile.hmmm> <file1.lbl> ...");
            System.exit(1);
        }

        try {
            // Read in all the source files
            LblReaderI reader = new LblReader();
            for (int i = 1; i < args.length; i++) {
                File f = new File(args[i]);
                reader.read(f);
            }

            // Write out the hmmm code
            HmmmWriterI writer = new HmmmWriter(reader.getLabelLookup());
            File f = new File(args[0]);
            writer.write(f, reader.getLines());
        } catch (DuplicateLabelException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unrecoverable error. Underlying file access failed.");
            System.exit(1);
        } catch (BadFileTypeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
