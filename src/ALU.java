/**
 * Class ALU
 * The ALU executes arithmetic and logic operations on the data in the registers.
 * It is a singleton class since there is only one ALU in the computer.
 */
public final class ALU {
    // create singleton instance
    private static ALU instance = null;

    // private constructor
    private ALU() {}

    // static method to create instance of ALU class
    public static ALU getInstance() {
        if (instance == null) {
            instance = new ALU();
        }
        return instance;
    }

    // 1. LDA <reg1> <reg2>/<var>/<const>
    public static void lda(Hardware.Register reg1, byte value) {
        reg1.setValue(value);
    }

    // 2. STR <var> <reg>/<const>
    public static void str(String var, byte value) {
        Hardware.memory.put(var, value);
    }

    // 3. PUSH <reg>/<var>/<const> : value
    public static void push(byte value) {
        Hardware.stack.push(value);
    }

    // 4. POP <reg>
    public static void pop(Hardware.Register reg) {
        reg.setValue(Hardware.stack.pop());
    }

    // 5. AND <reg1> <reg2>/<var>/<const>
    public static void and(Hardware.Register reg1, byte value) {
        reg1.setValue(reg1.getValue() & value);
    }

    // 6. OR <reg1> <reg2>/<var>/<const>
    public static void or(Hardware.Register reg1,byte value) {
        reg1.setValue(reg1.getValue() | value);
    }

    // 7. NOT <reg>
    public static void not(Hardware.Register reg){
        reg.setValue(~reg.getValue());
    }

    // 8. ADD <reg1> <reg2>/<var>/<const>
    public static void add(Hardware.Register reg1, byte value) {
        reg1.setValue(reg1.getValue() + value);
    }

    // 9. SUB <reg1> <reg2>/<var>/<const>
    public static void sub(Hardware.Register reg1, byte value) {
        reg1.setValue(value - reg1.getValue());
    }

    // 10. DIV <reg1> <reg2>/<var>/<const>
    public static void div(Hardware.Register reg1, byte value) {
        reg1.setValue(value / reg1.getValue());
    }

    // 11. MUL <reg1> <reg2>/<var>/<const>
    public static void mul(Hardware.Register reg1,byte value) {
        reg1.setValue(reg1.getValue() * value);
    }

    // 12. MOD <reg1> <reg2>/<var>/<const>
    public static void mod(Hardware.Register reg1, short value) {
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
    public static void beq(byte value1, byte value2, int label) {
        if (value1 == value2) {
            jmp(label);
        } else {
            jmp(label + 1);
        }
    }

    // 16. BNE <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bne(byte value1, byte value2, int label) {
        if (value1 != value2) {
            jmp(label);
        } else {
            jmp(label + 1);
        }
    }

    // 17. BBG <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bbg(byte value1, byte value2, int label) {
        if (value1 > value2) {
           jmp(label);
        } else {
            jmp(label + 1);
        }
    }

    // 18. BSM <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bsm(byte value1, byte value2, int label) {
        if (value1 < value2) {
            jmp(label);
        } else {
            jmp(label + 1);
        }
    }

    // 19. JMP <LABEL>
    public static void jmp(int label) {
        Hardware.programCounter = label;

    }

    // 20. HLT
    public static void hlt() {
        System.exit(0);
    }
}
