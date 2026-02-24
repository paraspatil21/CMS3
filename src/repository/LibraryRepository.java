package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Library;

public class LibraryRepository {
    private DBConnection dbConnection;

    public LibraryRepository() {
        dbConnection = new DBConnection();
    }

    public boolean addBook(Library book) {
        String query = "INSERT INTO library (book_name, author_name, isbn, quantity, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = dbConnection.connect();
                PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, book.getBookName());
            pst.setString(2, book.getAuthorName());
            pst.setString(3, book.getIsbn());
            pst.setInt(4, book.getQuantity());
            pst.setInt(5, book.getAvailable());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Library> getAllBooks() {
        List<Library> books = new ArrayList<>();
        String query = "SELECT * FROM library";
        try (Connection con = dbConnection.connect();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                books.add(new Library(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("author_name"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"),
                        rs.getInt("available")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean updateBook(Library book) {
        String query = "UPDATE library SET book_name = ?, author_name = ?, isbn = ?, quantity = ?, available = ? WHERE id = ?";
        Connection con = null;
        try {
            con = dbConnection.connect();
            con.setAutoCommit(false); // ACID Compliance

            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, book.getBookName());
                pst.setString(2, book.getAuthorName());
                pst.setString(3, book.getIsbn());
                pst.setInt(4, book.getQuantity());
                pst.setInt(5, book.getAvailable());
                pst.setInt(6, book.getId());

                int result = pst.executeUpdate();
                con.commit();
                return result > 0;
            } catch (SQLException e) {
                if (con != null)
                    con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean deleteBook(int id) {
        String query = "DELETE FROM library WHERE id = ?";
        Connection con = null;
        try {
            con = dbConnection.connect();
            con.setAutoCommit(false);

            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, id);
                int result = pst.executeUpdate();
                con.commit();
                return result > 0;
            } catch (SQLException e) {
                if (con != null)
                    con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Library getBookById(int id) {
        String query = "SELECT * FROM library WHERE id = ?";
        try (Connection con = dbConnection.connect();
                PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Library(
                            rs.getInt("id"),
                            rs.getString("book_name"),
                            rs.getString("author_name"),
                            rs.getString("isbn"),
                            rs.getInt("quantity"),
                            rs.getInt("available"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
