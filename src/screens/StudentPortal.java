package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.Student;
import repository.DBConnection;

public class StudentPortal extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    static String reg_no = null;
    private final Connection con = new DBConnection().connect();
    ArrayList<Student> student_list = new ArrayList<>();

    public StudentPortal(Role role, String app_no) {
        StudentPortal.role = role;
        StudentPortal.reg_no = app_no;
        initComponents();
        customizeComponents();
        fillTable();
    }

    private void customizeComponents() {
        setLocationRelativeTo(this);
        setTitle("STUDENT PORTAL");
    }

    private ArrayList<Student> retrieveData() {
        ArrayList<Student> stu_list = new ArrayList<>();
        try {
            String qry = "SELECT * FROM student WHERE status LIKE '%CONFIRM%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                Student s = new Student(rs.getString("name"), rs.getString("roll_no"), rs.getString("application_no"),
                        rs.getString("registration_no"), rs.getString("mother_name"), rs.getString("mother_occupation"),
                        rs.getString("address"), rs.getString("father_name"), rs.getString("father_occupation"),
                        rs.getString("sex"), rs.getString("dob"), rs.getString("phone"), rs.getString("email"),
                        rs.getBytes("photo"), rs.getString("date_of_application"), rs.getString("course"),
                        rs.getString("branch"), rs.getInt("batch"), rs.getString("semester"),
                        rs.getInt("year_of_passing"),
                        rs.getBoolean("hostel"), rs.getBoolean("library"), rs.getString("qualification"),
                        rs.getString("university"), rs.getString("quota"), rs.getString("marks"),
                        rs.getString("status"));
                stu_list.add(s);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return stu_list;
    }

    private void fillTable() {
        student_list.clear();
        student_list = retrieveData();
        DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
        model.setRowCount(0);
        for (Student s : student_list) {
            model.addRow(new Object[] { s.getRegNo(), s.getRollNo(), s.getName(), s.getFather_name(), s.getCourse(),
                    s.getBranch(), s.getSemester() });
        }
    }

    private void initComponents() {
        TitlePanel = new JPanel();
        TitleLabel = new JLabel("STUDENT PORTAL");
        ButtonPanel = new JPanel(new FlowLayout());
        EntryButton = new JButton("Entry Form");
        ApplicationButton = new JButton("Application Form");
        ConfirmationButton = new JButton("View Applications");
        RefreshButton = new JButton("Refresh");
        SearchPanel = new JPanel(new FlowLayout());
        SearchTextField = new JTextField(40);
        SearchButton = new JButton("Search");
        TablePanel = new JPanel(new BorderLayout());
        StudentTable = new JTable();
        StudentScrollPane = new JScrollPane(StudentTable);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TitleLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
        TitlePanel.add(TitleLabel);

        ButtonPanel.add(EntryButton);
        ButtonPanel.add(ApplicationButton);
        ButtonPanel.add(ConfirmationButton);
        ButtonPanel.add(RefreshButton);

        SearchPanel.add(new JLabel("Search"));
        SearchPanel.add(SearchTextField);
        SearchPanel.add(SearchButton);

        StudentTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Reg. No.", "Roll No.", "Name", "Father's Name", "Course", "Branch", "Semester" }) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        TablePanel.add(StudentScrollPane, BorderLayout.CENTER);

        EntryButton.addActionListener(this::EntryButtonActionPerformed);
        ApplicationButton.addActionListener(this::ApplicationButtonActionPerformed);
        ConfirmationButton.addActionListener(this::ConfirmationButtonActionPerformed);
        RefreshButton.addActionListener(evt -> fillTable());
        SearchButton.addActionListener(evt -> performSearch());
        StudentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                StudentTableMousePressed(evt);
            }
        });

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(TitlePanel);
        topPanel.add(ButtonPanel);
        topPanel.add(SearchPanel);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(TablePanel, BorderLayout.CENTER);

        pack();
        setSize(1200, 800);
    }

    private void performSearch() {
        String val = SearchTextField.getText();
        student_list.clear();
        try {
            String qry = "SELECT * FROM student WHERE name LIKE '%" + val + "%' OR roll_no LIKE '%" + val + "%' "
                    + "OR registration_no LIKE '%" + val + "%' OR application_no LIKE '%" + val + "%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                Student s = new Student(rs.getString("name"), rs.getString("roll_no"), rs.getString("application_no"),
                        rs.getString("registration_no"), rs.getString("mother_name"), rs.getString("mother_occupation"),
                        rs.getString("address"), rs.getString("father_name"), rs.getString("father_occupation"),
                        rs.getString("sex"), rs.getString("dob"), rs.getString("phone"), rs.getString("email"),
                        rs.getBytes("photo"), rs.getString("date_of_application"), rs.getString("course"),
                        rs.getString("branch"), rs.getInt("batch"), rs.getString("semester"),
                        rs.getInt("year_of_passing"),
                        rs.getBoolean("hostel"), rs.getBoolean("library"), rs.getString("qualification"),
                        rs.getString("university"), rs.getString("quota"), rs.getString("marks"),
                        rs.getString("status"));
                student_list.add(s);
            }
            DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
            model.setRowCount(0);
            for (Student s : student_list) {
                model.addRow(new Object[] { s.getRegNo(), s.getRollNo(), s.getName(), s.getFather_name(), s.getCourse(),
                        s.getBranch(), s.getSemester() });
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void EntryButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role == Role.ADMIN) {
            new StudentEntryForm(role, reg_no).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied", 0);
        }
    }

    private void ApplicationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new StudentApplicationForm(role).setVisible(true);
    }

    private void ConfirmationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role == Role.ADMIN) {
            new StudentApplicationsScreen(role).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised", "Access Denied", 0);
        }
    }

    private void StudentTableMousePressed(java.awt.event.MouseEvent evt) {
        int r = StudentTable.getSelectedRow();
        if (r != -1) {
            String reg = student_list.get(r).getRegNo();
            StudentEntryForm form = new StudentEntryForm(role, reg_no);
            form.setVisible(true);
            form.showItemToFields(reg);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new StudentPortal(role, reg_no).setVisible(true));
    }

    private javax.swing.JButton ApplicationButton;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JButton ConfirmationButton;
    private javax.swing.JButton EntryButton;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JButton SearchButton;
    private javax.swing.JPanel SearchPanel;
    private javax.swing.JTextField SearchTextField;
    private javax.swing.JScrollPane StudentScrollPane;
    private javax.swing.JTable StudentTable;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel TitlePanel;
}
