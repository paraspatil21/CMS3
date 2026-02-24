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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;
import models.Branch;
import models.Faculty;
import repository.DBConnection;
import repository.DBFunctions;
import repository.RandomGenerator;
import repository.UtilFunctions;

public class FacultyEntryForm extends JFrame {

        private static final long serialVersionUID = 1L;
        static Role role;
        String photopath = null;
        Date date = new Date();
        private final Connection con = new DBConnection().connect();

        public FacultyEntryForm(Role role) {
                FacultyEntryForm.role = role;
                initComponents();
                customizeComponents();
        }

        private String generateAppNo() {
                return "FACULTY" + RandomGenerator.getNumericString(4);
        }

        private void customizeComponents() {
                setLocationRelativeTo(this);
                DateChooser.setDate(date);
                RegNoTextField.setText(generateAppNo());
        }

        private void clearFields() {
                RegNoTextField.setText(generateAppNo());
                NameTextField.setText("");
                DOBChooser.setDate(null);
                DOJChooser.setDate(null);
                FatherNameTextField.setText("");
                AddressTextArea.setText("");
                PhoneTextField.setText("");
                EmailTextField.setText("");
                DateChooser.setDate(date);
                PasswordTextField.setText("");
                QualificationTextField.setText("");
                InstitutionTextField.setText("");
                ExperienceSpinner.setValue(0);
                AccountNoTextField.setText("");
                SalaryTextField.setText("");
                photopath = null;
                PhotoLabel.setIcon(null);
        }

        private Faculty retrieveData(String reg_no) {
                Faculty faculty = null;
                try {
                        String qry = "SELECT * FROM faculty WHERE registration_no LIKE '%" + reg_no + "%'";
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery(qry);
                        if (rs.next()) {
                                faculty = new Faculty(rs.getString("registration_no"), rs.getString("name"),
                                                rs.getString("father_name"), rs.getString("sex"), rs.getString("dob"),
                                                rs.getString("email"),
                                                rs.getString("phone"), rs.getString("password"),
                                                rs.getString("address"), rs.getBytes("photo"),
                                                rs.getString("qualifications"), rs.getString("institution"),
                                                rs.getString("designation"),
                                                rs.getInt("experience"), rs.getString("course"),
                                                rs.getString("department"),
                                                rs.getString("date_joined"), rs.getString("date_updated"));
                        }
                } catch (Exception e) {
                        System.out.println(e);
                }
                return faculty;
        }

