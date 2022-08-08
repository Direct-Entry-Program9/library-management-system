package controller;

import db.Book;
import db.IntMemoryDB;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.util.Optional;

public class UpdateBookFormController {
    public TextField txtISBN;
    public TextField txtBookName;
    public TextField txtAuthor;
    public TextField txtQuantity;
    public Button btnUpdate;
    private int bookIndex;
    Jedis jedis;

    public void initialize(){
        jedis = new Jedis("127.0.0.1", 10001);
        txtISBN.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        Book book = new Book(txtISBN.getText(), txtBookName.getText(), txtAuthor.getText(), txtQuantity.getText());
        boolean success = IntMemoryDB.updateBook(bookIndex, book);
        jedis.hset(book.getIsbnNumber(),"book-name",book.getName());
        jedis.hset(book.getIsbnNumber(),"book-author",book.getAuthor());
        jedis.hset(book.getIsbnNumber(),"book-qt",book.getQuantity());

        if (success){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ? ", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Your book will successfully updated to the system.");
            Optional<ButtonType> selectedOption = alert.showAndWait();
            if (selectedOption.get()==ButtonType.OK){
                BookStoreFormController ctrl = (BookStoreFormController) Navigation.navigate(Routes.BOOK_STORE);
                ctrl.tblBooksDetails.refresh();
                btnUpdate.getScene().getWindow().hide();
            }
        }

    }

    public void setData(Book book, int bookIndex){
        txtISBN.setText(book.getIsbnNumber());
        txtBookName.setText(book.getName());
        txtAuthor.setText(book.getAuthor());
        txtQuantity.setText(book.getQuantity());
        this.bookIndex=bookIndex;
    }
}
