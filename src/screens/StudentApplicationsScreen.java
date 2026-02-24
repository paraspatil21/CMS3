package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import models.Student;
import repository.DBConnection;

public class StudentApplicationsScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    private final Connection con = new DBConnection().connect();
    ArrayList<Student> student_list = new ArrayList<>();

    public StudentApplicationsScreen(Role role) {
        StudentApplicationsScreen.role = role;
        initComponents();
        setLocationRelativeTo(null);
        fillTable();
    }

    private ArrayList<Student> retrieveData() {
        ArrayList<Student> stu_list = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student WHERE status NOT LIKE '%CONFIRM%'");
            while (rs.next()) {
                stu_list.add(new Student(rs.getString("name"), rs.getString("roll_no"), rs.getString("application_no"),
                        rs.getString("registration_no"), rs.getString("mother_name"), rs.getString("mother_occupation"),
                        rs.getString("address"), rs.getString("father_name"), rs.getString("father_occupation"),
                        rs.getString("sex"), rs.getString("dob"), rs.getString("phone"), rs.getString("email"),
                        rs.getBytes("photo"), rs.getString("date_of_application"), rs.getString("course"),
                        rs.getString("branch"), rs.getInt("batch"), rs.getString("semester"),
                        rs.getInt("year_of_passing"),
                        rs.getBoolean("hostel"), rs.getBoolean("library"), rs.getString("qualification"),
                        rs.getString("university"), rs.getString("quota"), rs.getString("marks"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return stu_list;
    }

    private void fillTable() {
        student_list = retrieveData();
        DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
        model.setRowCount(0);
        for (Student s : student_list) {
            model.addRow(new Object[] { s.getRegNo(), s.getRollNo(), s.getName(), s.getFather_name(), s.getCourse(),
                    s.getBranch(), s.getSemester() });
        }
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("STUDENT APPLICATIONS");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search"));
        SearchTextField = new JTextField(40);
        searchPanel.add(SearchTextField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchStudent());
        searchPanel.add(searchBtn);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> fillTable());
        searchPanel.add(refreshBtn);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        StudentTable = new JTable(new DefaultTableModel(
                new String[] { "Reg. No.", "Roll No.", "Name", "Father's Name", "Course", "Branch", "Semester" }, 0));
        StudentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int ind = StudentTable.getSelectedRow();
                if (ind >= 0) {
                    String reg = student_list.get(ind).getRegNo();
                    new StudentEntryForm(role, reg).setVisible(true);
                }
            }
        });
        centerPanel.add(new JScrollPane(StudentTable), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void searchStudent() {
        String val = "%" + SearchTextField.getText() + "%";
        try {
            String qry = "SELECT * FROM student WHERE (name LIKE ? OR roll_no LIKE ? OR registration_no LIKE ? OR application_no LIKE ?) AND (status!='CONFIRM')";
            PreparedStatement ps = con.prepareStatement(qry);
            ps.setString(1, val);
            ps.setString(2, val);
            ps.setString(3, val);
            ps.setString(4, val);
            ResultSet rs = ps.executeQuery();
            student_list.clear();
            while (rs.next()) {
                student_list.add(new Student(rs.getString("name"), rs.getString("roll_no"),
                        rs.getString("application_no"),
                        rs.getString("registration_no"), rs.getString("mother_name"), rs.getString("mother_occupation"),
                        rs.getString("address"), rs.getString("father_name"), rs.getString("father_occupation"),
                        rs.getString("sex"), rs.getString("dob"), rs.getString("phone"), rs.getString("email"),
                        rs.getBytes("photo"), rs.getString("date_of_application"), rs.getString("course"),
                        rs.getString("branch"), rs.getInt("batch"), rs.getString("semester"),
                        rs.getInt("year_of_passing"),
                        rs.getBoolean("hostel"), rs.getBoolean("library"), rs.getString("qualification"),
                        rs.getString("university"), rs.getString("quota"), rs.getString("marks"),
                        rs.getString("status")));
            }
            DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
            model.setRowCount(0);
            for (Student s : student_list) {
                model.addRow(new Object[] { s.getRegNo(), s.getRollNo(), s.getName(), s.getFather_name(), s.getCourse(),
                        s.getBranch(), s.getSemester() });
            }
            if (student_list.isEmpty())
                JOptionPane.showMessageDialog(null, "No Records Found!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new StudentApplicationsScreen(role).setVisible(true));
    }

    private JTextField SearchTextField;
    private JTable StudentTable;
}
