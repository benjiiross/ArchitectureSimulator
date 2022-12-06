package compiler;

import assembly.AssemblyFile;
import hardware.Hardware;

/**
 * Class that parses the instructions and calls the corresponding methods
 * from the AssemblyFile class.
 * It verifies the syntax of the instructions and throws an exception if
 * they are not correct.
 */
public class Parser {
    // function that check if the instruction in the file are all correct
    public static boolean parse() {
        String[] instructionNames = {
                "LDA",
                "STR",
                "PUSH",
                "POP",
                "AND",
                "OR",
                "NOT",
                "ADD",
                "SUB",
                "DIV",
                "MUL",
                "MOD",
                "INC",
                "DEC",
                "BEQ",
                "BNE",
                "BBG",
                "BSM",
                "JMP",
                "HLT"
        };
        boolean isValid = true;
        int lineIndex = 0;
        String[] instruction;

        try {
            while (isValid) {
                // instruction array that contains the instruction name and the arguments
                instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

                // check if there is an instruction in the line, but it starts by space
                if (instruction.length > 1 && instruction[0].isEmpty()) {
                    throw new Exception("Instruction starts with space");
                }

                // check if the line is a comment or if it is empty, then remove it
                else if (instruction[0].startsWith("!") || instruction[0].isEmpty()) {
                    AssemblyFile.instructions.remove(lineIndex);
                }

                // data section
                else if (instruction[0].startsWith("#DATA")) {
                    // if line starts with #DATA, then go to next one
                    lineIndex++;

                    // check if we are still in the file
                    while (lineIndex < AssemblyFile.instructions.size()) {
                        // instruction array that contains the instruction name and the arguments
                        instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

                        // check if there is an instruction in the line, but it starts by space
                        if (instruction.length > 1 && instruction[0].isEmpty()) {
                            throw new Exception("Instruction starts with space");
                        }

                        // check if the line is a comment or if it is empty, then remove it
                        else if (instruction[0].startsWith("!") || instruction[0].isEmpty()) {
                            AssemblyFile.instructions.remove(lineIndex);
                        }

                        // check if the line is a variable namethat is not a register name
                        else if (instruction[0].matches("[a-zA-Z]+") && !instruction[0].matches("R[0-9]+")) {
                            // check if the variable name is already used
                            if (Hardware.memory.get(instruction[0]) != null) {
                                throw new Exception("Variable name already used");
                            }

                            // check if the variable is initialized
                            else if (instruction.length == 1) {
                                throw new Exception("Variable not initialized");
                            }

                            // check if the variable is initialized with a number
                            else if (!instruction[1].matches("[0-9]+")) {
                                throw new Exception("Variable not initialized with a number");
                            }

                            // add the variable to the variables map
                            Hardware.memory.put(instruction[0], Integer.parseInt(instruction[1]));

                            // remove the line from the instructions list
                            AssemblyFile.instructions.remove(lineIndex);
                        }
                        else if (instruction[0].matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
                            // check if the name of the variable is neither "T0" nor "T1" and if not, add it to the memory
                            if (!instruction[0].equals("T0")
                                    && !instruction[0].equals("T1")
                                    && !instruction[0].equals("T2")
                                    && !instruction[0].equals("T3")) {
                                Hardware.memory.put(instruction[0], 0);
                                lineIndex++;
                            } else {
                                throw new Exception("Variable name cannot be T0 or T1");
                            }
                        }
                    }
                }

                // code section
                else if (instruction[0].startsWith("#CODE")) {
                    // if line starts with #CODE, then go to next one
                    lineIndex++;

                    while (lineIndex < AssemblyFile.instructions.size()) {

                        // TODO: fix this
                        if (instruction.length > 1 && instruction[0].isEmpty()) {
                            throw new Exception("Instruction starts with space");
                        } else if (instruction[0].startsWith("!") || instruction[0].isEmpty()) {
                            AssemblyFile.instructions.remove(lineIndex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            // the parse method returns false if the instruction is not valid
            return false;
        }

        return true;
    }
}