package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import models.Hostel;
import repository.DBConnection;

public class HostelIssueForm extends JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    Date date = new Date();
    private final Connection con = new DBConnection().connect();

    public HostelIssueForm(Role role) {
        HostelIssueForm.role = role;
        initComponents();
        customizeComponents();
    }

    private void customizeComponents() {
        setLocationRelativeTo(null);
        DateChooser.setDate(date);
    }

    private void clearFields() {
        RegNoTextField.setText(null);
        NameTextField.setText(null);
        HostelNoComboBox.setSelectedItem("Select");
        RoomNoComboBox.setSelectedItem("Select");
        FloorNoComboBox.setSelectedItem("Select");
        RoomTypeComboBox.setSelectedItem("Select");
        BedTypeComboBox.setSelectedItem("Select");
        DateChooser.setDate(date);
    }

    private Hostel retrieveData(String reg_no) {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM hostel WHERE reg_no LIKE '%" + reg_no + "%'");
            if (rs.next()) {
                return new Hostel(rs.getString("reg_no"), rs.getString("name"), rs.getString("hostel_no"),
                        rs.getString("floor_no"), rs.getString("room_no"), rs.getString("room_type"),
                        rs.getString("bed_type"), rs.getString("timestamp"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void showItemToFields(String reg_no) {
        Hostel data = retrieveData(reg_no);
        if (data == null)
            return;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        RegNoTextField.setText(data.getReg_no());
        NameTextField.setText(data.getName());
        HostelNoComboBox.setSelectedItem(data.getHostel_no());
        FloorNoComboBox.setSelectedItem(data.getFloor_no());
        RoomNoComboBox.setSelectedItem(data.getRoom_no());
        RoomTypeComboBox.setSelectedItem(data.getRoom_type());
        BedTypeComboBox.setSelectedItem(data.getBed_type());
        try {
            DateChooser.setDate(dateFormat.parse(data.getDate()));
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("HOSTEL ISSUE FORM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        RegNoTextField = new JTextField(20);
        JButton GetDetailsButton = new JButton("Get Details");
        GetDetailsButton.addActionListener(e -> getStudentName());

        NameTextField = new JTextField(20);
        HostelNoComboBox = new JComboBox<>(new String[] { "Select", "Hostel - 1", "Hostel - 2", "Hostel - 3" });
        FloorNoComboBox = new JComboBox<>(
                new String[] { "Select", "Ground Floor", "First Floor", "Second Floor", "Third Floor", "Top Floor" });
        RoomNoComboBox = new JComboBox<>(new String[] { "Select", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });
        RoomTypeComboBox = new JComboBox<>(
                new String[] { "Select", "Single Bed Room", "2-Bed Room", "3-Bed Room", "4-Bed Room" });
        BedTypeComboBox = new JComboBox<>(new String[] { "Select", "Separate", "Shared" });
        DateChooser = new JDateChooser();

        // Row 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        bodyPanel.add(new JLabel("Reg. No."), gbc);
        gbc.gridx = 1;
        bodyPanel.add(RegNoTextField, gbc);
        gbc.gridx = 2;
        bodyPanel.add(GetDetailsButton, gbc);

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        bodyPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(NameTextField, gbc);
        gbc.gridwidth = 1;

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        bodyPanel.add(new JLabel("Hostel No."), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(HostelNoComboBox, gbc);
        gbc.gridwidth = 1;

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 3;
        bodyPanel.add(new JLabel("Floor No."), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(FloorNoComboBox, gbc);
        gbc.gridwidth = 1;

        // Row 4
        gbc.gridx = 0;
        gbc.gridy = 4;
        bodyPanel.add(new JLabel("Room No."), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(RoomNoComboBox, gbc);
        gbc.gridwidth = 1;

        // Row 5
        gbc.gridx = 0;
        gbc.gridy = 5;
        bodyPanel.add(new JLabel("Room Type"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(RoomTypeComboBox, gbc);
        gbc.gridwidth = 1;

        // Row 6
        gbc.gridx = 0;
        gbc.gridy = 6;
        bodyPanel.add(new JLabel("Bed Type"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(BedTypeComboBox, gbc);
        gbc.gridwidth = 1;

        // Row 7
        gbc.gridx = 0;
        gbc.gridy = 7;
        bodyPanel.add(new JLabel("Date"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        bodyPanel.add(DateChooser, gbc);
        gbc.gridwidth = 1;

        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(this::saveInfo);
        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(this::updateInfo);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(this::deleteInfo);
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        btnPanel.add(saveBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void getStudentName() {
        String val = RegNoTextField.getText();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM student WHERE registration_no LIKE '%" + val + "%'");
            if (rs.next())
                NameTextField.setText(rs.getString("name"));
            else
                JOptionPane.showMessageDialog(null, "No Records Found!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void saveInfo(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "Access Denied!");
            return;
        }
        if (validateInputs()
                && JOptionPane.showConfirmDialog(null, "Issue hostel?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO hostel(reg_no, name, hostel_no, floor_no, room_no, room_type, bed_type, timestamp) VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1, RegNoTextField.getText().toUpperCase());
                ps.setString(2, NameTextField.getText());
                ps.setString(3, HostelNoComboBox.getSelectedItem().toString());
                ps.setString(4, FloorNoComboBox.getSelectedItem().toString());
                ps.setString(5, RoomNoComboBox.getSelectedItem().toString());
                ps.setString(6, RoomTypeComboBox.getSelectedItem().toString());
                ps.setString(7, BedTypeComboBox.getSelectedItem().toString());
                ps.setString(8, new SimpleDateFormat("dd-MM-yyyy").format(DateChooser.getDate()));
                if (ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(null, "Issued!");
                    clearFields();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void updateInfo(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "Access Denied!");
            return;
        }
        if (validateInputs()
                && JOptionPane.showConfirmDialog(null, "Update record?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE hostel SET reg_no=?, name=?, hostel_no=?, floor_no=?, room_no=?, room_type=?, bed_type=?, timestamp=? WHERE reg_no LIKE ?");
                ps.setString(1, RegNoTextField.getText().toUpperCase());
                ps.setString(2, NameTextField.getText());
                ps.setString(3, HostelNoComboBox.getSelectedItem().toString());
                ps.setString(4, FloorNoComboBox.getSelectedItem().toString());
                ps.setString(5, RoomNoComboBox.getSelectedItem().toString());
                ps.setString(6, RoomTypeComboBox.getSelectedItem().toString());
                ps.setString(7, BedTypeComboBox.getSelectedItem().toString());
                ps.setString(8, new SimpleDateFormat("dd-MM-yyyy").format(DateChooser.getDate()));
                ps.setString(9, "%" + RegNoTextField.getText() + "%");
                if (ps.executeUpdate() == 1)
                    JOptionPane.showMessageDialog(null, "Updated!");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void deleteInfo(java.awt.event.ActionEvent evt) {
        if (role != Role.ADMIN) {
            JOptionPane.showMessageDialog(null, "Access Denied!");
            return;
        }
        if (RegNoTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Reg No Required!");
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "Delete record?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM hostel WHERE reg_no LIKE ?");
                ps.setString(1, "%" + RegNoTextField.getText() + "%");
                if (ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(null, "Deleted!");
                    clearFields();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private boolean validateInputs() {
        if (RegNoTextField.getText().isBlank() || NameTextField.getText().isBlank()
                || HostelNoComboBox.getSelectedIndex() == 0 || FloorNoComboBox.getSelectedIndex() == 0
                || RoomNoComboBox.getSelectedIndex() == 0 || RoomTypeComboBox.getSelectedIndex() == 0
                || BedTypeComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return false;
        }
        return true;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new HostelIssueForm(role).setVisible(true));
    }

    private JTextField RegNoTextField, NameTextField;
    private JComboBox<String> HostelNoComboBox, FloorNoComboBox, RoomNoComboBox, RoomTypeComboBox, BedTypeComboBox;
    private JDateChooser DateChooser;
}
