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
    // boolean that stores if the file is valid.
    public static boolean isValid = true;
    private static int lineIndex = 0;

    // checks if the file is valid, it changes the isValid variable.
    public static void parse() {

        String[] instruction;

        while (lineIndex < AssemblyFile.instructions.size()) {
            instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

            switch (instruction[0]) {
                case "#DATA" -> {
                    checkDataSection();
                    if (!isValid) {
                        return;
                    }
                }
                // if we are in the code section
                case "#CODE" -> {
                    checkCodeSection();
                    if (!isValid) {
                        return;
                    }
                }
                // case if we are not in the data or code section
                default -> {
                    // if the line is a comment || (if the line is empty, meaning that the line only contains space)
                    // don't need to increment the lineIndex because we are removing the line from the list.
                    if (instruction[0].startsWith("!") || (instruction[0].equals("") && instruction.length == 1)) {
                        AssemblyFile.instructions.remove(lineIndex);
                    }

                    // if the line is neither a comment nor a blank line then it is invalid
                    else {
                        isValid = false;
                        return;
                    }
                }
            }
        }
    }
    private static void checkDataSection() {
        String[] instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

        // if the string is not only #DATA, then it is invalid
        if (instruction.length != 1) {
            isValid = false;
            return;
        }

        // else we are in the data section, so we remove the line from the list
        AssemblyFile.instructions.remove(lineIndex);

        // while we are in the data section, and we haven't reached the end of the file
        while (lineIndex < AssemblyFile.instructions.size()) {
            instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

            // successful data section
            if (instruction[0].equals("#CODE")) {
                return;
            }

            // if the variable name is a register name, then it is invalid
            if (instruction[0].matches("^T[0-3]$")) {
                isValid = false;
                return;
            }

            // if instruction[0] is a valid variable name
            // and instruction[1] is a valid integer
            // and the array has only 2 elements
            else if (instruction[0].matches("^[a-zA-Z][a-zA-Z0-9]*$")
                    && instruction[1].matches("^-?[0-9]+$")
                    && instruction.length == 2) {
                Hardware.memory.put(instruction[0], Integer.parseInt(instruction[1]));
                AssemblyFile.instructions.remove(lineIndex);
            }

            else if (instruction[0].startsWith("!") || (instruction[0].equals("") && instruction.length == 1)) {
                AssemblyFile.instructions.remove(lineIndex);
            }

            else {
                isValid = false;
                return;
            }
        }

        // if we reach the end of the file without finding the #CODE section
        if (lineIndex >= AssemblyFile.instructions.size()) {
            isValid = false;
        }
    }

    private static void checkCodeSection() {
        String[] instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

        // if the string is not only #CODE, then it is invalid
        if (instruction.length != 1) {
            isValid = false;
            return;
        }

        // else we are in the code section, so we remove the line
        AssemblyFile.instructions.remove(lineIndex);

        // boolean value to store whether there is a HLT instruction
        // if there is no HLT instruction, then the file is invalid
        boolean hasHLT = false;

        while (lineIndex < AssemblyFile.instructions.size()) {
            instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

            switch (instruction[0]) {
                case "LDA", "AND", "OR", "ADD", "SUB", "DIV", "MUL", "MOD" -> {
                    // if the instruction has 3 arguments
                    // and the first argument is a valid register name
                    // and the second argument is either a register name
                    // or a known variable name or a valid integer
                    if (instruction.length == 3
                            && instruction[1].matches("^T[0-3]$")
                            && (instruction[2].matches("^T[0-3]$")
                            || Hardware.memory.containsKey(instruction[2])
                            || instruction[2].matches("^-?[0-9]+$"))) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "STR" -> {
                    // if the instruction has 3 arguments
                    // and the first argument is a known variable name
                    // and the second argument is either a register name or a constant
                    if (instruction.length == 3
                            && Hardware.memory.containsKey(instruction[1])
                            && (instruction[2].matches("^T[0-3]$")
                            || instruction[2].matches("^-?[0-9]+$"))) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "PUSH" -> {
                    // if the instruction has 2 arguments
                    // and the first argument is either a valid register name
                    // or a known variable name or a valid integer
                    if (instruction.length == 2
                            && (instruction[1].matches("^T[0-3]$")
                            || Hardware.memory.containsKey(instruction[1])
                            || instruction[1].matches("^-?[0-9]+$"))) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "POP", "NOT", "INC", "DEC" -> {
                    // if the instruction has 2 arguments
                    // and the first argument is a valid register name
                    if (instruction.length == 2
                            && instruction[1].matches("^T[0-3]$")) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "BEQ", "BNE", "BBG", "BSM" -> {
                    // if the instruction has 4 arguments
                    // and the first argument is either a valid register name
                    // or a known variable name or a valid integer
                    // and the second argument is either a valid register name
                    // or a known variable name or a valid integer
                    // and the third argument is a valid positive integer
                    if (instruction.length == 4
                            && (instruction[1].matches("^T[0-3]$")
                            || Hardware.memory.containsKey(instruction[1])
                            || instruction[1].matches("^-?[0-9]+$"))
                            && (instruction[2].matches("^T[0-3]$")
                            || Hardware.memory.containsKey(instruction[2])
                            || instruction[2].matches("^-?[0-9]+$"))
                            && instruction[3].matches("^[0-9]+$")) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "JMP" -> {
                    // if the instruction has 2 arguments
                    // and the first argument is a valid positive integer
                    if (instruction.length == 2
                            && instruction[1].matches("^[0-9]+$")) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                case "HLT" -> {
                    // if the instruction has 1 argument
                    if (instruction.length == 1) {
                        lineIndex++;
                        hasHLT = true;
                    } else {
                        isValid = false;
                        return;
                    }
                }

                default -> {
                    if (instruction[0].startsWith("!") || (instruction[0].equals("") && instruction.length == 1)) {
                        AssemblyFile.instructions.remove(lineIndex);
                    } else {
                        isValid = false;
                        return;
                    }
                }
            }
        }
        // if we reach the end of the file without finding the HLT instruction
        if (!hasHLT) {
            isValid = false;
        }
    }
}