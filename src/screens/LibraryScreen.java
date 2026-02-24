package screens;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import models.Library;
import repository.LibraryRepository;

public class LibraryScreen extends JFrame {

    private JTextField txtBookName, txtAuthorName, txtIsbn, txtQuantity, txtAvailable;
    private JTable table;
    private DefaultTableModel tableModel;
    private LibraryRepository repository;
    private int selectedId = -1;

    public LibraryScreen() {
        setTitle("Library Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        repository = new LibraryRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new TitledBorder(new EtchedBorder(), "Book Details"));

        formPanel.add(new JLabel("Book Name:"));
        txtBookName = new JTextField();
        formPanel.add(txtBookName);

        formPanel.add(new JLabel("Author Name:"));
        txtAuthorName = new JTextField();
        formPanel.add(txtAuthorName);

        formPanel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        formPanel.add(txtIsbn);

        formPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        formPanel.add(txtQuantity);

        formPanel.add(new JLabel("Available:"));
        txtAvailable = new JTextField();
        formPanel.add(txtAvailable);

        JButton btnAdd = new JButton("Add Book");
        btnAdd.addActionListener(e -> addBook());
        formPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Book");
        btnUpdate.addActionListener(e -> updateBook());
        formPanel.add(btnUpdate);

        mainPanel.add(formPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columns = { "ID", "Book Name", "Author", "ISBN", "Qty", "Avail" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedId = (int) table.getValueAt(row, 0);
                txtBookName.setText(table.getValueAt(row, 1).toString());
                txtAuthorName.setText(table.getValueAt(row, 2).toString());
                txtIsbn.setText(table.getValueAt(row, 3).toString());
                txtQuantity.setText(table.getValueAt(row, 4).toString());
                txtAvailable.setText(table.getValueAt(row, 5).toString());
            }
        });
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> deleteBook());
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Library> books = repository.getAllBooks();
        for (Library book : books) {
            tableModel.addRow(new Object[] {
                    book.getId(),
                    book.getBookName(),
                    book.getAuthorName(),
                    book.getIsbn(),
                    book.getQuantity(),
                    book.getAvailable()
            });
        }
        clearFields();
    }

    private void clearFields() {
        txtBookName.setText("");
        txtAuthorName.setText("");
        txtIsbn.setText("");
        txtQuantity.setText("");
        txtAvailable.setText("");
        selectedId = -1;
        table.clearSelection();
    }

    private void addBook() {
        try {
            if (txtBookName.getText().isEmpty() || txtAuthorName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill required fields.");
                return;
            }
            Library book = new Library(0, txtBookName.getText(), txtAuthorName.getText(),
                    txtIsbn.getText(), Integer.parseInt(txtQuantity.getText()),
                    Integer.parseInt(txtAvailable.getText()));
            if (repository.addBook(book)) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding book.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and available.");
        }
    }

    private void updateBook() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
            return;
        }
        try {
            Library book = new Library(selectedId, txtBookName.getText(), txtAuthorName.getText(),
                    txtIsbn.getText(), Integer.parseInt(txtQuantity.getText()),
                    Integer.parseInt(txtAvailable.getText()));
            if (repository.updateBook(book)) {
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating book.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and available.");
        }
    }

    private void deleteBook() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (repository.deleteBook(selectedId)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting book.");
            }
        }
    }
}
