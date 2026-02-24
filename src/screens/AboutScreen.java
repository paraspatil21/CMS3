package screens;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AboutScreen extends JFrame {

    public AboutScreen() {
        setTitle("About - College Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("About Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        contentPanel.add(createLabel("Application Name: College Management System"));
        contentPanel.add(createLabel("Version: 1.0.0"));
        contentPanel.add(createLabel("Developer Name: Antigravity AI"));
        contentPanel.add(createLabel("Technology Stack: Java Swing, JDBC"));
        contentPanel.add(createLabel("Database Used: MySQL"));

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }
}
