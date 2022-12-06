package gui;

import assembly.AssemblyFile;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

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
    private JList<HashMap<String, Byte>> variablesList;
    private JList<Integer> stackList;


    // Buttons panel
    private JButton loadFileButton;
    private JButton checkFileButton;
    private JButton simulateButton;
    private JButton stepSimulationButton;

    public Window() {
        this.setContentPane(mainPanel);
        this.setTitle("Assembly Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addActionListeners();
        this.setVisible(true);
        this.pack();
    }

    private void addActionListeners() {
        addButtonsListeners();
    }

    private void addButtonsListeners() {
        loadFileButton.addActionListener(actionEvent -> {
            // open file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setCurrentDirectory(new File("./src/assembly/programs"));

            int result = fileChooser.showOpenDialog(this);

            // if the selected file is a .mod7 file, import it
            if (result == JFileChooser.APPROVE_OPTION
                    && fileChooser.getSelectedFile().getAbsolutePath().endsWith(".mod7")) {

                AssemblyFile.importFile(fileChooser.getSelectedFile().getAbsolutePath());
                resetData();
            }
        });

        checkFileButton.addActionListener(actionEvent -> {
            t0.setText(String.valueOf(Integer.parseInt(t0.getText())+1));
        });
        simulateButton.addActionListener(actionEvent -> {
        });
        stepSimulationButton.addActionListener(actionEvent -> {
        });
    }

    private void resetData() {
        // file info
        fileName.setText(AssemblyFile.file.getName());
        fileStatus.setText("File loaded successfully");
        codeList.setListData(AssemblyFile.instructions.toArray(new String[0]));

        // registers
        nextInstruction.setText(AssemblyFile.instructions.get(0));
        t0.setText("0");
        t1.setText("0");
        t2.setText("0");
        t3.setText("0");
        programCounter.setText("0");
        // get all values from Hardware.memory


    }

    public static void main(String[] args) {
        new Window();
    }
}
