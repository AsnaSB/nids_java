package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardScreen extends JFrame {

    private JButton runButton;
    private JLabel metricsLabel;

    public DashboardScreen() {
        setTitle("NIDS Evaluation Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        metricsLabel = new JLabel("Metrics will appear here");
        runButton = new JButton("Run Evaluation on Test CSV");

        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                metricsLabel.setText("Running evaluation... (placeholder)");
                // Here you will call RuleEngine + Evaluator
            }
        });

        add(runButton);
        add(metricsLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new DashboardScreen();
    }
}
