package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;
import models.Branch;
import models.Student;
import repository.DBConnection;
import repository.DBFunctions;
import repository.RandomGenerator;
import repository.UtilFunctions;

public class StudentApplicationForm extends JFrame {

        private static final long serialVersionUID = 1L;
        static Role role;
        String photopath = null;
        Date date = new Date();
        private final Connection con = new DBConnection().connect();

        public StudentApplicationForm(Role role) {
                StudentApplicationForm.role = role;
                initComponents();
                customizeComponents();
        }

        private String generateAppNo() {
                return "APN" + RandomGenerator.getNumericString(5);
        }

        private String generateRegNo() {
                return "STUDENT" + RandomGenerator.getNumericString(4);
        }

        private Student retrieveData(String roll_no) {
                Student student = null;
                try {
                        String qry = "SELECT * FROM student WHERE roll_no='" + roll_no + "'";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(qry);
                        if (rs.next()) {
                                student = new Student(rs.getString("name"), rs.getString("roll_no"),
                                                rs.getString("application_no"), rs.getString("registration_no"),
                                                rs.getString("mother_name"),
                                                rs.getString("mother_occupation"), rs.getString("address"),
                                                rs.getString("father_name"),
                                                rs.getString("father_occupation"), rs.getString("sex"),
                                                rs.getString("dob"), rs.getString("phone"),
                                                rs.getString("email"), rs.getBytes("photo"),
                                                rs.getString("date_of_application"),
                                                rs.getString("course"), rs.getString("branch"), rs.getInt("batch"),
                                                rs.getString("semester"),
                                                rs.getInt("year_of_passing"), rs.getBoolean("hostel"),
                                                rs.getBoolean("library"),
                                                rs.getString("qualification"), rs.getString("university"),
                                                rs.getString("quota"),
                                                rs.getString("marks"), rs.getString("status"));
                        }
                } catch (Exception e) {
                        System.out.println(e);
                }
                return student;
        }

