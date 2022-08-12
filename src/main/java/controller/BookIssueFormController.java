package controller;

import db.Book;
import db.IntMemoryDB;
import db.IssueBook;
import db.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.util.Callback;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.security.AllPermission;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class BookIssueFormController {
    public TextField txtISBN;
    public Button btnIssue;
    public TextField txtNIC;
    public Button btnBack;
    public DatePicker dtpIssueDate;
    public DatePicker dtpReturnDate;
    public static int maximumBook = 3;
    Jedis jedis;

    public void initialize(){

        jedis = new Jedis("127.0.0.1",10003);

        dtpIssueDate.setEditable(false);
        dtpReturnDate.setEditable(false);

        dtpIssueDate.setValue(LocalDate.now());
        dtpReturnDate.setValue(dtpIssueDate.getValue().plusDays(10));

        Callback<DatePicker, DateCell> callback = new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item,empty);

                        if (item.isBefore(dtpIssueDate.getValue().plusDays(10))){
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        dtpReturnDate.setDayCellFactory(callback);

    }

    public void btnIssueOnAction(ActionEvent actionEvent) throws IOException {

        if (IntMemoryDB.isEmpty(txtNIC.getText())){
            txtNIC.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtISBN.getText())) {
            txtNIC.requestFocus();
            return;
        }else if (dtpIssueDate.getValue() ==null) {
            new Alert(Alert.AlertType.ERROR, "Issue date is required.").showAndWait();
            dtpIssueDate.requestFocus();
            return;
        }else if (dtpReturnDate.getValue() ==null) {
            new Alert(Alert.AlertType.ERROR, "Return date is required.").showAndWait();
            dtpReturnDate.requestFocus();
            return;
        }

        Book book = IntMemoryDB.findBook(txtISBN.getText());
        User user = IntMemoryDB.findUser(txtNIC.getText());

        if (user==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User is Not Registered in library", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("You will be redirected to user register page");
            Optional<ButtonType> buttonTypeOptional = alert.showAndWait();
            if (buttonTypeOptional.get()==ButtonType.OK) {
                Navigation.navigate(Routes.USER_STORE);
            }
            return;
        } else if (book==null) {
            new Alert(Alert.AlertType.ERROR,"This ISBN is not available in library. Please recheck ISBN").showAndWait();
            return;
        }

        int quantity = Integer.parseInt(book.getQuantity());

        if (quantity==0){
            new Alert(Alert.AlertType.ERROR,"This Book Not Available in library").showAndWait();
            return;
        }

        ArrayList<IssueBook> issueBooksArray = IntMemoryDB.findIssueBooks(txtNIC.getText());
        for (IssueBook issueBook : issueBooksArray) {
            if (issueBook.getISBN().equalsIgnoreCase(txtISBN.getText())){
                new Alert(Alert.AlertType.ERROR,"Already issue this book for this user").show();
                txtISBN.requestFocus();
                return;
            }
        }
        if (issueBooksArray.size()>maximumBook-1){
            new Alert(Alert.AlertType.ERROR,"Only "+ maximumBook +" books can issue per one user").show();
            txtISBN.clear();
            txtNIC.requestFocus();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION,"Successfully Issue Book").show();

        String keyIssueBook = "issueBook-"+txtNIC.getText()+"-"+txtISBN.getText();

        IssueBook issueBook = new IssueBook(txtNIC.getText(), txtISBN.getText(), dtpIssueDate.getValue(), dtpReturnDate.getValue());

        IntMemoryDB.addNewIssueBook(issueBook);
        jedis.hset(keyIssueBook,"nic",issueBook.getNic());
        jedis.hset(keyIssueBook,"isbn",issueBook.getISBN());
        jedis.hset(keyIssueBook,"issueDate", String.valueOf(issueBook.getIssueDate()));
        jedis.hset(keyIssueBook,"returnDate", String.valueOf(issueBook.getReturnDate()));

        int newQuantity=quantity-1;
        book.setQuantity(String.valueOf(newQuantity));

        txtNIC.clear();
        txtISBN.clear();

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }
}
