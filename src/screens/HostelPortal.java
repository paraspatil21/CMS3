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
import models.Hostel;
import repository.DBConnection;

public class HostelPortal extends JFrame {

    private static final long serialVersionUID = 1L;
    static Role role;
    private final Connection con = new DBConnection().connect();
    ArrayList<Hostel> hostel_list = new ArrayList<>();

    public HostelPortal(Role role) {
        HostelPortal.role = role;
        initComponents();
        setLocationRelativeTo(null);
        fillTable();
    }

    private ArrayList<Hostel> retrieveData() {
        ArrayList<Hostel> stu_list = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM hostel");
            while (rs.next()) {
                stu_list.add(new Hostel(rs.getString("reg_no"), rs.getString("name"), rs.getString("hostel_no"),
                        rs.getString("floor_no"), rs.getString("room_no"), rs.getString("room_type"),
                        rs.getString("bed_type"), rs.getString("timestamp")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return stu_list;
    }

    private void fillTable() {
        hostel_list = retrieveData();
        DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
        model.setRowCount(0);
        for (Hostel h : hostel_list) {
            model.addRow(new Object[] { h.getReg_no(), h.getName(), h.getHostel_no(), h.getFloor_no(), h.getRoom_no(),
                    h.getRoom_type(), h.getBed_type() });
        }
    }

    private void initComponents() {
        setTitle("COLLEGE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("HOSTEL PORTAL");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton EntryButton = new JButton("New Entry");
        EntryButton.addActionListener(e -> {
            if (role == Role.ADMIN)
                new HostelIssueForm(role).setVisible(true);
            else
                JOptionPane.showMessageDialog(null, "Access Denied!");
        });
        JButton RefreshButton = new JButton("Refresh");
        RefreshButton.addActionListener(e -> {
            SearchTextField.setText(null);
            fillTable();
        });

        buttonPanel.add(EntryButton);
        buttonPanel.add(RefreshButton);
        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search"));
        SearchTextField = new JTextField(40);
        searchPanel.add(SearchTextField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchHostel());
        searchPanel.add(searchBtn);
        centerPanel.add(searchPanel, BorderLayout.CENTER);

        StudentTable = new JTable(new DefaultTableModel(
                new String[] { "Reg. No.", "Name", "Hostel No.", "Floor No.", "Room No.", "Room Type", "Bed Type" },
                0));
        StudentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int ind = StudentTable.getSelectedRow();
                if (ind >= 0) {
                    String reg = hostel_list.get(ind).getReg_no();
                    HostelIssueForm form = new HostelIssueForm(role);
                    form.setVisible(true);
                    form.showItemToFields(reg);
                }
            }
        });
        centerPanel.add(new JScrollPane(StudentTable), BorderLayout.SOUTH);
        StudentTable.setPreferredScrollableViewportSize(new java.awt.Dimension(1100, 500));

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void searchHostel() {
        String val = "%" + SearchTextField.getText() + "%";
        try {
            String qry = "SELECT * FROM hostel WHERE name LIKE ? OR reg_no LIKE ?";
            PreparedStatement ps = con.prepareStatement(qry);
            ps.setString(1, val);
            ps.setString(2, val);
            ResultSet rs = ps.executeQuery();
            hostel_list.clear();
            while (rs.next()) {
                hostel_list.add(new Hostel(rs.getString("reg_no"), rs.getString("name"), rs.getString("hostel_no"),
                        rs.getString("floor_no"), rs.getString("room_no"), rs.getString("room_type"),
                        rs.getString("bed_type"), rs.getString("timestamp")));
            }
            DefaultTableModel model = (DefaultTableModel) StudentTable.getModel();
            model.setRowCount(0);
            for (Hostel h : hostel_list) {
                model.addRow(new Object[] { h.getReg_no(), h.getName(), h.getHostel_no(), h.getFloor_no(),
                        h.getRoom_no(), h.getRoom_type(), h.getBed_type() });
            }
            if (hostel_list.isEmpty())
                JOptionPane.showMessageDialog(null, "No Records Found!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new HostelPortal(role).setVisible(true));
    }

    private JTextField SearchTextField;
    private JTable StudentTable;
}
