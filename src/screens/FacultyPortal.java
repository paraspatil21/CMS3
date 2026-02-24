package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import models.Faculty;
import repository.DBConnection;

public class FacultyPortal extends JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    private final Connection con = new DBConnection().connect();
    ArrayList<Faculty> faculty_list = new ArrayList<>();

    public FacultyPortal(Role role) {
        FacultyPortal.role = role;
        initComponents();
        setLocationRelativeTo(null);
        fillTable();
    }

    private ArrayList<Faculty> retrieveData() {
        ArrayList<Faculty> fac_list = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM faculty");
            while (rs.next()) {
                fac_list.add(new Faculty(rs.getString("registration_no"), rs.getString("name"),
                        rs.getString("father_name"), rs.getString("sex"), rs.getString("dob"),
                        rs.getString("email"), rs.getString("phone"), rs.getString("password"),
                        rs.getString("address"), rs.getBytes("photo"), rs.getString("qualifications"),
                        rs.getString("institution"), rs.getString("designation"), rs.getInt("experience"),
                        rs.getString("course"), rs.getString("department"), rs.getString("date_joined"),
                        rs.getString("date_updated")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return fac_list;
    }

    private void fillTable() {
        faculty_list = retrieveData();
        DefaultTableModel model = (DefaultTableModel) FacultyTable.getModel();
        model.setRowCount(0);
        for (Faculty f : faculty_list) {
            model.addRow(new Object[] { f.getRegNo(), f.getName(), f.getQualifications(), f.getDesignation(),
                    f.getDepartment(), f.getExperience(), f.getDate_joined() });
        }
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("FACULTY PORTAL");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton AddFacultyButton = new JButton("Add Faculty");
        AddFacultyButton.addActionListener(e -> {
            if (role == Role.ADMIN)
                new FacultyEntryForm(role).setVisible(true);
            else
                JOptionPane.showMessageDialog(null, "Access Denied!");
        });
        JButton RefreshButton = new JButton("Refresh");
        RefreshButton.addActionListener(e -> fillTable());

        buttonPanel.add(AddFacultyButton);
        buttonPanel.add(RefreshButton);
        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search"));
        SearchTextField = new JTextField(40);
        searchPanel.add(SearchTextField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchFaculty());
        searchPanel.add(searchBtn);
        centerPanel.add(searchPanel, BorderLayout.CENTER);

        FacultyTable = new JTable(new DefaultTableModel(
                new String[] { "Reg No.", "Name", "Qualification", "Designation", "Department", "Experience", "DOJ" },
                0));
        FacultyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int ind = FacultyTable.getSelectedRow();
                String reg_no = faculty_list.get(ind).getRegNo();
                FacultyEntryForm entryForm = new FacultyEntryForm(role);
                entryForm.setVisible(true);
                entryForm.showItemToFields(reg_no);
            }
        });
        centerPanel.add(new JScrollPane(FacultyTable), BorderLayout.SOUTH);
        FacultyTable.setPreferredScrollableViewportSize(new java.awt.Dimension(1100, 500));

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void searchFaculty() {
        String val = "%" + SearchTextField.getText() + "%";
        try {
            String qry = "SELECT * FROM faculty WHERE name LIKE ? OR registration_no LIKE ? OR designation LIKE ?";
            PreparedStatement ps = con.prepareStatement(qry);
            ps.setString(1, val);
            ps.setString(2, val);
            ps.setString(3, val);
            ResultSet rs = ps.executeQuery();
            faculty_list.clear();
            while (rs.next()) {
                faculty_list.add(new Faculty(rs.getString("registration_no"), rs.getString("name"),
                        rs.getString("father_name"), rs.getString("sex"), rs.getString("dob"),
                        rs.getString("email"), rs.getString("phone"), rs.getString("password"),
                        rs.getString("address"), rs.getBytes("photo"), rs.getString("qualifications"),
                        rs.getString("institution"), rs.getString("designation"), rs.getInt("experience"),
                        rs.getString("course"), rs.getString("department"), rs.getString("date_joined"),
                        rs.getString("date_updated")));
            }
            DefaultTableModel model = (DefaultTableModel) FacultyTable.getModel();
            model.setRowCount(0);
            for (Faculty f : faculty_list) {
                model.addRow(new Object[] { f.getRegNo(), f.getName(), f.getQualifications(), f.getDesignation(),
                        f.getDepartment(), f.getExperience(), f.getDate_joined() });
            }
            if (faculty_list.isEmpty())
                JOptionPane.showMessageDialog(null, "No Records Found!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FacultyPortal(role).setVisible(true));
    }

    private JTextField SearchTextField;
    private JTable FacultyTable;
}
