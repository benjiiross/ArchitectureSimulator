import java.util.Stack;

/**
 * Class that regroups all the hardware components of the program:
 * the ALU, the registers, the memory, the stack
 */
public class Hardware {

    /*
     * registers that simulates the registers of the computer
     * it stores the data of the variables temporarily
     * in total there are 4 registers of 8 bytes each,
     * so they can store values from -128 to 127
     * the registers are named t0, t1, t2, t3
     */
    public static short t0,t1,t2,t3;

    // programCounter is a variable that stores the next instruction to be executed
    public static int programCounter = 0;

    //create the memory, it is an array of 4096 bytes
    public static final byte[] memory = new byte[4096];

    public static Stack<Byte> stack = new Stack<>();

    //pushes a value on the stack
    public static boolean checkIfFull() {
        return stack.size() == 4096;
    }
    //check if the stack is empty
    public static boolean checkIfEmpty() {
        return stack.size() == 0;
    }
}
