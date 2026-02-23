package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import models.Branch;
import models.TimeTable;
import repository.DBConnection;
import repository.DBFunctions;
import repository.RandomGenerator;

public class FacultySubjectIssueForm extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;

    Date date = new Date();
    private final Connection con = new DBConnection().connect();

    public FacultySubjectIssueForm(Role role) {
        FacultySubjectIssueForm.role = role;
        initComponents();
        customizeComponents();
    }

    private String generateIssueID() {
        return "UID" + RandomGenerator.getNumericString(5);
    }

    private void customizeComponents() {
        setLocationRelativeTo(this);
        DateChooser.setDate(date);
        UIDTextField.setText(generateIssueID());
    }

    private void clearFields() {
        UIDTextField.setText(generateIssueID());
        CourseComboBox.setSelectedItem(null);
        BranchComboBox.setSelectedItem(null);
        SemesterComboBox.setSelectedItem(null);
        DateChooser.setDate(date);
        SubjectComboBox.setSelectedItem(null);
        FacultyComboBox.setSelectedItem(null);
        SectionTextField.setText(null);
        DayComboBox.setSelectedItem(null);
        TimeTextField.setText(null);
    }

    private TimeTable retrieveData(String id) {
        TimeTable tTable = null;
        try {
            String qry = "SELECT * FROM time_table WHERE id LIKE '%" + id + "%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) {
                tTable = new TimeTable(rs.getString("id"), rs.getString("subject"), rs.getString("faculty"),
                        rs.getString("course"), rs.getString("branch"), rs.getString("semester"),
                        rs.getString("section"), rs.getString("day"), rs.getString("time"), rs.getString("timestamp"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return tTable;
    }

    public void showItemToFields(String id) {
        final TimeTable data = retrieveData(id);
        if (data == null)
            return;

        ArrayList<String> courses = DBFunctions.loadCourses();
        CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));

        ArrayList<Branch> branches = DBFunctions.loadBranches(data.getCourse());
        ArrayList<String> brArrayList = new ArrayList<>();
        for (Branch b : branches)
            brArrayList.add(b.getInit());
        BranchComboBox.setModel(new DefaultComboBoxModel<>(brArrayList.toArray(new String[0])));

        ArrayList<String> semList = DBFunctions.getSemester(data.getCourse(), data.getBranch());
        if (semList.isEmpty())
            semList.add("Select");
        SemesterComboBox.setModel(new DefaultComboBoxModel<>(semList.toArray(new String[0])));

        ArrayList<String> subjList = DBFunctions.getSubjects(data.getCourse());
        if (subjList.isEmpty())
            subjList.add("Select");
        SubjectComboBox.setModel(new DefaultComboBoxModel<>(subjList.toArray(new String[0])));

        ArrayList<String> facList = DBFunctions.getFaculty(data.getCourse());
        if (facList.isEmpty())
            facList.add("Select");
        FacultyComboBox.setModel(new DefaultComboBoxModel<>(facList.toArray(new String[0])));

        UIDTextField.setText(data.getId());
        CourseComboBox.setSelectedItem(data.getCourse());
        BranchComboBox.setSelectedItem(data.getBranch());
        SemesterComboBox.setSelectedItem(data.getSemester());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateChooser.setDate(dateFormat.parse(data.getDate()));
        } catch (ParseException e) {
            System.out.println(e);
        }

        SubjectComboBox.setSelectedItem(data.getSubject());
        FacultyComboBox.setSelectedItem(data.getFaculty());
        SectionTextField.setText(data.getSection());
        DayComboBox.setSelectedItem(data.getDay());

        String time = data.getTime();
        String timeMeridian = time.substring(time.length() - 2);
        TimeTextField.setText(time.substring(0, time.length() - 2).trim());
        TimeComboBox.setSelectedItem(timeMeridian);
    }

    private void initComponents() {
        TitlePanel = new javax.swing.JPanel();
        TitleLabel = new javax.swing.JLabel();
        BodyPanel = new javax.swing.JPanel();
        UIDTextField = new javax.swing.JTextField();
        CourseComboBox = new javax.swing.JComboBox<>();
        CourseLoadingButton = new javax.swing.JButton("Load");
        BranchComboBox = new javax.swing.JComboBox<>();
        BranchLoadingButton = new javax.swing.JButton("Load");
        SemesterComboBox = new javax.swing.JComboBox<>();
        SemesterLoadingButton = new javax.swing.JButton("Load");
        SubjectComboBox = new javax.swing.JComboBox<>();
        SubLoadingButton = new javax.swing.JButton("Load");
        FacultyComboBox = new javax.swing.JComboBox<>();
        FacultyLoadingButton = new javax.swing.JButton("Load");
        SectionTextField = new javax.swing.JTextField();
        DayComboBox = new javax.swing.JComboBox<>();
        TimeTextField = new javax.swing.JTextField();
        TimeComboBox = new javax.swing.JComboBox<>();
        DateChooser = new com.toedter.calendar.JDateChooser();

        ButtonPanel = new javax.swing.JPanel();
        SaveButton = new javax.swing.JButton("Save");
        UpdateButton = new javax.swing.JButton("Update");
        DeleteButton = new javax.swing.JButton("Delete");
        ClearButton = new javax.swing.JButton("Clear");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TitleLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 36));
        TitleLabel.setText("SUBJECT ISSUE FORM");
        TitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TitlePanel.add(TitleLabel);

        BodyPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // UID
        gbc.gridx = 0;
        gbc.gridy = 0;
        BodyPanel.add(new JLabel("UID"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(UIDTextField, gbc);
        UIDTextField.setEditable(false);

        // Course
        gbc.gridx = 0;
        gbc.gridy = 1;
        BodyPanel.add(new JLabel("Course"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(CourseComboBox, gbc);
        gbc.gridx = 2;
        BodyPanel.add(CourseLoadingButton, gbc);

        // Branch
        gbc.gridx = 0;
        gbc.gridy = 2;
        BodyPanel.add(new JLabel("Branch"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(BranchComboBox, gbc);
        gbc.gridx = 2;
        BodyPanel.add(BranchLoadingButton, gbc);

        // Semester
        gbc.gridx = 0;
        gbc.gridy = 3;
        BodyPanel.add(new JLabel("Semester"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(SemesterComboBox, gbc);
        gbc.gridx = 2;
        BodyPanel.add(SemesterLoadingButton, gbc);

        // Subject
        gbc.gridx = 0;
        gbc.gridy = 4;
        BodyPanel.add(new JLabel("Subject Name"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(SubjectComboBox, gbc);
        gbc.gridx = 2;
        BodyPanel.add(SubLoadingButton, gbc);

        // Faculty
        gbc.gridx = 0;
        gbc.gridy = 5;
        BodyPanel.add(new JLabel("Faculty Name"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(FacultyComboBox, gbc);
        gbc.gridx = 2;
        BodyPanel.add(FacultyLoadingButton, gbc);

        // Section
        gbc.gridx = 0;
        gbc.gridy = 6;
        BodyPanel.add(new JLabel("Section"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(SectionTextField, gbc);

        // Day
        gbc.gridx = 0;
        gbc.gridy = 7;
        BodyPanel.add(new JLabel("Day"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(DayComboBox, gbc);
        DayComboBox
                .setModel(new DefaultComboBoxModel<>(new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" }));

        // Time
        gbc.gridx = 0;
        gbc.gridy = 8;
        BodyPanel.add(new JLabel("Time"), gbc);
        gbc.gridx = 1;
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timePanel.add(TimeTextField);
        TimeTextField.setColumns(10);
        timePanel.add(TimeComboBox);
        TimeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "AM", "PM" }));
        BodyPanel.add(timePanel, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 9;
        BodyPanel.add(new JLabel("Date"), gbc);
        gbc.gridx = 1;
        BodyPanel.add(DateChooser, gbc);

        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.add(SaveButton);
        ButtonPanel.add(UpdateButton);
        ButtonPanel.add(DeleteButton);
        ButtonPanel.add(ClearButton);

        CourseLoadingButton.addActionListener(evt -> CourseLoadingButtonActionPerformed(null));
        BranchLoadingButton.addActionListener(evt -> BranchLoadingButtonActionPerformed(null));
        SemesterLoadingButton.addActionListener(evt -> SemesterLoadingButtonActionPerformed(null));
        SubLoadingButton.addActionListener(evt -> SubLoadingButtonActionPerformed(null));
        FacultyLoadingButton.addActionListener(evt -> FacultyLoadingButtonActionPerformed(null));
        SaveButton.addActionListener(this::SaveButtonActionPerformed);
        UpdateButton.addActionListener(this::UpdateButtonActionPerformed);
        DeleteButton.addActionListener(this::DeleteButtonActionPerformed);
        ClearButton.addActionListener(evt -> clearFields());

        getContentPane().add(TitlePanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(BodyPanel), BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);

        pack();
    }

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised!!!");
            return;
        }
        if (_checkInputs()) {
            try {
                String sql = "INSERT INTO time_table(subject, faculty, course, branch, semester, section, day, time, timestamp, id) VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, SubjectComboBox.getSelectedItem().toString());
                ps.setString(2, FacultyComboBox.getSelectedItem().toString());
                ps.setString(3, CourseComboBox.getSelectedItem().toString());
                ps.setString(4, BranchComboBox.getSelectedItem().toString());
                ps.setString(5, SemesterComboBox.getSelectedItem().toString());
                ps.setString(6, SectionTextField.getText());
                ps.setString(7, DayComboBox.getSelectedItem().toString());
                ps.setString(8, TimeTextField.getText() + " " + TimeComboBox.getSelectedItem().toString());
                ps.setString(9, new SimpleDateFormat("dd-MM-yyyy").format(DateChooser.getDate()));
                ps.setString(10, UIDTextField.getText());
                if (ps.executeUpdate() >= 1) {
                    JOptionPane.showMessageDialog(null, "Subject Issued Successfully...");
                    clearFields();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised!!!");
            return;
        }
        if (_checkInputs()) {
            try {
                String sql = "UPDATE time_table SET subject=?, faculty=?, course=?, branch=?, semester=?, section=?, day=?, time=?, timestamp=? WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, SubjectComboBox.getSelectedItem().toString());
                ps.setString(2, FacultyComboBox.getSelectedItem().toString());
                ps.setString(3, CourseComboBox.getSelectedItem().toString());
                ps.setString(4, BranchComboBox.getSelectedItem().toString());
                ps.setString(5, SemesterComboBox.getSelectedItem().toString());
                ps.setString(6, SectionTextField.getText());
                ps.setString(7, DayComboBox.getSelectedItem().toString());
                ps.setString(8, TimeTextField.getText() + " " + TimeComboBox.getSelectedItem().toString());
                ps.setString(9, new SimpleDateFormat("dd-MM-yyyy").format(DateChooser.getDate()));
                ps.setString(10, UIDTextField.getText());
                if (ps.executeUpdate() >= 1) {
                    JOptionPane.showMessageDialog(null, "Time Table Updated Successfully...");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "You Are Not Authorised!!!");
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM time_table WHERE id=?");
            ps.setString(1, UIDTextField.getText());
            if (ps.executeUpdate() >= 1) {
                JOptionPane.showMessageDialog(null, "Record Deleted...");
                clearFields();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean _checkInputs() {
        if (CourseComboBox.getSelectedItem() == null || CourseComboBox.getSelectedItem() == "Select")
            return false;
        if (BranchComboBox.getSelectedItem() == null || BranchComboBox.getSelectedItem() == "Select")
            return false;
        if (SemesterComboBox.getSelectedItem() == null || SemesterComboBox.getSelectedItem() == "Select")
            return false;
        if (SubjectComboBox.getSelectedItem() == null || SubjectComboBox.getSelectedItem() == "Select")
            return false;
        if (FacultyComboBox.getSelectedItem() == null || FacultyComboBox.getSelectedItem() == "Select")
            return false;
        if (SectionTextField.getText().isBlank())
            return false;
        return true;
    }

    private void CourseLoadingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CourseComboBox.setModel(new DefaultComboBoxModel<>(DBFunctions.loadCourses().toArray(new String[0])));
    }

    private void BranchLoadingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (CourseComboBox.getSelectedItem() != null) {
            ArrayList<Branch> branches = DBFunctions.loadBranches(CourseComboBox.getSelectedItem().toString());
            ArrayList<String> br = new ArrayList<>();
            for (Branch b : branches)
                br.add(b.getInit());
            BranchComboBox.setModel(new DefaultComboBoxModel<>(br.toArray(new String[0])));
        }
    }

    private void SemesterLoadingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (CourseComboBox.getSelectedItem() != null && BranchComboBox.getSelectedItem() != null) {
            ArrayList<String> sems = DBFunctions.getSemester(CourseComboBox.getSelectedItem().toString(),
                    BranchComboBox.getSelectedItem().toString());
            SemesterComboBox.setModel(new DefaultComboBoxModel<>(sems.toArray(new String[0])));
        }
    }

    private void SubLoadingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (CourseComboBox.getSelectedItem() != null) {
            ArrayList<String> subs = DBFunctions.getSubjects(CourseComboBox.getSelectedItem().toString());
            SubjectComboBox.setModel(new DefaultComboBoxModel<>(subs.toArray(new String[0])));
        }
    }

    private void FacultyLoadingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (CourseComboBox.getSelectedItem() != null) {
            ArrayList<String> facs = DBFunctions.getFaculty(CourseComboBox.getSelectedItem().toString());
            FacultyComboBox.setModel(new DefaultComboBoxModel<>(facs.toArray(new String[0])));
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FacultySubjectIssueForm(role).setVisible(true));
    }

    private javax.swing.JComboBox<String> BranchComboBox;
    private javax.swing.JButton BranchLoadingButton;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JButton ClearButton;
    private javax.swing.JComboBox<String> CourseComboBox;
    private javax.swing.JButton CourseLoadingButton;
    private com.toedter.calendar.JDateChooser DateChooser;
    private javax.swing.JComboBox<String> DayComboBox;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton FacultyLoadingButton;
    private javax.swing.JComboBox<String> FacultyComboBox;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTextField SectionTextField;
    private javax.swing.JButton SemLoadingButton;
    private javax.swing.JComboBox<String> SemesterComboBox;
    private javax.swing.JComboBox<String> SubjectComboBox;
    private javax.swing.JButton SubLoadingButton;
    private javax.swing.JComboBox<String> TimeComboBox;
    private javax.swing.JTextField TimeTextField;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JPanel TitlePanel;
    private javax.swing.JPanel BodyPanel;
    private javax.swing.JTextField UIDTextField;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JButton SemesterLoadingButton;
}