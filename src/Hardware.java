import java.util.Stack;

/**
 * Class that regroups some hardware components of the program:
 * the ALU, the registers, the memory, the stack
 */
public class Hardware {
    /* Registers that simulates the registers of the computer.
     * The enum keyword is used to create a list of constants.
     * We don't need a constructor because the values are initialized to 0.
     * TODO: ask for register value type
     */
    public enum Register {
        // register fields
        T0(),
        T1(),
        T2(),
        T3();
        private int value;
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
    };

    // creates a reference to the ALU class without creating an instance of it
    public static ALU alu = new ALU();

    public static final byte[] memory = new byte[4096];

    // the stack is logically locked to 4096 bytes
    public static final Stack<Byte> stack = new Stack<>();

    // programCounter is a variable that stores the next instruction to be executed
    public static int programCounter = 0;
}
