package assembly;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * File class that imports Assembly *.mod7 files.
 * The files are converted to a list of instructions.
 */
public class AssemblyFile {
    // File that is being imported
    public static File file;

    // The list of instructions
    public static final ArrayList<String> instructions = new ArrayList<>();

    // function that retrieves the list of instructions from Assembly *.mod7 files
    // for example, path = "assembly1.mod7"
    public static void importFile(String path) {
        try {
            // create a new file object
            file = new File(path);

            // create a new scanner object that scans the file
            Scanner sc = new Scanner(file);

            // while there are lines in the file, add them to the list
            while(sc.hasNextLine()) {
                instructions.add(sc.nextLine());
            }

            // close the scanner
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
