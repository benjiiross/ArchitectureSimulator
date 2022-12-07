package gui;

import assembly.AssemblyFile;
import compiler.Interpreter;
import compiler.Parser;
import hardware.Hardware;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Window extends JFrame {
    private JPanel mainPanel;


    // File info panel
    private JLabel fileName;
    private JLabel fileStatus;
    private JList<String> codeList;


    // Registers panel
    private JLabel nextInstruction;
    private JLabel t0;
    private JLabel t1;
    private JLabel t2;
    private JLabel t3;
    private JLabel programCounter;


    // Memory info panel
    private JList<String> variablesList;
    private JList<Integer> stackList;


    // Buttons panel
    private JButton loadFileButton;
    private JButton checkFileButton;
    private JButton simulateButton;
    private JButton stepSimulationButton;

    public Window() {
        // set look and feel to system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.setContentPane(mainPanel);
        this.setTitle("Assembly Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addActionListeners();
        this.setVisible(true);
        this.pack();
    }

    private void addActionListeners() {
        loadFileButton.addActionListener(actionEvent -> {
            // open file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setCurrentDirectory(new File("./src/assembly/programs"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Only .mod7 files", "mod7"));

            int result = fileChooser.showOpenDialog(this);

            // if the file was chosen, load it
            if (result == JFileChooser.APPROVE_OPTION) {

                // import file in AssemblyFile.instructions
                AssemblyFile.importFile(fileChooser.getSelectedFile().getAbsolutePath());

                // reset all data in hardware
                resetData();

                // change buttons status
                checkFileButton.setEnabled(true);
                stepSimulationButton.setEnabled(false);
                simulateButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(fileChooser,
                        "Please select a file.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        checkFileButton.addActionListener(actionEvent -> {
            Parser.parse();

            if (Parser.isValid) {
                fileStatus.setText("Checked");

                checkFileButton.setEnabled(false);
                stepSimulationButton.setEnabled(true);
                simulateButton.setEnabled(true);

                updateData();
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "The file is not valid.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        simulateButton.addActionListener(actionEvent -> Interpreter.simulate());

        stepSimulationButton.addActionListener(actionEvent -> Interpreter.stepSimulate());
    }

    private void resetData() {
        // file info
        fileName.setText(AssemblyFile.file.getName());
        fileStatus.setText("File loaded successfully");

        // registers
        nextInstruction.setText("");

        t0.setText("");
        t1.setText("");
        t2.setText("");
        t3.setText("");
        programCounter.setText("");

        // memory
        variablesList.setListData(new String[]{});
        stackList.setListData(new Integer[]{});

        Hardware.resetData();
    }

    private void updateData() {
        // file info
        codeList.setListData(AssemblyFile.instructions.toArray(new String[0]));


        // registers
        nextInstruction.setText(AssemblyFile.instructions.get(Hardware.programCounter));

        t0.setText(String.valueOf(Hardware.Register.T0.getValue()));
        t1.setText(String.valueOf(Hardware.Register.T1.getValue()));
        t2.setText(String.valueOf(Hardware.Register.T2.getValue()));
        t3.setText(String.valueOf(Hardware.Register.T3.getValue()));
        programCounter.setText(String.valueOf(Hardware.programCounter));


        // memory info
        String[] variables = new String[Hardware.memory.size()];
        Integer[] values = new Integer[Hardware.memory.size()];
        int i = 0;
        for (String key : Hardware.memory.keySet()) {
            variables[i] = key;
            values[i] = Hardware.memory.get(key);
            i++;
        }

        String[] memoryList = new String[variables.length];
        for (int j = 0; j < variables.length; j++) {
            memoryList[j] = variables[j] + " " + values[j];
        }

        variablesList.setListData(memoryList);
        stackList.setListData(Hardware.stack.toArray(new Integer[0]));
    }
}
