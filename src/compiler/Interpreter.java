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

    public void interpret() {
        boolean running = true;
        String[] instruction;

        while (running) {
            instruction = AssemblyFile.instructions // list of instructions
                    .get(Hardware.programCounter)   // get element in array
                    .split(" ");               // splits the message with spaces
            int val1 = 0;
            int val2 = 0;
            int label = 0;
            String variable = "";

            // default value for register, it will be modified during the execution,
            // only to prevent not null errors
            Hardware.Register register = Hardware.Register.T0;

            // assign the right values for the real switch statement.
            switch (instruction[0]) {
                case "LDA", "AND", "OR", "ADD", "SUB", "DIV", "MUL", "MOD" -> {
                    register = Hardware.Register.valueOf(instruction[1]);
                    val1 = this.getInt(instruction[2]);
                }
                case "STR" -> {
                    variable = instruction[1];
                    val1 = this.getInt(instruction[2]);
                }
                case "PUSH" -> val1 = this.getInt(instruction[1]);
                case "BEQ", "BNE", "BBG", "BSM" -> {
                    val1 = this.getInt(instruction[1]);
                    val2 = this.getInt(instruction[2]);
                    label = Integer.parseInt(instruction[3]);
                }
                case "POP", "NOT", "INC", "DEC" -> register = Hardware.Register.valueOf(instruction[1]);
                case "JMP" -> label = Integer.parseInt(instruction[1]);
            }

            switch (instruction[0]) {
                case "LDA" -> Hardware.ALU.lda(register, val1);
                case "STR" -> Hardware.ALU.str(variable, val1); // TODO: check if the str only takes reg / const
                case "PUSH"-> Hardware.ALU.push(val1);
                case "POP" -> Hardware.ALU.pop(register);
                case "AND" -> Hardware.ALU.and(register, val1);
                case "OR"  -> Hardware.ALU.or(register, val1);
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
                case "HLT" -> {
                    running = false;
                    Hardware.ALU.hlt();
                }
            }

            if (!instruction[0].equals("HLT") && !instruction[0].equals("JMP")) {
                Hardware.programCounter++;
            }
        }
    }

    private int getInt(String s) {
        // if the string is a number, return the number
        if (s.matches("^[0-9]+$")) {
            return Integer.parseInt(s);
        }
        // if the string starts with a t, return the value of the register
        else if (s.startsWith("T")) {
            return Hardware.Register.valueOf(s).getValue();
        }
        // if the string is a variable name contained in memory, return the value of the variable
        // else if (Hardware.memory.containsKey(s)) {
        return Hardware.memory.get(s);
    }
}
