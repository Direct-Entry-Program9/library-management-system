package db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class IssueBook implements Serializable {

    private String nic;
    private String ISBN;
    private LocalDate issueDate;
    private LocalDate returnDate;

    public IssueBook(String nic, String ISBN, LocalDate issueDate, LocalDate returnDate) {
        this.nic = nic;
        this.ISBN = ISBN;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public IssueBook() {
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
