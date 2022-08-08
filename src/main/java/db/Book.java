package db;

public class Book {
    private String isbnNumber;
    private String name;
    private String author;
    private String quantity;

    public Book(String isbnNumber, String name, String author,String quantity) {
        this.isbnNumber = isbnNumber;
        this.name = name;
        this.author = author;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Book() {
    }

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
