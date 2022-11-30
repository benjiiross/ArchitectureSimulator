/**
 * Class ALU
 * The ALU executes arithmetic and logic operations on the data in the registers.
 */
public class ALU {

    // 1. LDA <reg1> <reg2>/<var>/<const>
    public static void lda() {
    }

    // 2. STR <var> <reg>/<const>
    public static void str() {
    }

    // 3. PUSH <reg>/<var>/<const> : value
    public static void push(byte value) {
        Hardware.stack.push(value);
    }

    // 4. POP <reg>
    // the reg value is the register number
    public static void pop(Hardware.Register reg) {
        reg.setValue(Hardware.stack.pop());
    }

    // 5. AND <reg1> <reg2>/<var>/<const>
    public static void and() {
    }

    // 6. OR <reg1> <reg2>/<var>/<const>
    public static void or() {
    }

    // 7. NOT <reg>
    public static void not() {
    }

    // 8. ADD <reg1> <reg2>/<var>/<const>
    public static void add() {
    }

    // 9. SUB <reg1> <reg2>/<var>/<const>
    public static void sub() {
    }

    // 10. DIV <reg1> <reg2>/<var>/<const>
    public static void div() {
    }

    // 11. MUL <reg1> <reg2>/<var>/<const>
    public static void mul() {
    }

    // 12. MOD <reg1> <reg2>/<var>/<const>
    public static void mod() {
    }

    // 13. INC <reg>
    public static void inc() {
    }

    // 14. DEC <reg>
    public static void dec() {
    }

    // 15. BEQ <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void beq() {
    }

    // 16. BNE <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bne() {
    }

    // 17. BBG <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bbg() {
    }

    // 18. BSM <reg1>/<var1>/<const1> <reg2>/<var2>/<const2> <LABEL>
    public static void bsm() {
    }

    // 19. JMP <LABEL>
    public static void jmp() {
    }

    // 20. HLT
    public static void hlt() {
    }
}
