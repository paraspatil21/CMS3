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
import models.TimeTable;
import repository.DBConnection;

public class FacultySubjectIssuingScreen extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    private final Connection con = new DBConnection().connect();
    ArrayList<TimeTable> timeTables = new ArrayList<>();

    public FacultySubjectIssuingScreen(Role role) {
        FacultySubjectIssuingScreen.role = role;
        initComponents();
        customizeComponents();
        fillTable();
    }

    private void customizeComponents() {
        setLocationRelativeTo(this);
        setTitle("TIME TABLE");
    }

    private ArrayList<TimeTable> retrieveData() {
        ArrayList<TimeTable> tTables = new ArrayList<>();
        try {
            String qry = "SELECT * FROM time_table";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                TimeTable t = new TimeTable(rs.getString("id"), rs.getString("subject"),
                        rs.getString("faculty"), rs.getString("course"),
                        rs.getString("branch"), rs.getString("semester"), rs.getString("section"), rs.getString("day"),
                        rs.getString("time"), rs.getString("timestamp"));
                tTables.add(t);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return tTables;
    }

    private void fillTable() {
        timeTables = retrieveData();
        DefaultTableModel model = (DefaultTableModel) TimeTableTable.getModel();
        model.setRowCount(0);
        for (TimeTable t : timeTables) {
            model.addRow(new Object[] { t.getCourse(), t.getBranch(), t.getSemester(), t.getSection(), t.getSubject(),
                    t.getFaculty(), t.getDay(), t.getTime() });
        }
    }

    private void initComponents() {
        TitlePanel = new JPanel();
        TitleLabel = new JLabel("TIME TABLE");
        ButtonPanel = new JPanel();
        IssueSubjectButton = new JButton("Issue Subject");
        TimetableButton = new JButton("View Time Table");
        RefreshButton = new JButton("Refresh");
        SearchPanel = new JPanel();
        SearchTextField = new JTextField(40);
        SearchButton = new JButton("Search");
        TablePanel = new JPanel(new BorderLayout());
        TimeTableTable = new JTable();
        TimeTableScrollPane = new JScrollPane(TimeTableTable);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TitleLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
        TitlePanel.add(TitleLabel);

        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.add(IssueSubjectButton);
        ButtonPanel.add(TimetableButton);
        ButtonPanel.add(RefreshButton);

        SearchPanel.setLayout(new FlowLayout());
        SearchPanel.add(new JLabel("Search"));
        SearchPanel.add(SearchTextField);
        SearchPanel.add(SearchButton);

        TimeTableTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Course", "Branch", "Semester", "Section", "Subject", "Faculty Name", "Day", "Time" }) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        TablePanel.add(TimeTableScrollPane, BorderLayout.CENTER);

        IssueSubjectButton.addActionListener(this::IssueSubjectButtonActionPerformed);
        RefreshButton.addActionListener(evt -> fillTable());
        TimeTableTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TimeTableTableMousePressed(evt);
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

    private void IssueSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role == Role.ADMIN) {
            new FacultySubjectIssueForm(role).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised!!!");
        }
    }

    private void TimeTableTableMousePressed(java.awt.event.MouseEvent evt) {
        int r = TimeTableTable.getSelectedRow();
        if (r != -1) {
            String id = timeTables.get(r).getId();
            FacultySubjectIssueForm form = new FacultySubjectIssueForm(role);
            form.setVisible(true);
            form.showItemToFields(id);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FacultySubjectIssuingScreen(role).setVisible(true));
    }

    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JButton IssueSubjectButton;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JButton SearchButton;
    private javax.swing.JPanel SearchPanel;
    private javax.swing.JTextField SearchTextField;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JScrollPane TimeTableScrollPane;
    private javax.swing.JTable TimeTableTable;
    private javax.swing.JButton TimetableButton;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel TitlePanel;
}
