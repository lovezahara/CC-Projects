package fileaccess;

import java.io.File;

/** Inidicates that a file type is not what is expected */
public class BadFileTypeException extends Exception {
    /** Creates a BadFileTypeException
     *
     * @param f the file instance that has the wrong type
     * @param type the desired type
     */
    public BadFileTypeException(File f, String type) {
        super(f.getName()+" has the wrong type. Should be "+type);
    }
}
