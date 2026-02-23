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

public class StudentEntryForm extends JFrame {

        private static final long serialVersionUID = 1L;
        static Role role;
        static String reg_no;

        String photopath = null;
        Date date = new Date();
        private final Connection con = new DBConnection().connect();

        public StudentEntryForm(Role role, String reg_no) {
                StudentEntryForm.role = role;
                StudentEntryForm.reg_no = reg_no;
                initComponents();
                customizeComponents();
        }

        private String generateAppNo() {
                return "APN" + RandomGenerator.getNumericString(5);
        }

        private String generateRegNo() {
                return "STUDENT" + RandomGenerator.getNumericString(4);
        }

        private Student retrieveData(String reg_no) {
                Student student = null;
                try {
                        String qry = "SELECT * FROM student WHERE registration_no LIKE '%" + reg_no + "%'";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(qry);
                        if (rs.next()) {
                                student = new Student(rs.getString("name"), rs.getString("roll_no"),
                                                rs.getString("application_no"),
                                                rs.getString("registration_no"), rs.getString("mother_name"),
                                                rs.getString("mother_occupation"),
                                                rs.getString("address"), rs.getString("father_name"),
                                                rs.getString("father_occupation"),
                                                rs.getString("sex"), rs.getString("dob"), rs.getString("phone"),
                                                rs.getString("email"),
                                                rs.getBytes("photo"), rs.getString("date_of_application"),
                                                rs.getString("course"),
                                                rs.getString("branch"), rs.getInt("batch"), rs.getString("semester"),
                                                rs.getInt("year_of_passing"),
                                                rs.getBoolean("hostel"), rs.getBoolean("library"),
                                                rs.getString("qualification"),
                                                rs.getString("university"), rs.getString("quota"),
                                                rs.getString("marks"), rs.getString("status"));
                        }
                } catch (Exception e) {
                        System.out.println(e);
                }
                return student;
        }

