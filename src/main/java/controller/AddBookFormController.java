package controller;

import com.sun.source.tree.IfTree;
import db.Book;
import db.IntMemoryDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AddBookFormController {
    public TextField txtISBN;
    public TextField txtBookName;
    public TextField txtAuthor;
    public Button btnAdd;
    public TextField txtQuantity;
    Jedis jedis;

    public void initialize(){
        jedis = new Jedis("127.0.0.1", 10001);
    }

    public void btnAddOnAction(ActionEvent actionEvent) throws IOException {

        String iSBN = txtISBN.getText();
        String bookName = txtBookName.getText();
        String author = txtAuthor.getText();
        String quantity = txtQuantity.getText();

        if (IntMemoryDB.isEmpty(iSBN)) {
            txtISBN.requestFocus();
            return;
        }
        else if(!IntMemoryDB.isNumber(iSBN)){
            new Alert(Alert.AlertType.ERROR,"Please enter valid ISBN").showAndWait();
            txtISBN.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(bookName)) {
            txtBookName.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(author)) {
            txtAuthor.requestFocus();
            return;
        } else if (!IntMemoryDB.isName(author)) {
            new Alert(Alert.AlertType.ERROR,"Author Name should be letter").showAndWait();
            txtAuthor.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(author)) {
            txtAuthor.requestFocus();
            return;
        } else if (!IntMemoryDB.isNumber(quantity)) {
            new Alert(Alert.AlertType.ERROR,"Please enter valid Quantity").showAndWait();
            txtISBN.requestFocus();
            return;
        }

        Book book = new Book(iSBN, bookName, author, quantity);
        boolean success = IntMemoryDB.addNewBook(book);

        if (success){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ? ", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Your book will successfully added to the system.");
            Optional<ButtonType> selectedOption = alert.showAndWait();
            if (selectedOption.get()==ButtonType.OK){
                BookStoreFormController ctrl = (BookStoreFormController) Navigation.navigate(Routes.BOOK_STORE);
                jedis.hset(book.getIsbnNumber(),"book-name",book.getName());
                jedis.hset(book.getIsbnNumber(),"book-author",book.getAuthor());
                jedis.hset(book.getIsbnNumber(),"book-qt",book.getQuantity());
                ctrl.tblBooksDetails.refresh();
                btnAdd.getScene().getWindow().hide();
            }else {
                IntMemoryDB.removeBook(book.getIsbnNumber());
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "This Book is already available in your library").showAndWait();
        }

    }



}
