package models;

public class Library {
    private int id;
    private String bookName;
    private String authorName;
    private String isbn;
    private int quantity;
    private int available;

    public Library() {
    }

    public Library(int id, String bookName, String authorName, String isbn, int quantity, int available) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.isbn = isbn;
        this.quantity = quantity;
        this.available = available;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
