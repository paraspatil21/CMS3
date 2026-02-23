package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import repository.DBConnection;

public class LoginScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField UsernameTextField;
    private JPasswordField PasswordTextField;
    private JComboBox<String> RoleComboBox;

    public LoginScreen() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 620);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        UsernameTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(UsernameTextField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        PasswordTextField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(PasswordTextField, gbc);

        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);

        RoleComboBox = new JComboBox<>(new String[] { "ADMIN", "FACULTY", "STUDENT" });
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(RoleComboBox, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.addActionListener(evt -> loginAdmin());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loginAdmin() {
        if (UsernameTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Username Field Is Empty!!!");
        } else if (PasswordTextField.getPassword().length <= 0) {
            JOptionPane.showMessageDialog(null, "Password Field Is Empty!!!");
        } else {
            Connection conn = new DBConnection().connect();
            String admin_sql = "SELECT * FROM admins WHERE username=? and password=?";
            String faculty_sql = "SELECT * FROM faculty WHERE registration_no=? and password=?";
            String student_sql = "SELECT * FROM student WHERE registration_no=? and password=?";
            String uname = UsernameTextField.getText();
            String pass = String.copyValueOf(PasswordTextField.getPassword());
            String role = RoleComboBox.getSelectedItem().toString();
            Role role_id = null;
            PreparedStatement stmt = null;
            try {
                switch (role) {
                    case "ADMIN":
                        role_id = Role.ADMIN;
                        stmt = conn.prepareStatement(admin_sql);
                        break;
                    case "FACULTY":
                        role_id = Role.FACULTY;
                        stmt = conn.prepareStatement(faculty_sql);
                        break;
                    case "STUDENT":
                        role_id = Role.STUDENT;
                        stmt = conn.prepareStatement(student_sql);
                        break;
                    default:
                        break;
                }
                if (stmt != null) {
                    stmt.setString(1, uname);
                    stmt.setString(2, pass);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Logged In Successfully.", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                        new HomeScreen(role_id, uname).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong credentials. Please check and try again!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}
