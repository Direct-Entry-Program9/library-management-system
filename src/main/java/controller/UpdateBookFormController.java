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
        jedis = new Jedis("127.0.0.1", 10003);
        txtISBN.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {

        if (IntMemoryDB.isEmpty(txtISBN.getText())) {
            txtISBN.requestFocus();
            return;
        }
        else if(!IntMemoryDB.isNumber(txtISBN.getText())){
            new Alert(Alert.AlertType.ERROR,"Please enter valid ISBN").showAndWait();
            txtISBN.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtBookName.getText())) {
            txtBookName.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtAuthor.getText())) {
            txtAuthor.requestFocus();
            return;
        } else if (!IntMemoryDB.isName(txtAuthor.getText())) {
            new Alert(Alert.AlertType.ERROR,"Author Name should be letter").showAndWait();
            txtAuthor.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtQuantity.getText())) {
            txtAuthor.requestFocus();
            return;
        } else if (!IntMemoryDB.isNumber(txtQuantity.getText())) {
            new Alert(Alert.AlertType.ERROR,"Please enter valid Quantity").showAndWait();
            txtISBN.requestFocus();
            return;
        }

        Book book = new Book(txtISBN.getText(), txtBookName.getText(), txtAuthor.getText(), txtQuantity.getText());

        String keyBook = "book-"+book.getIsbnNumber();
        IntMemoryDB.updateBook(bookIndex, book);
        jedis.hset(keyBook,"book-isbn",book.getIsbnNumber());
        jedis.hset(keyBook,"book-name",book.getName());
        jedis.hset(keyBook,"book-author",book.getAuthor());
        jedis.hset(keyBook,"book-qt",book.getQuantity());

        new Alert(Alert.AlertType.INFORMATION, "Your book will successfully updated to the system.").showAndWait();
        BookStoreFormController ctrl = (BookStoreFormController) Navigation.navigate(Routes.BOOK_STORE);
        ctrl.tblBooksDetails.refresh();
        btnUpdate.getScene().getWindow().hide();

    }

    public void setData(Book book, int bookIndex){
        txtISBN.setText(book.getIsbnNumber());
        txtBookName.setText(book.getName());
        txtAuthor.setText(book.getAuthor());
        txtQuantity.setText(book.getQuantity());
        this.bookIndex=bookIndex;
    }
}
