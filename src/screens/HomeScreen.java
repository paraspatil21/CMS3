package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
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
        setTitle("College Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel appNameLabel = new JLabel("College Management System", SwingConstants.CENTER);
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        headerPanel.add(appNameLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutButton.addActionListener(evt -> {
            this.dispose();
            new LoginScreen().setVisible(true);
        });
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutWrapper.add(logoutButton);
        headerPanel.add(logoutWrapper, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Dashboard Grid: 2-2-1 Centered layout
        JPanel centerContainer = new JPanel(new GridLayout(3, 1, 20, 20));
        centerContainer.setBorder(new EmptyBorder(30, 100, 30, 100));

        // Row 1: 2 Panels
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 20));
        row1.add(createMenuButton("Student", evt -> openStudentPortal()));
        row1.add(createMenuButton("Faculty", evt -> openFacultyPortal()));

        // Row 2: 2 Panels
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 20));
        row2.add(createMenuButton("Hostel", evt -> openHostelPortal()));
        row2.add(createMenuButton("Library", evt -> openLibrary()));

        // Row 3: 1 centered Panel
        JPanel row3Wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton aboutBtn = createMenuButton("About", evt -> openAbout());
        // To make the 1 button same relative width, we can put it in a grid or set size
        JPanel row3 = new JPanel(new GridLayout(1, 1));
        row3.add(aboutBtn);
        row3.setPreferredSize(new Dimension(380, 120)); // Approximate half width of rows
        row3Wrapper.add(row3);

        centerContainer.add(row1);
        centerContainer.add(row2);
        centerContainer.add(row3Wrapper);

        mainPanel.add(centerContainer, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.addActionListener(listener);
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

    private void openLibrary() {
        new LibraryScreen().setVisible(true);
    }

    private void openAbout() {
        new AboutScreen().setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new HomeScreen(role, reg_no).setVisible(true));
    }
}
