package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import models.Branch;

/**
 *
 * @author nixrajput
 */
public class DBFunctions {

    static Connection conn = new DBConnection().connect();

    public static ArrayList<String> loadCourses() {
        ArrayList<String> courseList = new ArrayList<>();
        try {
            String sql_query = "SELECT * FROM course";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_query);
            while (rs.next()) {
                courseList.add(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return courseList;
    }

    public static ArrayList<Branch> loadBranches(String course) {
        ArrayList<Branch> branchList = new ArrayList<>();
        try {
            String sql_query = "SELECT * FROM branch WHERE course LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1, "%" + course + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                branchList.add(new Branch(rs.getString("course"), rs.getString("title"), rs.getString("initial")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return branchList;
    }

    public static ArrayList<String> getSemester(String course, String branch) {
        ArrayList<String> sem = new ArrayList<>();
        try {
            String sql_query = "SELECT * FROM semester WHERE course LIKE ? AND branch LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1, "%" + course + "%");
            ps.setString(2, "%" + branch + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sem.add(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return sem;
    }

    public static ArrayList<String> getSubjects(String course) {
        ArrayList<String> subjectList = new ArrayList<>();
        try {
            String sql_query = "SELECT * FROM subject WHERE course LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1, "%" + course + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subjectList.add(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return subjectList;
    }

    public static ArrayList<String> getFaculty(String course) {
        ArrayList<String> facList = new ArrayList<>();
        try {
            String sql_query = "SELECT * FROM faculty WHERE course LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql_query);
            ps.setString(1, "%" + course + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                facList.add(rs.getString("name"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return facList;
    }
}
