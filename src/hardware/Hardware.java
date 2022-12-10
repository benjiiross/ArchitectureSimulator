package hardware;

import compiler.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Class that regroups some hardware components of the program:
 * the ALU, the registers, the memory, the stack
 */
public class Hardware {

    /* Registers that simulates the registers of the computer.
     * The enum keyword is used to create a list of constants.
     * We don't need a constructor because the values are initialized to 0.
     */
    public enum Register {
        // register fields
        T0(),
        T1(),
        T2(),
        T3();

        private int value;
        public int getValue() {
            return this.value;
        }
        public void setValue(int value) {
            this.value = value;
        }
    }

    // Creates the memory:
    // - the memory is an arraylist of integers
    // - the memory is static because it is shared by all the instances of the class
    public static final ArrayList<Integer> memory = new ArrayList<>();

    // this hashmap is used to store the variables and their addresses in memory
    public static final HashMap<String, Integer> variables = new HashMap<>();
    public static final Stack<Integer> stack = new Stack<>();

    public static final HashMap<String, Integer> labels = new HashMap<>();

    // programCounter is a variable that stores the next instruction to be executed
    public static int programCounter = 0;

    public static class ALU {
        // 1. LDA <reg1> <reg2>/<var>/<const>
        public static void lda(Hardware.Register reg1, int value) {
            reg1.setValue(value);
        }

        // 2. STR <var> <reg>/<const>
        // gets the address of the variable and stores the value in the memory
        public static void str(int var, int value) {
            Hardware.memory.set(var, value);
        }

        // 3. PUSH <reg>/<var>/<const> : value
        public static void push(int value) {
            Hardware.stack.push(value);
        }

        // 4. POP <reg>
        public static void pop(Hardware.Register reg) {
            reg.setValue(Hardware.stack.pop());
        }

        // 5. AND <reg1> <reg2>/<var>/<const>
        public static void and(Hardware.Register reg1, int value) {
            reg1.setValue(reg1.getValue() & value);
        }

        // 6. OR <reg1> <reg2>/<var>/<const>
        public static void or(Hardware.Register reg1, int value) {
            reg1.setValue(reg1.getValue() | value);
        }

        // 7. NOT <reg>
        public static void not(Hardware.Register reg){
            reg.setValue(~reg.getValue());
        }

        // 8. ADD <reg1> <reg2>/<var>/<const>
        public static void add(Hardware.Register reg1, int value) {
            reg1.setValue(reg1.getValue() + value);
        }

        // 9. SUB <reg1> <reg2>/<var>/<const>
        public static void sub(Hardware.Register reg1, int value) {
            reg1.setValue(value - reg1.getValue());
        }

        // 10. DIV <reg1> <reg2>/<var>/<const>
        public static void div(Hardware.Register reg1, int value) {
            reg1.setValue(value / reg1.getValue());
        }

        // 11. MUL <reg1> <reg2>/<var>/<const>
        public static void mul(Hardware.Register reg1, int value) {
            reg1.setValue(reg1.getValue() * value);
        }

        // 12. MOD <reg1> <reg2>/<var>/<const>
        public static void mod(Hardware.Register reg1, int value) {
            reg1.setValue(value % reg1.getValue());
        }

        // 13. INC <reg>
        public static void inc(Hardware.Register reg) {
            reg.setValue(reg.getValue() + 1);
        }

        // 14. DEC <reg>
        public static void dec(Hardware.Register reg) {
            reg.setValue(reg.getValue() - 1);
        }

        // 15. BEQ <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
        public static void beq(int value1, int value2, int label) {
            if (value1 == value2) {
                jmp(label);
            } else {
                jmp(Hardware.programCounter + 1);
            }
        }

        // 16. BNE <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
        public static void bne(int value1, int value2, int label) {
            if (value1 != value2) {
                jmp(label);
            } else {
                jmp(Hardware.programCounter + 1);
            }
        }

        // 17. BBG <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
        public static void bbg(int value1, int value2, int label) {
            if (value1 > value2) {
                jmp(label);
            } else {
                jmp(Hardware.programCounter + 1);
            }
        }

        // 18. BSM <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
        public static void bsm(int value1, int value2, int label) {
            if (value1 < value2) {
                jmp(label);
            } else {
                jmp(Hardware.programCounter + 1);
            }
        }

        // 19. JMP <LABEL>
        public static void jmp(int label) {
            System.out.println("Jumping to " + label);
            Hardware.programCounter = label;
        }

        // 20. HLT
        public static void hlt() {
            Interpreter.isRunning = false;
            Hardware.programCounter = -1;
        }

        // 21. SRL <reg> <const>
        public static void srl(Hardware.Register reg, int value) {
            reg.setValue(reg.getValue() << value);
        }

        // 22. SRR <reg> <const>
        public static void sll(Hardware.Register reg, int value) {
            reg.setValue(reg.getValue() >> value);
        }
    }

    public static void resetData() {
        Interpreter.isRunning = false;
        Hardware.Register.T0.setValue(0);
        Hardware.Register.T1.setValue(0);
        Hardware.Register.T2.setValue(0);
        Hardware.Register.T3.setValue(0);
        Hardware.stack.clear();
        Hardware.memory.clear();
        Hardware.variables.clear();
        Hardware.labels.clear();
        Hardware.programCounter = 0;
    }
}
