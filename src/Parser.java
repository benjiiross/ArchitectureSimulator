/**
 * Class that parses the instructions and calls the corresponding methods
 * from the AssemblyFile class.
 * It verifies the syntax of the instructions and throws an exception if
 * they are not correct.
 */
public class Parser {
    // function that check if the instruction in the file are all correct
    public static boolean parse() {

        /*
            the function parse verifies the syntax of the instructions
            before the interpreter can execute them
            it returns true if the instructions are correct
            the variable containing the instructions is called AssemblyFile.instructions

         */

        // for each line in assembly file
        for (String line : AssemblyFile.instructions) {
            try {
                //...

                // - don't forget to create variables in the memory if there are
                // - don't forget to return false if there is an error
                // - don't forget to REALLY check if the INPUT TYPES are correct
                //   for example, <var> REALLY needs to be a variable
            }
            catch(Exception e) {


            }
        }


        // returns if the instructions are correct
        return true;
    }
}