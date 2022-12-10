package gui;

import assembly.AssemblyFile;
import compiler.Interpreter;
import compiler.Parser;
import hardware.Hardware;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;

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
    private JButton resetSimulationButton;

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

            // if the file was chosen and exist

            if (result == JFileChooser.APPROVE_OPTION
                    && fileChooser.getSelectedFile().exists()
                    && fileChooser.getSelectedFile().getName().endsWith(".mod7")) {

                // import file in AssemblyFile.instructions
                AssemblyFile.importFile(fileChooser.getSelectedFile().getAbsolutePath());

                // reset all data in hardware
                resetData();
                fileStatus.setText("File loaded successfully");

                // change buttons status
                checkFileButton.setEnabled(true);
                stepSimulationButton.setEnabled(false);
                simulateButton.setEnabled(false);
                resetSimulationButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(fileChooser,
                        "Please select a correct file / filename.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        checkFileButton.addActionListener(actionEvent -> {
            // parse the file
            Parser.parse();

            // if the file is valid, enable the simulate button
            if (Parser.isValid) {
                updateData();
                fileStatus.setText("Checked, ready to be executed");
                Interpreter.isRunning = true;

                checkFileButton.setEnabled(false);
                stepSimulationButton.setEnabled(true);
                simulateButton.setEnabled(true);
                resetSimulationButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,
                        "The file is valid.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // else show error message
            else {
                JOptionPane.showMessageDialog(this,
                        Parser.errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);

                AssemblyFile.importFile(AssemblyFile.file.getPath());
                resetData();
                fileStatus.setText("invalid file: " + Parser.errorMessage);

                checkFileButton.setEnabled(false);
                stepSimulationButton.setEnabled(false);
                simulateButton.setEnabled(false);
                resetSimulationButton.setEnabled(false);
            }
        });

        simulateButton.addActionListener(actionEvent -> {
            Interpreter.simulate();
            fileStatus.setText("Running");
            updateData();

            checkFileButton.setEnabled(false);
            stepSimulationButton.setEnabled(false);
            simulateButton.setEnabled(false);
            resetSimulationButton.setEnabled(true);
            fileStatus.setText("Finished");
            codeList.setSelectedIndex(-1);
            nextInstruction.setText("");
        });

        stepSimulationButton.addActionListener(actionEvent -> {
            if (Interpreter.isRunning) {
                fileStatus.setText("Running");
                Interpreter.stepSimulate();
                updateData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "The file is finished.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                checkFileButton.setEnabled(false);
                stepSimulationButton.setEnabled(false);
                simulateButton.setEnabled(false);
                resetSimulationButton.setEnabled(true);
                fileStatus.setText("Finished");
                codeList.setSelectedIndex(-1);
                nextInstruction.setText("");
            }
        });

        resetSimulationButton.addActionListener(actionEvent -> {
            resetData();
            AssemblyFile.importFile(AssemblyFile.file.getPath());
            Parser.parse();
            updateData();
            fileStatus.setText("Reset, ready to be executed");

            Interpreter.isRunning = true;
            stepSimulationButton.setEnabled(true);
            simulateButton.setEnabled(true);
            resetSimulationButton.setEnabled(true);
        });
    }

    private void resetData() {
        Hardware.resetData();
        // file info
        fileName.setText(AssemblyFile.file.getName());
        codeList.setListData(new String[0]);

        // registers
        nextInstruction.setText("");

        t0.setText("");
        t1.setText("");
        t2.setText("");
        t3.setText("");
        programCounter.setText("");

        // memory
        variablesList.setListData(new String[0]);
        stackList.setListData(new Integer[0]);
    }

    private void updateData() {
        // file info
        codeList.setListData(AssemblyFile.instructions.toArray(new String[0]));
        codeList.setSelectedIndex(Hardware.programCounter);

        // registers
        if (Hardware.programCounter >= 0) {
            nextInstruction.setText(AssemblyFile.instructions.get(Hardware.programCounter));

        } else {
            fileStatus.setText("Finished");
            nextInstruction.setText("");
        }
        t0.setText(String.valueOf(Hardware.Register.T0.getValue()));
        t1.setText(String.valueOf(Hardware.Register.T1.getValue()));
        t2.setText(String.valueOf(Hardware.Register.T2.getValue()));
        t3.setText(String.valueOf(Hardware.Register.T3.getValue()));
        programCounter.setText(String.valueOf(Hardware.programCounter));

        // convert hashmap to sorted array, sort array by Hardware.variables.get(key)
        // create array of strings with the format "key address value"
        String[] variables = Hardware.variables.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (0x" + String.format("%04X", entry.getValue()) + ") "
                        + Hardware.memory[entry.getValue()])
                .toArray(String[]::new);

        variablesList.setListData(variables);
        stackList.setListData(Hardware.stack.toArray(new Integer[0]));
    }
}
