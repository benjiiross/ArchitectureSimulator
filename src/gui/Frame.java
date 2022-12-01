package gui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public Frame() {
        super("Assembly Simulator");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(null);

        ImageIcon icon = new ImageIcon("./src/assets/icon.png");
        this.setIconImage(icon.getImage());

        this.addFileInfoPanel();

        this.pack();
    }

    private void addFileInfoPanel() {
        JPanel fileInfoPanel = new JPanel();
        fileInfoPanel.setBackground(Color.WHITE);
    }

}
