import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Encapuslates logic for reading/writing files
 */
public class FileAccess {
    public static Grid read_map(String filename) {
        // NOTE: C does not have exceptions... You'll need to detect
        //       that the file did not open or getline() failed
        //       by checking return values
        // NOTE: For on_the_grid all map files tested will have valid
        //       contents. (no missing diminesions, no long/short lines)
        try {
            if(filename==null) { return null; }
            // NOTE: File opening can be done with fopen()
            // NOTE: getline() reads a line at a time from a file
            File f = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();

            // NOTE: the work of these lines can be done with sscanf
            Scanner sc = new Scanner(line);
            int width = sc.nextInt();
            int height = sc.nextInt();

            Grid ret = Grid2D.grid_init(width, height, '#');

            for(int y=0; y<height; y++) {
                line = br.readLine();
                for(int x=0; x<width; x++) {
                    // NOTE: C doesn't have strings... the "string" is just
                    //       a pointer to the address of the first character.
                    ret.set(Point.init(x,y), line.charAt(x));
                }
            }

            br.close();

            return ret;
        } catch(IOException e) {
            return null;
        }
    }


    public static int write_map(String filename, Grid map) {
        // NOTE: C does not have exceptions... You'll need to detect
        //       that the file did not open or getline() failed
        //       by checking return values
        try {
            if(filename==null) { return 1; }
            if(map==null) { return 1; }

            File f = new File(filename);
            FileWriter fw = new FileWriter(f);
            // NOTE: fprintf() will let you write to a file
            fw.write(map.width()+" "+map.height());
            for(int y=0; y<map.height(); y++) {
                fw.write("\n");
                for(int x=0; x<map.width(); x++) {
                    fw.write(map.get(Point.init(x,y)));
                }
            }
            fw.close();
            return 0;
        } catch(IOException e) {
            return 1;
        }
    }
}
