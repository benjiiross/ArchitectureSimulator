import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

/**
 * File class that imports Assembly *.mod7 files.
 * The files are converted to a list of instructions.
 */
public class AssemblyFile {
    // The list of instructions
    public static final ArrayList<String> instructions = new ArrayList<>();

    // function that retrieves the list of instructions from Assembly *.mod7 files
    // for example, path = "assembly1.mod7"
    public static void importFile(String path) {
        try {
            // if the path don't finis with ".mod7", it will throw an exception
            if (!path.endsWith(".mod7")) {
                throw new FileNotFoundException("The file must be a .mod7 file");
            }

            // create a new file object
            File assemblyFile = new File(path);

            // create a new scanner object that scans the file
            Scanner sc = new Scanner(assemblyFile);

            // while there are lines in the file, add them to the list
            while(sc.hasNextLine()) {
                instructions.add(sc.nextLine());
            }

            // close the scanner
            sc.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }
}