        public void showItemToFields(String reg_no) {
                final Student data = retrieveData(reg_no);
                if (data == null)
                        return;

                ArrayList<String> courses = DBFunctions.loadCourses();
                CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));

                ArrayList<Branch> branches = DBFunctions.loadBranches(data.getCourse());
                ArrayList<String> brArrayList = new ArrayList<>();
                if (branches.isEmpty()) {
                        brArrayList.add("Select");
                } else {
                        for (Branch b : branches)
                                brArrayList.add(b.getInit());
                }
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
                setTitle("STUDENT ENTRY FORM");
                DateChooser.setDate(date);
                ApplNoTextField.setText(generateAppNo());
                RegNoTextField.setText(generateRegNo());
        }

        private void clearFields() {
                ApplNoTextField.setText(generateAppNo());
                RegNoTextField.setText(generateRegNo());
                RolllNoTextField.setText(null);
                NameTextField.setText(null);
                MotherNameTextField.setText(null);
                MotherOccupationTextField.setText(null);
                FatherNameTextField.setText("");
                FatherOccupationTextField.setText(null);
                AddressTextArea.setText(null);
                PhoneTextField.setText(null);
                EmailTextField.setText(null);
                BatchYearChooser.setYear(2020);
                PassingYearChooser.setYear(2020);
                photopath = null;
                PhotoLabel.setIcon(null);
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
        }

        private boolean _checkInputFields() {
                if (NameTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Name Field Is Required!!!");
                } else if (DOBChooser.getDate() == null) {
                        JOptionPane.showMessageDialog(null, "DOB Field Is Required!!!");
                } else if (SexComboBox.getSelectedItem() == null || SexComboBox.getSelectedItem() == "Select") {
                        JOptionPane.showMessageDialog(null, "Sex Field Is Required!!!");
                } else if (MotherNameTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Mother's Name Field Is Required!!!");
                } else if (MotherOccupationTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Mother's Occupation Field Is Required!!!");
                } else if (FatherNameTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Father's Name Field Is Required!!!");
                } else if (FatherOccupationTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Father's Occupation Field Is Required!!!");
                } else if (AddressTextArea.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Address Field Is Required!!!");
                } else if (PhoneTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Phone Field Is Required!!!");
                } else if (EmailTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Email Field Is Required!!!");
                } else if (CourseComboBox.getSelectedItem() == null || CourseComboBox.getSelectedItem() == "Select") {
                        JOptionPane.showMessageDialog(null, "Course Field Is Required!!!");
                } else if (BranchComboBox.getSelectedItem() == null || BranchComboBox.getSelectedItem() == "Select") {
                        JOptionPane.showMessageDialog(null, "Branch Field Is Required!!!");
                } else if (SemesterComboBox.getSelectedItem() == null
                                || SemesterComboBox.getSelectedItem() == "Select") {
                        JOptionPane.showMessageDialog(null, "Semester Field Is Required!!!");
                } else if (PassingYearChooser.getValue() == 0) {
                        JOptionPane.showMessageDialog(null, "YOP Field Is Required!!!");
                } else if (QualificationTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Qualification Field Is Required!!!");
                } else if (UniversityTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "University Field Is Required!!!");
                } else if (MarksTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Marks Field Is Required!!!");
                } else if (QuotaComboBox.getSelectedItem() == null || QuotaComboBox.getSelectedItem() == "Select") {
                        JOptionPane.showMessageDialog(null, "Quota Field Is Required!!!");
                } else {
                        return true;
                }
                return false;
        }

        private void initComponents() {
                TitlePanel = new JPanel();
                TitleLabel = new JLabel("STUDENT ENTRY FORM");
                BodyPanel = new JPanel();

                ApplNoTextField = new JTextField(20);
                RolllNoTextField = new JTextField(20);
                RegNoTextField = new JTextField(20);
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
                CourseLoadingButton = new JButton("Load");
                BranchComboBox = new JComboBox<>(new String[] { "Select" });
                BranchLoadingButton = new JButton("Load");
                SemesterComboBox = new JComboBox<>(new String[] { "Select" });
                SemLoadingButton = new JButton("Load");
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
                PhotoChooserButton = new JButton("Select Photo");

                ButtonPanel = new JPanel();
                SaveButton = new JButton("Save");
                UpdateButton = new JButton("Update");
                DeleteButton = new JButton("Delete");
                ClearButton = new JButton("Clear");

                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                TitleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                TitlePanel.add(TitleLabel);

                BodyPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Row 0
                gbc.gridx = 0;
                gbc.gridy = 0;
                BodyPanel.add(new JLabel("Appl. No."), gbc);
                gbc.gridx = 1;
                BodyPanel.add(ApplNoTextField, gbc);
                ApplNoTextField.setEditable(false);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Roll No."), gbc);
                gbc.gridx = 3;
                BodyPanel.add(RolllNoTextField, gbc);
                RolllNoTextField.setEditable(false);
                gbc.gridx = 4;
                BodyPanel.add(new JLabel("Reg. No."), gbc);
                gbc.gridx = 5;
                BodyPanel.add(RegNoTextField, gbc);
                RegNoTextField.setEditable(false);

                // Row 1
                gbc.gridx = 0;
                gbc.gridy = 1;
                BodyPanel.add(new JLabel("Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                BodyPanel.add(NameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                BodyPanel.add(new JLabel("Sex"), gbc);
                gbc.gridx = 4;
                BodyPanel.add(SexComboBox, gbc);
                gbc.gridx = 5;
                BodyPanel.add(new JLabel("D.O.B"), gbc);
                gbc.gridx = 6;
                BodyPanel.add(DOBChooser, gbc);

                // Row 2
                gbc.gridx = 0;
                gbc.gridy = 2;
                BodyPanel.add(new JLabel("Mother's Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                BodyPanel.add(MotherNameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                BodyPanel.add(new JLabel("Occupation"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                BodyPanel.add(MotherOccupationTextField, gbc);
                gbc.gridwidth = 1;

                // Row 3
                gbc.gridx = 0;
                gbc.gridy = 3;
                BodyPanel.add(new JLabel("Father's Name"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                BodyPanel.add(FatherNameTextField, gbc);
                gbc.gridwidth = 1;
                gbc.gridx = 3;
                BodyPanel.add(new JLabel("Occupation"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                BodyPanel.add(FatherOccupationTextField, gbc);
                gbc.gridwidth = 1;

                // Row 4
                gbc.gridx = 0;
                gbc.gridy = 4;
                BodyPanel.add(new JLabel("Address"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 2;
                gbc.gridheight = 2;
                BodyPanel.add(new JScrollPane(AddressTextArea), gbc);
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                gbc.gridx = 3;
                BodyPanel.add(new JLabel("Phone"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                BodyPanel.add(PhoneTextField, gbc);
                gbc.gridwidth = 1;

                // Row 5
                gbc.gridx = 3;
                gbc.gridy = 5;
                BodyPanel.add(new JLabel("Email"), gbc);
                gbc.gridx = 4;
                gbc.gridwidth = 2;
                BodyPanel.add(EmailTextField, gbc);
                gbc.gridwidth = 1;

                // Row 6
                gbc.gridx = 0;
                gbc.gridy = 6;
                BodyPanel.add(new JLabel("Course"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(CourseComboBox, gbc);
                gbc.gridx = 2;
                BodyPanel.add(CourseLoadingButton, gbc);
                gbc.gridx = 3;
                BodyPanel.add(new JLabel("Branch"), gbc);
                gbc.gridx = 4;
                BodyPanel.add(BranchComboBox, gbc);
                gbc.gridx = 5;
                BodyPanel.add(BranchLoadingButton, gbc);

                // Row 7
                gbc.gridx = 0;
                gbc.gridy = 7;
                BodyPanel.add(new JLabel("Semester"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(SemesterComboBox, gbc);
                gbc.gridx = 2;
                BodyPanel.add(SemLoadingButton, gbc);
                gbc.gridx = 3;
                BodyPanel.add(HostelCheckBox, gbc);
                gbc.gridx = 4;
                BodyPanel.add(LibraryCheckBox, gbc);

                // Row 8
                gbc.gridx = 0;
                gbc.gridy = 8;
                BodyPanel.add(new JLabel("Batch"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(BatchYearChooser, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Y.O.P"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(PassingYearChooser, gbc);
                gbc.gridx = 4;
                BodyPanel.add(new JLabel("Date"), gbc);
                gbc.gridx = 5;
                BodyPanel.add(DateChooser, gbc);

                // Row 9
                gbc.gridx = 0;
                gbc.gridy = 9;
                BodyPanel.add(new JLabel("Quota"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(QuotaComboBox, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Marks(%)"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(MarksTextField, gbc);
                gbc.gridx = 4;
                BodyPanel.add(new JLabel("Qualification"), gbc);
                gbc.gridx = 5;
                BodyPanel.add(QualificationTextField, gbc);

                // Row 10
                gbc.gridx = 0;
                gbc.gridy = 10;
                BodyPanel.add(new JLabel("University"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(UniversityTextField, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Status"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(StatusComboBox, gbc);

                // Photo
                gbc.gridx = 6;
                gbc.gridy = 6;
                gbc.gridheight = 4;
                PhotoLabel.setPreferredSize(new java.awt.Dimension(150, 150));
                PhotoLabel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
                BodyPanel.add(PhotoLabel, gbc);
                gbc.gridy = 10;
                gbc.gridheight = 1;
                BodyPanel.add(PhotoChooserButton, gbc);

                PhotoChooserButton.addActionListener(this::PhotoChooserButtonActionPerformed);
                CourseLoadingButton.addActionListener(evt -> CourseLoadingButtonMousePressed(null));
                BranchLoadingButton.addActionListener(evt -> BranchLoadingButtonMousePressed(null));
                SemLoadingButton.addActionListener(evt -> SemLoadingButtonMousePressed(null));
                SaveButton.addActionListener(this::SaveButtonActionPerformed);
                UpdateButton.addActionListener(this::UpdateButtonActionPerformed);
                DeleteButton.addActionListener(this::DeleteButtonActionPerformed);
                ClearButton.addActionListener(evt -> clearFields());

                ButtonPanel.setLayout(new FlowLayout());
                ButtonPanel.add(SaveButton);
                ButtonPanel.add(UpdateButton);
                ButtonPanel.add(DeleteButton);
                ButtonPanel.add(ClearButton);

                getContentPane().add(TitlePanel, BorderLayout.NORTH);
                getContentPane().add(new JScrollPane(BodyPanel), BorderLayout.CENTER);
                getContentPane().add(ButtonPanel, BorderLayout.SOUTH);

                pack();
                setSize(1200, 800);
        }

        private void PhotoChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("Images", "jpg", "png", "jpeg");
                chooser.addChoosableFileFilter(fnef);
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File selectedPhoto = chooser.getSelectedFile();
                        photopath = selectedPhoto.getAbsolutePath();
                        PhotoLabel.setIcon(UtilFunctions.resizeImage(photopath, null, PhotoLabel));
                }
        }

        private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (_checkInputFields()) {
                        if (JOptionPane.showConfirmDialog(null, "Save Student Data?", "Confirm", 0, 3) == 0) {
                                _saveStudentData();
                        }
                }
        }

        private void _saveStudentData() {
                if (photopath == null) {
                        JOptionPane.showMessageDialog(null, "Photo Is Required!!!");
                        return;
                }
                final String sql = "INSERT INTO student(name, roll_no, application_no, registration_no, mother_name, mother_occupation, address, father_name, father_occupation, sex, dob, phone, email, photo, password, date_of_application, course, branch, batch, semester, year_of_passing, hostel, library, qualification, university, quota, marks) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
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
                                JOptionPane.showMessageDialog(null, "Student data saved.");
                                clearFields();
                        }
                } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, "Insertion Failed", "Error", 0);
                }
        }

        private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (_checkInputFields()) {
                        if (JOptionPane.showConfirmDialog(null, "Update Student Data?", "Confirm", 0, 3) == 0) {
                                _updateStudentData();
                        }
                }
        }

        private void _updateStudentData() {
                String sql;
                if (photopath == null) {
                        sql = "UPDATE student SET name=?, mother_name=?, mother_occupation=?, address=?, father_name=?, father_occupation=?, sex=?, dob=?, phone=?, email=?, password=?, date_of_application=?, course=?, branch=?, batch=?, semester=?, year_of_passing=?, hostel=?, library=?, qualification=?, university=?, quota=?, marks=?, status=? WHERE registration_no=?";
                } else {
                        sql = "UPDATE student SET name=?, mother_name=?, mother_occupation=?, address=?, father_name=?, father_occupation=?, sex=?, dob=?, phone=?, email=?, photo=?, password=?, date_of_application=?, course=?, branch=?, batch=?, semester=?, year_of_passing=?, hostel=?, library=?, qualification=?, university=?, quota=?, marks=?, status=? WHERE registration_no=?";
                }
                try {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, NameTextField.getText());
                        ps.setString(2, MotherNameTextField.getText());
                        ps.setString(3, MotherOccupationTextField.getText());
                        ps.setString(4, AddressTextArea.getText());
                        ps.setString(5, FatherNameTextField.getText());
                        ps.setString(6, FatherOccupationTextField.getText());
                        ps.setString(7, SexComboBox.getSelectedItem().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        ps.setString(8, sdf.format(DOBChooser.getDate()));
                        ps.setString(9, PhoneTextField.getText());
                        ps.setString(10, EmailTextField.getText());
                        int i = 11;
                        if (photopath != null) {
                                ps.setBlob(i++, new FileInputStream(new File(photopath)));
                        }
                        ps.setString(i++, new SimpleDateFormat("ddMMyyyy").format(DOBChooser.getDate()));
                        ps.setString(i++, sdf.format(DateChooser.getDate()));
                        ps.setString(i++, CourseComboBox.getSelectedItem().toString());
                        ps.setString(i++, BranchComboBox.getSelectedItem().toString());
                        ps.setInt(i++, BatchYearChooser.getYear());
                        ps.setString(i++, SemesterComboBox.getSelectedItem().toString());
                        ps.setInt(i++, PassingYearChooser.getYear());
                        ps.setBoolean(i++, HostelCheckBox.isSelected());
                        ps.setBoolean(i++, LibraryCheckBox.isSelected());
                        ps.setString(i++, QualificationTextField.getText());
                        ps.setString(i++, UniversityTextField.getText());
                        ps.setString(i++, QuotaComboBox.getSelectedItem().toString());
                        ps.setDouble(i++, Double.parseDouble(MarksTextField.getText()));
                        ps.setString(i++, StatusComboBox.getSelectedItem().toString());
                        ps.setString(i++, RegNoTextField.getText());

                        if (ps.executeUpdate() >= 1) {
                                JOptionPane.showMessageDialog(null, "Student data updated.");
                        }
                } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(null, "Update Failed", "Error", 0);
                }
        }

        private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (JOptionPane.showConfirmDialog(null, "Delete Student Data?", "Confirm", 0, 3) == 0) {
                        try {
                                PreparedStatement ps = con
                                                .prepareStatement("DELETE FROM student WHERE registration_no=?");
                                ps.setString(1, RegNoTextField.getText());
                                if (ps.executeUpdate() >= 1) {
                                        JOptionPane.showMessageDialog(null, "Student data deleted.");
                                        clearFields();
                                }
                        } catch (Exception e) {
                                System.out.println(e);
                        }
                }
        }

        private void CourseLoadingButtonMousePressed(java.awt.event.ActionEvent evt) {
                ArrayList<String> courses = DBFunctions.loadCourses();
                CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));
        }

        private void BranchLoadingButtonMousePressed(java.awt.event.ActionEvent evt) {
                if (CourseComboBox.getSelectedItem() != null) {
                        ArrayList<Branch> branches = DBFunctions
                                        .loadBranches(CourseComboBox.getSelectedItem().toString());
                        ArrayList<String> br = new ArrayList<>();
                        if (branches.isEmpty())
                                br.add("Select");
                        else
                                for (Branch b : branches)
                                        br.add(b.getInit());
                        BranchComboBox.setModel(new DefaultComboBoxModel<>(br.toArray(new String[0])));
                }
        }

        private void SemLoadingButtonMousePressed(java.awt.event.ActionEvent evt) {
                if (CourseComboBox.getSelectedItem() != null && BranchComboBox.getSelectedItem() != null) {
                        ArrayList<String> sems = DBFunctions.getSemester(CourseComboBox.getSelectedItem().toString(),
                                        BranchComboBox.getSelectedItem().toString());
                        if (sems.isEmpty())
                                sems.add("Select");
                        SemesterComboBox.setModel(new DefaultComboBoxModel<>(sems.toArray(new String[0])));
                }
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new StudentEntryForm(role, reg_no).setVisible(true));
        }

        private JTextArea AddressTextArea;
        private JTextField ApplNoTextField;
        private JYearChooser BatchYearChooser;
        private JPanel BodyPanel;
        private JComboBox<String> BranchComboBox;
        private JButton BranchLoadingButton;
        private JPanel ButtonPanel;
        private JButton ClearButton;
        private JComboBox<String> CourseComboBox;
        private JButton CourseLoadingButton;
        private JDateChooser DOBChooser;
        private JDateChooser DateChooser;
        private JButton DeleteButton;
        private JTextField EmailTextField;
        private JTextField FatherNameTextField;
        private JTextField FatherOccupationTextField;
        private JCheckBox HostelCheckBox;
        private JCheckBox LibraryCheckBox;
        private JTextField MarksTextField;
        private JTextField MotherNameTextField;
        private JTextField MotherOccupationTextField;
        private JTextField NameTextField;
        private JYearChooser PassingYearChooser;
        private JTextField PhoneTextField;
        private JButton PhotoChooserButton;
        private JLabel PhotoLabel;
        private JTextField QualificationTextField;
        private JComboBox<String> QuotaComboBox;
        private JTextField RegNoTextField;
        private JTextField RolllNoTextField;
        private JButton SaveButton;
        private JButton SemLoadingButton;
        private JComboBox<String> SemesterComboBox;
        private JComboBox<String> SexComboBox;
        private JComboBox<String> StatusComboBox;
        private JLabel TitleLabel;
        private JPanel TitlePanel;
        private JTextField UniversityTextField;
        private JButton UpdateButton;
}