        public void showItemToFields(String reg_no) {
                final Faculty data = retrieveData(reg_no);
                if (data == null)
                        return;

                ArrayList<String> courses = DBFunctions.loadCourses();
                CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));

                ArrayList<Branch> branches = DBFunctions.loadBranches(data.getCourse());
                ArrayList<String> brArrayList = new ArrayList<>();
                for (Branch b : branches)
                        brArrayList.add(b.getInit());
                DepartmentComboBox.setModel(new DefaultComboBoxModel<>(brArrayList.toArray(new String[0])));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                RegNoTextField.setText(data.getRegNo());
                NameTextField.setText(data.getName());
                FatherNameTextField.setText(data.getFather_name());
                SexComboBox.setSelectedItem(data.getSex());

                try {
                        DateChooser.setDate(sdf.parse(data.getDate_updated()));
                        DOBChooser.setDate(sdf.parse(data.getDob()));
                        DOJChooser.setDate(sdf.parse(data.getDate_joined()));
                } catch (ParseException e) {
                        System.out.println("Error in showItemToFields");
                }
                AddressTextArea.setText(data.getAddress());
                PhoneTextField.setText(data.getPhone());
                EmailTextField.setText(data.getEmail());
                DesignationComboBox.setSelectedItem(data.getDesignation());
                CourseComboBox.setSelectedItem(data.getCourse());
                DepartmentComboBox.setSelectedItem(data.getDepartment());
                ExperienceSpinner.setValue(data.getExperience());
                PhotoLabel.setIcon(UtilFunctions.resizeImage(null, data.getPhoto(), PhotoLabel));
                PasswordTextField.setText(data.getPassword());
                QualificationTextField.setText(data.getQualifications());
                InstitutionTextField.setText(data.getInstitution());
        }

        private void initComponents() {
                TitlePanel = new JPanel();
                TitleLabel = new JLabel("FACULTY ENTRY FORM");
                BodyPanel = new JPanel();

                RegNoTextField = new JTextField(20);
                DateChooser = new JDateChooser();
                NameTextField = new JTextField(20);
                DOBChooser = new JDateChooser();
                SexComboBox = new JComboBox<>(new String[] { "Select", "Male", "Female", "Other" });
                FatherNameTextField = new JTextField(20);
                AddressTextArea = new JTextArea(3, 20);
                PhoneTextField = new JTextField(20);
                EmailTextField = new JTextField(20);
                DesignationComboBox = new JComboBox<>(new String[] { "Select", "Assistant Professor",
                                "Associate Professor", "Head of Department", "Principal", "Dean" });
                QualificationTextField = new JTextField(20);
                InstitutionTextField = new JTextField(20);
                PasswordTextField = new JTextField(20);
                ExperienceSpinner = new JSpinner();
                DOJChooser = new JDateChooser();
                CourseComboBox = new JComboBox<>(new String[] { "Select" });
                DepartmentComboBox = new JComboBox<>(new String[] { "Select" });
                AccountNoTextField = new JTextField(20);
                SalaryTextField = new JTextField(20);
                PhotoLabel = new JLabel();

                JButton GenPassButton = new JButton("Generate");
                JButton PhotoChooserButon = new JButton("Select Photo");
                JButton CourseLoadingButton = new JButton("Load");
                JButton DeptLoadingButton = new JButton("Load");

                ButtonPanel = new JPanel(new FlowLayout());
                SaveButton = new JButton("Save");
                UpdateButton = new JButton("Update");
                DeleteButton = new JButton("Delete");
                ClearButton = new JButton("Clear");

                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setResizable(false);

                TitleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                TitlePanel.add(TitleLabel);

                BodyPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Row 0
                gbc.gridx = 0;
                gbc.gridy = 0;
                BodyPanel.add(new JLabel("Reg. No."), gbc);
                gbc.gridx = 1;
                BodyPanel.add(RegNoTextField, gbc);
                RegNoTextField.setEditable(false);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Date"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(DateChooser, gbc);

                // Row 1
                gbc.gridx = 0;
                gbc.gridy = 1;
                BodyPanel.add(new JLabel("Name"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(NameTextField, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("D.O.B"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(DOBChooser, gbc);

                // Row 2
                gbc.gridx = 0;
                gbc.gridy = 2;
                BodyPanel.add(new JLabel("Sex"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(SexComboBox, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Father's Name"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(FatherNameTextField, gbc);

                // Row 3 (Address)
                gbc.gridx = 0;
                gbc.gridy = 3;
                BodyPanel.add(new JLabel("Address"), gbc);
                gbc.gridx = 1;
                gbc.gridwidth = 3;
                BodyPanel.add(new JScrollPane(AddressTextArea), gbc);
                gbc.gridwidth = 1;

                // Row 4
                gbc.gridx = 0;
                gbc.gridy = 4;
                BodyPanel.add(new JLabel("Phone"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(PhoneTextField, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Email"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(EmailTextField, gbc);

                // Row 5
                gbc.gridx = 0;
                gbc.gridy = 5;
                BodyPanel.add(new JLabel("Designation"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(DesignationComboBox, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Experience (Years)"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(ExperienceSpinner, gbc);

                // Row 6
                gbc.gridx = 0;
                gbc.gridy = 6;
                BodyPanel.add(new JLabel("Qualification"), gbc);
                gbc.gridx = 1;
                BodyPanel.add(QualificationTextField, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Institution"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(InstitutionTextField, gbc);

                // Row 7
                gbc.gridx = 0;
                gbc.gridy = 7;
                BodyPanel.add(new JLabel("Password"), gbc);
                gbc.gridx = 1;
                JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                passPanel.add(PasswordTextField);
                passPanel.add(GenPassButton);
                BodyPanel.add(passPanel, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("D.O.J"), gbc);
                gbc.gridx = 3;
                BodyPanel.add(DOJChooser, gbc);

                // Row 8
                gbc.gridx = 0;
                gbc.gridy = 8;
                BodyPanel.add(new JLabel("Course"), gbc);
                gbc.gridx = 1;
                JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                coursePanel.add(CourseComboBox);
                coursePanel.add(CourseLoadingButton);
                BodyPanel.add(coursePanel, gbc);
                gbc.gridx = 2;
                BodyPanel.add(new JLabel("Department"), gbc);
                gbc.gridx = 3;
                JPanel deptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                deptPanel.add(DepartmentComboBox);
                deptPanel.add(DeptLoadingButton);
                BodyPanel.add(deptPanel, gbc);

                // Photo
                gbc.gridx = 4;
                gbc.gridy = 0;
                gbc.gridheight = 5;
                PhotoLabel.setPreferredSize(new java.awt.Dimension(150, 150));
                PhotoLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                BodyPanel.add(PhotoLabel, gbc);
                gbc.gridy = 5;
                gbc.gridheight = 1;
                BodyPanel.add(PhotoChooserButon, gbc);

                ButtonPanel.add(SaveButton);
                ButtonPanel.add(UpdateButton);
                ButtonPanel.add(DeleteButton);
                ButtonPanel.add(ClearButton);

                PhotoChooserButon.addActionListener(evt -> selectPhoto());
                GenPassButton.addActionListener(
                                evt -> PasswordTextField.setText(RandomGenerator.getAlphaNumericString(8)));
                CourseLoadingButton.addActionListener(evt -> CourseComboBox.setModel(
                                new DefaultComboBoxModel<>(DBFunctions.loadCourses().toArray(new String[0]))));
                DeptLoadingButton.addActionListener(evt -> {
                        if (CourseComboBox.getSelectedItem() != null) {
                                ArrayList<Branch> brs = DBFunctions
                                                .loadBranches(CourseComboBox.getSelectedItem().toString());
                                ArrayList<String> names = new ArrayList<>();
                                for (Branch b : brs)
                                        names.add(b.getInit());
                                DepartmentComboBox.setModel(new DefaultComboBoxModel<>(names.toArray(new String[0])));
                        }
                });
                SaveButton.addActionListener(this::SaveButtonActionPerformed);
                UpdateButton.addActionListener(this::UpdateButtonActionPerformed);
                DeleteButton.addActionListener(this::DeleteButtonActionPerformed);
                ClearButton.addActionListener(evt -> clearFields());

                getContentPane().add(TitlePanel, BorderLayout.NORTH);
                getContentPane().add(new JScrollPane(BodyPanel), BorderLayout.CENTER);
                getContentPane().add(ButtonPanel, BorderLayout.SOUTH);

                pack();
                setSize(1200, 800);
        }

        private void selectPhoto() {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        photopath = file.getAbsolutePath();
                        PhotoLabel.setIcon(UtilFunctions.resizeImage(photopath, null, PhotoLabel));
                }
        }

        private boolean checkInputs() {
                if (NameTextField.getText().isBlank() || photopath == null && PhotoLabel.getIcon() == null) {
                        JOptionPane.showMessageDialog(null, "Name and Photo are Required!");
                        return false;
                }
                return true;
        }

        private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (checkInputs()) {
                        try {
                                con.setAutoCommit(false);
                                String sql = "INSERT INTO faculty(registration_no, name, father_name, sex, dob, email, phone, password, address, photo, qualifications, institution, designation, experience, course, department, date_joined, date_updated) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement ps = con.prepareStatement(sql);
                                ps.setString(1, RegNoTextField.getText());
                                ps.setString(2, NameTextField.getText());
                                ps.setString(3, FatherNameTextField.getText());
                                ps.setString(4, SexComboBox.getSelectedItem().toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                ps.setString(5, sdf.format(DOBChooser.getDate()));
                                ps.setString(6, EmailTextField.getText());
                                ps.setString(7, PhoneTextField.getText());
                                ps.setString(8, PasswordTextField.getText());
                                ps.setString(9, AddressTextArea.getText());
                                if (photopath != null) {
                                        InputStream img = new FileInputStream(new File(photopath));
                                        ps.setBlob(10, img);
                                } else {
                                        ps.setNull(10, java.sql.Types.BLOB);
                                }
                                ps.setString(11, QualificationTextField.getText());
                                ps.setString(12, InstitutionTextField.getText());
                                ps.setString(13, DesignationComboBox.getSelectedItem().toString());
                                ps.setInt(14, (Integer) ExperienceSpinner.getValue());
                                ps.setString(15, CourseComboBox.getSelectedItem().toString());
                                ps.setString(16, DepartmentComboBox.getSelectedItem().toString());
                                ps.setString(17, sdf.format(DOJChooser.getDate()));
                                ps.setString(18, sdf.format(DateChooser.getDate()));
                                if (ps.executeUpdate() == 1) {
                                        con.commit();
                                        JOptionPane.showMessageDialog(null, "Faculty Recorded!");
                                        clearFields();
                                } else {
                                        con.rollback();
                                }
                        } catch (Exception e) {
                                try {
                                        con.rollback();
                                } catch (Exception ex) {
                                }
                                System.out.println(e);
                        } finally {
                                try {
                                        con.setAutoCommit(true);
                                } catch (Exception ex) {
                                }
                        }
                }
        }

        private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
                if (checkInputs()) {
                        try {
                                con.setAutoCommit(false);
                                String sql = "UPDATE faculty SET name=?, father_name=?, sex=?, dob=?, email=?, phone=?, password=?, address=?, photo=?, qualifications=?, institution=?, designation=?, experience=?, course=?, department=?, date_joined=?, date_updated=? WHERE registration_no=?";
                                PreparedStatement ps = con.prepareStatement(sql);
                                ps.setString(1, NameTextField.getText());
                                ps.setString(2, FatherNameTextField.getText());
                                ps.setString(3, SexComboBox.getSelectedItem().toString());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                ps.setString(4, sdf.format(DOBChooser.getDate()));
                                ps.setString(5, EmailTextField.getText());
                                ps.setString(6, PhoneTextField.getText());
                                ps.setString(7, PasswordTextField.getText());
                                ps.setString(8, AddressTextArea.getText());
                                if (photopath != null) {
                                        InputStream img = new FileInputStream(new File(photopath));
                                        ps.setBlob(9, img);
                                } else {
                                        // Keeping old photo would require more logic, but we must follow ACID for the
                                        // transaction itself
                                }
                                ps.setString(10, QualificationTextField.getText());
                                ps.setString(11, InstitutionTextField.getText());
                                ps.setString(12, DesignationComboBox.getSelectedItem().toString());
                                ps.setInt(13, (Integer) ExperienceSpinner.getValue());
                                ps.setString(14, CourseComboBox.getSelectedItem().toString());
                                ps.setString(15, DepartmentComboBox.getSelectedItem().toString());
                                ps.setString(16, sdf.format(DOJChooser.getDate()));
                                ps.setString(17, sdf.format(DateChooser.getDate()));
                                ps.setString(18, RegNoTextField.getText());
                                if (ps.executeUpdate() == 1) {
                                        con.commit();
                                        JOptionPane.showMessageDialog(null, "Updated!");
                                } else {
                                        con.rollback();
                                }
                        } catch (Exception e) {
                                try {
                                        con.rollback();
                                } catch (Exception ex) {
                                }
                                System.out.println(e);
                        } finally {
                                try {
                                        con.setAutoCommit(true);
                                } catch (Exception ex) {
                                }
                        }
                }
        }

        private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
                try {
                        con.setAutoCommit(false);
                        PreparedStatement ps = con.prepareStatement("DELETE FROM faculty WHERE registration_no=?");
                        ps.setString(1, RegNoTextField.getText());
                        if (ps.executeUpdate() == 1) {
                                con.commit();
                                JOptionPane.showMessageDialog(null, "Deleted!");
                                clearFields();
                        } else {
                                con.rollback();
                        }
                } catch (Exception e) {
                        try {
                                con.rollback();
                        } catch (Exception ex) {
                        }
                        System.out.println(e);
                } finally {
                        try {
                                con.setAutoCommit(true);
                        } catch (Exception ex) {
                        }
                }
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new FacultyEntryForm(role).setVisible(true));
        }

        private JTextField NameTextField, RegNoTextField, FatherNameTextField, PhoneTextField, EmailTextField,
                        PasswordTextField, QualificationTextField, InstitutionTextField, AccountNoTextField,
                        SalaryTextField;
        private JTextArea AddressTextArea;
        private JComboBox<String> SexComboBox, DesignationComboBox, CourseComboBox, DepartmentComboBox;
        private JDateChooser DOBChooser, DOJChooser, DateChooser;
        private JSpinner ExperienceSpinner;
        private JLabel PhotoLabel, TitleLabel;
        private JPanel TitlePanel, BodyPanel, ButtonPanel;
        private JButton SaveButton, UpdateButton, DeleteButton, ClearButton;
}
