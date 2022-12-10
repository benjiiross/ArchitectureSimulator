package compiler;

import assembly.AssemblyFile;
import hardware.Hardware;

/*
 * Interpreter for the ASM language.
 * Reads the value from the Assembly file and executes the instructions.
 * File is "AssemblyFile.file"
 */
public class Interpreter {
    // Interpreter for the ASM language.
    // Reads the value from the Assembly file and executes the instructions.
    // reads each line of the file and executes the instruction.
    public static boolean isRunning = false;

    public static void simulate() {
        while (isRunning) {
            stepSimulate();
        }
    }

    public static void stepSimulate() {
        String[] instruction;
        instruction = AssemblyFile.instructions // list of instructions
                .get(Hardware.programCounter)   // get element in array
                .split(" ");               // splits the message with spaces
        int val1 = 0;
        int val2 = 0;
        int label = 0;
        int variable = 0;

        // default value for register, it will be modified during the execution,
        // only to prevent not null errors
        Hardware.Register register = Hardware.Register.T0;

        // assign the right values for the real switch statement.
        switch (instruction[0]) {
            case "LDA", "AND", "OR", "ADD", "SUB", "DIV", "MUL", "MOD", "SRL", "SRR" -> {
                register = Hardware.Register.valueOf(instruction[1]);
                val1 = getInt(instruction[2]);
            }
            case "STR" -> {
                variable = getInt(instruction[1]);
                val1 = getInt(instruction[2]);
            }
            case "PUSH" -> val1 = getInt(instruction[1]);
            case "BEQ", "BNE", "BBG", "BSM" -> {
                val1 = getInt(instruction[1]);
                val2 = getInt(instruction[2]);
                label = Hardware.labels.get(instruction[3])-1; // julie's fix
            }
            case "POP", "NOT", "INC", "DEC" -> register = Hardware.Register.valueOf(instruction[1]);
            case "JMP" -> label = Hardware.labels.get(instruction[1]);
        }

        switch (instruction[0]) {
            case "LDA" -> Hardware.ALU.lda(register, val1);
            case "STR" -> Hardware.ALU.str(variable, val1);
            case "PUSH" -> Hardware.ALU.push(val1);
            case "POP" -> Hardware.ALU.pop(register);
            case "AND" -> Hardware.ALU.and(register, val1);
            case "OR" -> Hardware.ALU.or(register, val1);
            case "NOT" -> Hardware.ALU.not(register);
            case "ADD" -> Hardware.ALU.add(register, val1);
            case "SUB" -> Hardware.ALU.sub(register, val1);
            case "DIV" -> Hardware.ALU.div(register, val1);
            case "MUL" -> Hardware.ALU.mul(register, val1);
            case "MOD" -> Hardware.ALU.mod(register, val1);
            case "INC" -> Hardware.ALU.inc(register);
            case "DEC" -> Hardware.ALU.dec(register);
            case "BEQ" -> Hardware.ALU.beq(val1, val2, label);
            case "BNE" -> Hardware.ALU.bne(val1, val2, label);
            case "BBG" -> Hardware.ALU.bbg(val1, val2, label);
            case "BSM" -> Hardware.ALU.bsm(val1, val2, label);
            case "JMP" -> Hardware.ALU.jmp(val1);
            case "HLT" -> Hardware.ALU.hlt();
            case "SRL" -> Hardware.ALU.srl(register, val1);
            case "SLL" -> Hardware.ALU.sll(register, val1);
        }

        if (!instruction[0].equals("HLT") && !instruction[0].equals("JMP")) {
            Hardware.programCounter++;
        }

        if (Hardware.programCounter >= AssemblyFile.instructions.size()) {
            isRunning = false;
        }
    }

    private static int getInt(String s) {
        // if the string starts with a t, return the value of the register
        if (s.matches("T[0-3]")) {
            return Hardware.Register.valueOf(s).getValue();
        }
        // else the string is a number+ a label, return the number
        // take the value from beginning to '+', take the value from '+' to the end.
        // take the address from the first value, add the second value to it.
        else if (s.contains("+")) {
            int variableAddress = Hardware.variables.get(s.substring(0, s.indexOf('+')));
            int labelAddress = Integer.parseInt(s.substring(s.indexOf('+') + 1));
            System.out.println(variableAddress + labelAddress);
            // if current instruction is lda
            if (AssemblyFile.instructions.get(Hardware.programCounter).contains("LDA")) {
                return Hardware.memory.get(variableAddress + labelAddress);
            } else {
                return variableAddress + labelAddress;
            }
        }
        // else if the string is a variable name contained in memory, return the address of the variable
        else if (Hardware.variables.containsKey(s)) {
            if (AssemblyFile.instructions.get(Hardware.programCounter).contains("STR")) {
                return Hardware.variables.get(s);
            } else {
                return Hardware.memory.get(Hardware.variables.get(s));
            }
        }
        // else the string is a number, return the number
        else {
            return Integer.parseInt(s);
        }
    }
}
