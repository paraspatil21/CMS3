package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class HomeScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    static String reg_no = null;

    public HomeScreen(Role role, String reg_no) {
        HomeScreen.role = role;
        HomeScreen.reg_no = reg_no;
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel appNameLabel = new JLabel("COLLEGE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        headerPanel.add(appNameLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutButton.addActionListener(evt -> {
            this.dispose();
            new LoginScreen().setVisible(true);
        });
        JPanel logoutWrapper = new JPanel();
        logoutWrapper.add(logoutButton);
        headerPanel.add(logoutWrapper, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        gridPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JButton studentButton = createMenuButton("STUDENT");
        studentButton.addActionListener(evt -> openStudentPortal());

        JButton facultyButton = createMenuButton("FACULTY");
        facultyButton.addActionListener(evt -> openFacultyPortal());

        JButton libraryButton = createMenuButton("LIBRARY");
        JButton hostelButton = createMenuButton("HOSTEL");
        hostelButton.addActionListener(evt -> openHostelPortal());

        JButton attendanceButton = createMenuButton("ATTENDANCE");
        JButton accountsButton = createMenuButton("ACCOUNTS");
        JButton reportsButton = createMenuButton("REPORTS");
        JButton aboutButton = createMenuButton("ABOUT");

        gridPanel.add(studentButton);
        gridPanel.add(facultyButton);
        gridPanel.add(libraryButton);
        gridPanel.add(hostelButton);
        gridPanel.add(attendanceButton);
        gridPanel.add(accountsButton);
        gridPanel.add(reportsButton);
        gridPanel.add(aboutButton);

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        return btn;
    }

    private void openStudentPortal() {
        if (role == null) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied", JOptionPane.ERROR_MESSAGE);
        } else {
            switch (role) {
                case ADMIN:
                case FACULTY:
                    new StudentPortal(role, reg_no).setVisible(true);
                    break;
                case STUDENT:
                    StudentEntryForm entryForm = new StudentEntryForm(role, reg_no);
                    entryForm.setVisible(true);
                    entryForm.showItemToFields(reg_no);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private void openFacultyPortal() {
        if (role == null) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied", JOptionPane.ERROR_MESSAGE);
        } else {
            switch (role) {
                case ADMIN:
                    new FacultyPortal(role).setVisible(true);
                    break;
                case FACULTY:
                    FacultyEntryForm entryForm = new FacultyEntryForm(role);
                    entryForm.setVisible(true);
                    entryForm.showItemToFields(reg_no);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private void openHostelPortal() {
        if (role == null) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied", JOptionPane.ERROR_MESSAGE);
        } else {
            switch (role) {
                case ADMIN:
                case FACULTY:
                    new HostelPortal(role).setVisible(true);
                    break;
                case STUDENT:
                    HostelIssueForm issueForm = new HostelIssueForm(role);
                    issueForm.setVisible(true);
                    issueForm.showItemToFields(reg_no);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new HomeScreen(role, reg_no).setVisible(true));
    }
}