        public void showItemToFields(int index, String roll_no) {
                final Student data = retrieveData(roll_no);
                if (data == null)
                        return;

                ArrayList<String> courses = DBFunctions.loadCourses();
                CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));

                ArrayList<Branch> branches = DBFunctions.loadBranches(data.getCourse());
                ArrayList<String> brArrayList = new ArrayList<>();
                if (branches.isEmpty())
                        brArrayList.add("Select");
                else
                        for (Branch b : branches)
                                brArrayList.add(b.getInit());
                BranchComboBox.setModel(new DefaultComboBoxModel<>(brArrayList.toArray(new String[0])));

                ArrayList<String> semList = DBFunctions.getSemester(data.getCourse(), data.getBranch());
                if (semList.isEmpty())
                        semList.add("Select");
                SemesterComboBox.setModel(new DefaultComboBoxModel<>(semList.toArray(new String[0])));

                ApplNoTextField.setText(data.getApplicationNo());
                RolllNoTextField.setText(data.getRollNo());
                RegNoTextField.setText(data.getRegNo());
                NameTextField.setText(data.getName());
                SexComboBox.setSelectedItem(data.getSex());

                try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        DOBChooser.setDate(sdf.parse(data.getDob()));
                        DateChooser.setDate(sdf.parse(data.getDate_of_application()));
                } catch (ParseException e) {
                        System.out.println(e);
                }

                MotherNameTextField.setText(data.getMother_name());
                MotherOccupationTextField.setText(data.getMother_occupation());
                FatherNameTextField.setText(data.getFather_name());
                FatherOccupationTextField.setText(data.getFather_occupation());
                AddressTextArea.setText(data.getAddress());
                PhoneTextField.setText(data.getPhone());
                EmailTextField.setText(data.getEmail());
                CourseComboBox.setSelectedItem(data.getCourse());
                BranchComboBox.setSelectedItem(data.getBranch());
                SemesterComboBox.setSelectedItem(data.getSemester());
                BatchYearChooser.setYear(data.getBatch());
                PassingYearChooser.setYear(data.getPassing_year());
                HostelCheckBox.setSelected(data.isIsHostler());
                LibraryCheckBox.setSelected(data.isLibraryFacility());
                QualificationTextField.setText(data.getQualification());
                UniversityTextField.setText(data.getUniversity());
                QuotaComboBox.setSelectedItem(data.getQuota());
                MarksTextField.setText(data.getMarks());
                StatusComboBox.setSelectedItem(data.getStatus());

                PhotoLabel.setIcon(UtilFunctions.resizeImage(null, data.getPhoto(), PhotoLabel));
        }

        private void customizeComponents() {
                setLocationRelativeTo(this);
                DateChooser.setDate(date);
                ApplNoTextField.setText(generateAppNo());
                RegNoTextField.setText(generateRegNo());
                StatusComboBox.setEnabled(false);
        }

        private void clearFields() {
                ApplNoTextField.setText(generateAppNo());
                RegNoTextField.setText(generateRegNo());
                RolllNoTextField.setText(null);
                NameTextField.setText(null);
                MotherNameTextField.setText(null);
                MotherOccupationTextField.setText(null);
                FatherNameTextField.setText(null);
                FatherOccupationTextField.setText(null);
                AddressTextArea.setText(null);
                PhoneTextField.setText(null);
                EmailTextField.setText(null);
                BatchYearChooser.setYear(2020);
                PassingYearChooser.setYear(2020);
                DateChooser.setDate(date);
                QualificationTextField.setText(null);
                UniversityTextField.setText(null);
                MarksTextField.setText(null);
                SexComboBox.setSelectedItem("Select");
                DOBChooser.setDate(null);
                CourseComboBox.setSelectedItem("Select");
                BranchComboBox.setSelectedItem("Select");
                SemesterComboBox.setSelectedItem("Select");
                QuotaComboBox.setSelectedItem("Select");
                StatusComboBox.setSelectedItem("SELECT");
                photopath = null;
                PhotoLabel.setIcon(null);
        }

        private boolean checkInputs() {
                if (NameTextField.getText().isBlank() || DOBChooser.getDate() == null) {
                        JOptionPane.showMessageDialog(null, "Name and DOB are Required!");
                        return false;
                }
                return true;
        }

        private void initComponents() {
                setTitle("STUDENT APPLICATION FORM");
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setSize(1200, 800);
                setResizable(false);

                JPanel mainPanel = new JPanel(new BorderLayout());

                JPanel titlePanel = new JPanel();
                JLabel titleLabel = new JLabel("STUDENT APPLICATION FORM");
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                titlePanel.add(titleLabel);
                mainPanel.add(titlePanel, BorderLayout.NORTH);

                JPanel bodyPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                ApplNoTextField = new JTextField(20);
                ApplNoTextField.setEditable(false);
                RolllNoTextField = new JTextField(20);
                RolllNoTextField.setEditable(false);
                RegNoTextField = new JTextField(20);
                RegNoTextField.setEditable(false);
                NameTextField = new JTextField(20);
                SexComboBox = new JComboBox<>(new String[] { "Select", "Male", "Female", "Other" });
                DOBChooser = new JDateChooser();
                MotherNameTextField = new JTextField(20);
                MotherOccupationTextField = new JTextField(20);
                FatherNameTextField = new JTextField(20);
                FatherOccupationTextField = new JTextField(20);
                AddressTextArea = new JTextArea(3, 20);
                PhoneTextField = new JTextField(20);
                EmailTextField = new JTextField(20);
                CourseComboBox = new JComboBox<>(new String[] { "Select" });
                BranchComboBox = new JComboBox<>(new String[] { "Select" });
                SemesterComboBox = new JComboBox<>(new String[] { "Select" });
                HostelCheckBox = new JCheckBox("Hostel");
                LibraryCheckBox = new JCheckBox("Library");
                BatchYearChooser = new JYearChooser();
                PassingYearChooser = new JYearChooser();
                DateChooser = new JDateChooser();
                QuotaComboBox = new JComboBox<>(
                                new String[] { "Select", "Entrance Exam", "Academic Marks", "Management Quota" });
                MarksTextField = new JTextField(20);
                QualificationTextField = new JTextField(20);
                UniversityTextField = new JTextField(20);
                StatusComboBox = new JComboBox<>(new String[] { "SELECT", "CONFIRM", "PENDING", "CANCELLED" });
                PhotoLabel = new JLabel();
                PhotoLabel.setPreferredSize(new java.awt.Dimension(150, 150));
                PhotoLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                JButton photoBtn = new JButton("Select Photo");

                // Layout rows
                gbc.gridx = 0;
                gbc.gridy = 0;
                bodyPanel.add(new JLabel("Appl. No."), gbc);
                gbc.gridx = 1;
                bodyPanel.add(ApplNoTextField, gbc);
                gbc.gridx = 2;
                bodyPanel.add(new JLabel("Roll No."), gbc);
                gbc.gridx = 3;
                bodyPanel.add(RolllNoTextField, gbc);
                gbc.gridx = 4;
                bodyPanel.add(new JLabel("Reg. No."), gbc);
                gbc.gridx = 5;
                bodyPanel.add(RegNoTextField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                bodyPanel.add(new JLabel("Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                bodyPanel.add(NameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                bodyPanel.add(new JLabel("Sex"), gbc);
                gbc.gridx = 4;
                bodyPanel.add(SexComboBox, gbc);
                gbc.gridx = 5;
                bodyPanel.add(new JLabel("D.O.B"), gbc);
                gbc.gridx = 6;
                bodyPanel.add(DOBChooser, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                bodyPanel.add(new JLabel("Mother's Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                bodyPanel.add(MotherNameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                bodyPanel.add(new JLabel("Occupation"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                bodyPanel.add(MotherOccupationTextField, gbc);
                gbc.gridwidth = 1;

                gbc.gridx = 0;
                gbc.gridy = 3;
                bodyPanel.add(new JLabel("Father's Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                bodyPanel.add(FatherNameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                bodyPanel.add(new JLabel("Occupation"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                bodyPanel.add(FatherOccupationTextField, gbc);
                gbc.gridwidth = 1;

                gbc.gridx = 0;
                gbc.gridy = 4;
                bodyPanel.add(new JLabel("Address"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                gbc.gridheight = 2;
                bodyPanel.add(new JScrollPane(AddressTextArea), gbc);
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                gbc.gridx = 3;
                bodyPanel.add(new JLabel("Phone"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                bodyPanel.add(PhoneTextField, gbc);
                gbc.gridwidth = 1;

                gbc.gridx = 3;
                gbc.gridy = 5;
                bodyPanel.add(new JLabel("Email"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                bodyPanel.add(EmailTextField, gbc);
                gbc.gridwidth = 1;

                gbc.gridx = 0;
                gbc.gridy = 6;
                bodyPanel.add(new JLabel("Course"), gbc);
                gbc.gridx = 1;
                bodyPanel.add(CourseComboBox, gbc);
                JButton courseLoad = new JButton("Load");
                courseLoad.addActionListener(e -> CourseComboBox.setModel(
                                new DefaultComboBoxModel<>(DBFunctions.loadCourses().toArray(new String[0]))));
                gbc.gridx = 2;
                bodyPanel.add(courseLoad, gbc);
                gbc.gridx = 3;
                bodyPanel.add(new JLabel("Branch"), gbc);
                gbc.gridx = 4;
                bodyPanel.add(BranchComboBox, gbc);
                JButton branchLoad = new JButton("Load");
                branchLoad.addActionListener(e -> {
                        if (CourseComboBox.getSelectedItem() != null) {
                                ArrayList<Branch> brs = DBFunctions
                                                .loadBranches(CourseComboBox.getSelectedItem().toString());
                                ArrayList<String> names = new ArrayList<>();
                                for (Branch b : brs)
                                        names.add(b.getInit());
                                BranchComboBox.setModel(new DefaultComboBoxModel<>(names.toArray(new String[0])));
                        }
                });
                gbc.gridx = 5;
                bodyPanel.add(branchLoad, gbc);

                gbc.gridx = 0;
                gbc.gridy = 7;
                bodyPanel.add(new JLabel("Semester"), gbc);
                gbc.gridx = 1;
                bodyPanel.add(SemesterComboBox, gbc);
                JButton semLoad = new JButton("Load");
                semLoad.addActionListener(e -> {
                        if (CourseComboBox.getSelectedItem() != null && BranchComboBox.getSelectedItem() != null) {
                                ArrayList<String> sems = DBFunctions.getSemester(
                                                CourseComboBox.getSelectedItem().toString(),
                                                BranchComboBox.getSelectedItem().toString());
                                SemesterComboBox.setModel(new DefaultComboBoxModel<>(sems.toArray(new String[0])));
                        }
                });
                gbc.gridx = 2;
                bodyPanel.add(semLoad, gbc);
                gbc.gridx = 3;
                bodyPanel.add(HostelCheckBox, gbc);
                gbc.gridx = 4;
                bodyPanel.add(LibraryCheckBox, gbc);

                gbc.gridx = 0;
                gbc.gridy = 8;
                bodyPanel.add(new JLabel("Batch"), gbc);
                gbc.gridx = 1;
                bodyPanel.add(BatchYearChooser, gbc);
                gbc.gridx = 2;
                bodyPanel.add(new JLabel("Y.O.P"), gbc);
                gbc.gridx = 3;
                bodyPanel.add(PassingYearChooser, gbc);
                gbc.gridx = 4;
                bodyPanel.add(new JLabel("Date"), gbc);
                gbc.gridx = 5;
                bodyPanel.add(DateChooser, gbc);

                gbc.gridx = 0;
                gbc.gridy = 9;
                bodyPanel.add(new JLabel("Quota"), gbc);
                gbc.gridx = 1;
                bodyPanel.add(QuotaComboBox, gbc);
                gbc.gridx = 2;
                bodyPanel.add(new JLabel("Marks(%)"), gbc);
                gbc.gridx = 3;
                bodyPanel.add(MarksTextField, gbc);
                gbc.gridx = 4;
                bodyPanel.add(new JLabel("Qualification"), gbc);
                gbc.gridx = 5;
                bodyPanel.add(QualificationTextField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 10;
                bodyPanel.add(new JLabel("University"), gbc);
                gbc.gridx = 1;
                bodyPanel.add(UniversityTextField, gbc);
                gbc.gridx = 2;
                bodyPanel.add(new JLabel("Status"), gbc);
                gbc.gridx = 3;
                bodyPanel.add(StatusComboBox, gbc);

                gbc.gridx = 6;
                gbc.gridy = 0;
                gbc.gridheight = 5;
                bodyPanel.add(PhotoLabel, gbc);
                gbc.gridy = 5;
                gbc.gridheight = 1;
                bodyPanel.add(photoBtn, gbc);
                photoBtn.addActionListener(e -> selectPhoto());

                mainPanel.add(new JScrollPane(bodyPanel), BorderLayout.CENTER);

                JPanel btnPanel = new JPanel(new FlowLayout());
                JButton saveBtn = new JButton("Save");
                saveBtn.addActionListener(this::SaveButtonActionPerformed);
                JButton clearBtn = new JButton("Clear");
                clearBtn.addActionListener(e -> clearFields());
                btnPanel.add(saveBtn);
                btnPanel.add(clearBtn);
                mainPanel.add(btnPanel, BorderLayout.SOUTH);

                add(mainPanel);
                setLocationRelativeTo(null);
        }

        private void selectPhoto() {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        photopath = chooser.getSelectedFile().getAbsolutePath();
                        PhotoLabel.setIcon(UtilFunctions.resizeImage(photopath, null, PhotoLabel));
                }
        }

        private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (checkInputs()) {
                        if (JOptionPane.showConfirmDialog(null, "Submit application?", "Confirm",
                                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                saveStudentData();
                        }
                }
        }

        private void saveStudentData() {
                if (photopath == null) {
                        JOptionPane.showMessageDialog(null, "Photo Required!");
                        return;
                }
                try {
                        String sql = "INSERT INTO student(name, roll_no, application_no, registration_no, mother_name, mother_occupation, address, father_name, father_occupation, sex, dob, phone, email, photo, password, date_of_application, course, branch, batch, semester, year_of_passing, hostel, library, qualification, university, quota, marks) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, NameTextField.getText());
                        int batch_year = BatchYearChooser.getYear() % 100;
                        String roll_no = batch_year + RandomGenerator.getNumericString(5);
                        ps.setString(2, roll_no);
                        ps.setString(3, ApplNoTextField.getText());
                        ps.setString(4, RegNoTextField.getText());
                        ps.setString(5, MotherNameTextField.getText());
                        ps.setString(6, MotherOccupationTextField.getText());
                        ps.setString(7, AddressTextArea.getText());
                        ps.setString(8, FatherNameTextField.getText());
                        ps.setString(9, FatherOccupationTextField.getText());
                        ps.setString(10, SexComboBox.getSelectedItem().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        ps.setString(11, sdf.format(DOBChooser.getDate()));
                        ps.setString(12, PhoneTextField.getText());
                        ps.setString(13, EmailTextField.getText());
                        ps.setBlob(14, new FileInputStream(new File(photopath)));
                        ps.setString(15, new SimpleDateFormat("ddMMyyyy").format(DOBChooser.getDate()));
                        ps.setString(16, sdf.format(DateChooser.getDate()));
                        ps.setString(17, CourseComboBox.getSelectedItem().toString());
                        ps.setString(18, BranchComboBox.getSelectedItem().toString());
                        ps.setInt(19, BatchYearChooser.getYear());
                        ps.setString(20, SemesterComboBox.getSelectedItem().toString());
                        ps.setInt(21, PassingYearChooser.getYear());
                        ps.setBoolean(22, HostelCheckBox.isSelected());
                        ps.setBoolean(23, LibraryCheckBox.isSelected());
                        ps.setString(24, QualificationTextField.getText());
                        ps.setString(25, UniversityTextField.getText());
                        ps.setString(26, QuotaComboBox.getSelectedItem().toString());
                        ps.setDouble(27, Double.parseDouble(MarksTextField.getText()));

                        if (ps.executeUpdate() >= 1) {
                                JOptionPane.showMessageDialog(null, "Submitted! Reg No: " + RegNoTextField.getText());
                                clearFields();
                        }
                } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, "Failed: " + e.getMessage());
                }
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new StudentApplicationForm(role).setVisible(true));
        }

        private JTextField ApplNoTextField, RolllNoTextField, RegNoTextField, NameTextField, MotherNameTextField,
                        MotherOccupationTextField, FatherNameTextField, FatherOccupationTextField, PhoneTextField,
                        EmailTextField, MarksTextField, QualificationTextField, UniversityTextField;
        private JTextArea AddressTextArea;
        private JComboBox<String> SexComboBox, CourseComboBox, BranchComboBox, SemesterComboBox, QuotaComboBox,
                        StatusComboBox;
        private JDateChooser DOBChooser, DateChooser;
        private JYearChooser BatchYearChooser, PassingYearChooser;
        private JCheckBox HostelCheckBox, LibraryCheckBox;
        private JLabel PhotoLabel;
}
