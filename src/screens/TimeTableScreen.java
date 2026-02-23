package screens;

import constants.Role;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import repository.DBConnection;
import repository.DBFunctions;

public class TimeTableScreen extends JFrame {

        private static final long serialVersionUID = 1L;
        static Role role;
        private final Connection con = new DBConnection().connect();

        public TimeTableScreen(Role role) {
                TimeTableScreen.role = role;
                initComponents();
                setLocationRelativeTo(null);
        }

        private void initComponents() {
                setTitle("TIME TABLE");
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setSize(1200, 800);
                setResizable(false);

                JPanel mainPanel = new JPanel(new BorderLayout());

                JPanel titlePanel = new JPanel();
                JLabel titleLabel = new JLabel("TIME TABLE");
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
                titlePanel.add(titleLabel);
                mainPanel.add(titlePanel, BorderLayout.NORTH);

                JPanel controlsPanel = new JPanel(new FlowLayout());

                JPanel coursePanel = new JPanel(new FlowLayout());
                coursePanel.add(new JLabel("Course"));
                CourseComboBox = new JComboBox<>(new String[] { "Select" });
                coursePanel.add(CourseComboBox);
                JButton courseLoadBtn = new JButton("Load");
                courseLoadBtn.addActionListener(evt -> loadCourses());
                coursePanel.add(courseLoadBtn);

                JPanel branchPanel = new JPanel(new FlowLayout());
                branchPanel.add(new JLabel("Branch"));
                BranchComboBox = new JComboBox<>(new String[] { "Select" });
                branchPanel.add(BranchComboBox);
                JButton branchLoadBtn = new JButton("Load");
                branchPanel.add(branchLoadBtn);

                JPanel semPanel = new JPanel(new FlowLayout());
                semPanel.add(new JLabel("Semester"));
                SemComboBox = new JComboBox<>(new String[] { "Select" });
                semPanel.add(SemComboBox);
                JButton semLoadBtn = new JButton("Load");
                semPanel.add(semLoadBtn);

                controlsPanel.add(coursePanel);
                controlsPanel.add(branchPanel);
                controlsPanel.add(semPanel);

                mainPanel.add(controlsPanel, BorderLayout.CENTER);

                add(mainPanel);
        }

        private void loadCourses() {
                ArrayList<String> courses = DBFunctions.loadCourses();
                if (courses.isEmpty())
                        courses.add("Select");
                CourseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new TimeTableScreen(role).setVisible(true));
        }

        private JComboBox<String> CourseComboBox;
        private JComboBox<String> BranchComboBox;
        private JComboBox<String> SemComboBox;
}
