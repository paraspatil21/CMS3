package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import repository.DBConnection;

public class MainScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private final Connection conn = new DBConnection().connect();

    public MainScreen() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        JLabel welcomeLabel = new JLabel("WELCOME", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        JLabel toLabel = new JLabel("TO", SwingConstants.CENTER);
        toLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        JLabel appNameLabel = new JLabel("COLLEGE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 36));

        headerPanel.add(welcomeLabel);
        headerPanel.add(toLabel);
        headerPanel.add(appNameLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel btnWrapper = new JPanel();

        JButton loginButton = new JButton("LOGIN HERE");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginButton.setPreferredSize(new java.awt.Dimension(300, 60));
        loginButton.addActionListener(evt -> {
            this.dispose();
            new LoginScreen().setVisible(true);
        });

        JButton applyButton = new JButton(" APPLY NOW");
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        applyButton.setPreferredSize(new java.awt.Dimension(300, 60));
        applyButton.addActionListener(evt -> {
            new StudentApplicationForm(Role.ADMIN).setVisible(true);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(applyButton);
        btnWrapper.add(buttonPanel);
        mainPanel.add(btnWrapper, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MainScreen().setVisible(true));
    }
}
