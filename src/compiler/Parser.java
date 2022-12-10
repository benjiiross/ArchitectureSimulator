package compiler;

import assembly.AssemblyFile;
import hardware.Hardware;

import java.util.ArrayList;

/**
 * Class that parses the instructions and calls the corresponding methods
 * from the AssemblyFile class.
 * It verifies the syntax of the instructions and throws an exception if
 * they are not correct.
 */
public class Parser {
    // boolean that stores if the file is valid.
    public static boolean isValid;
    public static String errorMessage;
    private static int lineIndex;
    private static final ArrayList<String> unknownLabels = new ArrayList<>();
    private static String[] instruction;
    private static int maxMemoryIndex;

    // checks if the file is valid, it changes the isValid variable.
    public static void parse() {

        isValid = true;
        lineIndex = 0;
        errorMessage = "";
        maxMemoryIndex = 0;

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
                        errorMessage = "line \""
                                + AssemblyFile.instructions.get(lineIndex)
                                + "\" is not a valid instruction";
                        return;
                    }
                }
            }
        }
    }
    private static void checkDataSection() {
        // if the instruction is not only #DATA, then it is invalid
        if (instruction.length != 1) {
            isValid = false;
            errorMessage = "Invalid data section declaration, line " + lineIndex;
            return;
        }

        // else we are in the data section, so we remove the line from the list
        AssemblyFile.instructions.remove(lineIndex);

        // while we are in the data section, and we haven't reached the end of the file
        while (lineIndex < AssemblyFile.instructions.size()) {
            instruction = AssemblyFile.instructions.get(lineIndex).split(" ");

            System.out.println(instruction[0]);
            // successful data section
            if (instruction[0].equals("#CODE")) {
                return;
            }

            // if the variable name is a register name, then it is invalid
            if (instruction[0].matches("^T[0-3]$")) {
                isValid = false;
                errorMessage = "variable at line" + lineIndex + "is a register name";
                return;
            }

            // if instruction[0] is a valid variable name
            // and instruction[1] is a valid integer
            // and the array has only 2 elements
            else if (instruction.length == 2
                    && instruction[0].matches("^[a-zA-Z][a-zA-Z0-9]*$")
                    && instruction[1].matches("^-?[0-9]+$")) {
                // if variable is already declared, then it is invalid
                if (Hardware.variables.containsKey(instruction[0])) {
                    isValid = false;
                    errorMessage = "variable at line " + lineIndex + " is already in memory";
                    return;
                }
                Hardware.logicalSize++;
                Hardware.variables.put(instruction[0], Hardware.logicalSize-1);
                Hardware.memory[Hardware.logicalSize-1] = Integer.parseInt(instruction[1]);

                AssemblyFile.instructions.remove(lineIndex);
            }

            else if (instruction[0].startsWith("!") || (instruction[0].equals("") && instruction.length == 1)) {
                AssemblyFile.instructions.remove(lineIndex);
            }

            else {
                isValid = false;
                errorMessage = "Invalid data section declaration, line " + lineIndex;
                return;
            }
        }

        // if we reach the end of the file without finding the #CODE section
        if (lineIndex >= AssemblyFile.instructions.size()) {
            isValid = false;
            errorMessage = "Missing #CODE section";
        }
    }

    private static void checkCodeSection() {
        // if the string is not only #CODE, then it is invalid
        if (instruction.length != 1) {
            isValid = false;
            errorMessage = "Invalid code section declaration, line " + lineIndex;
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
                            || Hardware.variables.containsKey(instruction[2])
                            || instruction[2].matches("^-?[0-9]+$"))) {
                        // check for div by 0
                        if (instruction[0].equals("DIV") && instruction[2].equals("0")) {
                            isValid = false;
                            errorMessage = "Division by zero, line " + lineIndex;
                            return;
                        }
                        else {
                            lineIndex++;
                        }
                    }
                    else if (instruction[0].equals("LDA")
                            && instruction[1].matches("^T[0-3]$")
                            && instruction[2].matches("^[a-zA-Z][a-zA-Z0-9]*\\+[0-9]+$")) {

                        String[] instructionSplit = instruction[2].split("\\+");

                        // if the variable is in memory
                        if (checkMaxMemory(instructionSplit)) return;
                    }
                    else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }

                case "STR" -> {
                    // if the instruction has 3 arguments
                    // and the first argument is a known variable name
                    // and the second argument is either a register name or a constant
                    if (instruction.length == 3
                            && Hardware.variables.containsKey(instruction[1])
                            && (instruction[2].matches("^T[0-3]$")
                            || instruction[2].matches("^-?[0-9]+$"))) {
                        lineIndex++;
                    }
                    else if (instruction[1].matches("^[a-zA-Z][a-zA-Z0-9]*\\+[0-9]+$")
                            && instruction[2].matches("^T[0-3]$")) {

                        String[] instructionSplit = instruction[1].split("\\+");

                        // if the variable is in memory
                        if (checkMaxMemory(instructionSplit)) return;
                    }
                    else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }

                case "PUSH" -> {
                    // if the instruction has 2 arguments
                    // and the first argument is either a valid register name
                    // or a known variable name or a valid integer
                    if (instruction.length == 2
                            && (instruction[1].matches("^T[0-3]$")
                            || Hardware.variables.containsKey(instruction[1])
                            || instruction[1].matches("^-?[0-9]+$"))) {
                        lineIndex++;
                    } else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
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
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }

                case "BEQ", "BNE", "BBG", "BSM" -> {
                    // if the instruction has 4 arguments
                    // and the first argument is either a valid register name
                    // or a known variable name or a valid integer
                    // and the second argument is either a valid register name
                    // or a known variable name or a valid integer
                    if (instruction.length == 4
                            && (instruction[1].matches("^T[0-3]$")
                            || Hardware.variables.containsKey(instruction[1])
                            || instruction[1].matches("^-?[0-9]+$"))

                            && (instruction[2].matches("^T[0-3]$")
                            || Hardware.variables.containsKey(instruction[2])
                            || instruction[2].matches("^-?[0-9]+$"))) {

                        // if the third argument is a valid label
                        if (Hardware.labels.containsKey(instruction[3])) {
                            // the line is good
                            lineIndex++;
                        }
                        // else, that means that we do not know the label, so we add it to the jumpLabels map.
                        // if it is already in it, we do not add it to the map again
                        else if (!unknownLabels.contains(instruction[3])) {
                                unknownLabels.add(instruction[3]);
                                lineIndex++;
                        }
                    } else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }

                case "JMP" -> {
                    // if the instruction has 2 arguments
                    // and the first argument is a valid positive integer
                    if (instruction.length == 2) {
                        // if the third argument is a valid label
                        if (Hardware.labels.containsKey(instruction[1])) {
                            // the line is good
                            lineIndex++;
                        }
                        // else, that means that we do not know the label, so we add it to the jumpLabels map.
                        // if it is already in it, we do not add it to the map again
                        else if (!unknownLabels.contains(instruction[1])) {
                            unknownLabels.add(instruction[1]);
                            lineIndex++;
                        }
                    } else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
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
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }

                default -> {
                    if (instruction[0].startsWith("!") || (instruction[0].equals("") && instruction.length == 1)) {
                        AssemblyFile.instructions.remove(lineIndex);
                    }

                    // if the line is a label with : at the end
                    else if (instruction.length == 1 && instruction[0].matches("^[a-zA-Z]+:$")) {
                        // add the label to the labels hashmap
                        // the label is the label substring without the :
                        if (Hardware.labels.containsKey(instruction[0].substring(0, instruction[0].length() - 1))) {
                            isValid = false;
                            errorMessage ="Label \""
                                    + instruction[0].substring(0, instruction[0].length() - 1)
                                    + "\" is already defined";
                            return;
                        }
                        Hardware.labels.put(instruction[0].substring(0, instruction[0].length() - 1), lineIndex);

                        // remove the label from the instructions list
                        unknownLabels.remove(instruction[0].substring(0, instruction[0].length() - 1));
                        lineIndex++;
                    } else {
                        isValid = false;
                        errorMessage = "Line \"" + AssemblyFile.instructions.get(lineIndex) + "\" is invalid";
                        return;
                    }
                }
            }
        }
        // if we reach the end of the file without finding the HLT instruction
        if (!hasHLT) {
            isValid = false;
            errorMessage = "Missing HLT instruction";
            return;
        }

        // if there are still unknown labels
        if (!unknownLabels.isEmpty()) {
            isValid = false;
            errorMessage = "Unknown labels: " + unknownLabels;
        }

        // if an LDA or STR instruction has an index access bigger than the last index of the array
        if (maxMemoryIndex > Hardware.variables.size() - 1) {
            isValid = false;
            errorMessage = "Memory overflow";
        }
    }

    private static boolean checkMaxMemory(String[] instructionSplit) {
        if (Hardware.variables.containsKey(instructionSplit[0])) {

            // the max memory index is the max number between the current max memory index
            // and the variable index + the offset
            maxMemoryIndex = Math.max(maxMemoryIndex,
                    Hardware.variables.get(instructionSplit[0])
                            + Integer.parseInt(instructionSplit[1]));
            lineIndex++;
        } else {
            isValid = false;
            errorMessage = "Invalid variable name, line " + lineIndex;
            return true;
        }
        return false;
    }
}