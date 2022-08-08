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

        IssueBook issueBook = new IssueBook(txtNIC.getText(), txtISBN.getText(), dtpIssueDate.getValue(), dtpReturnDate.getValue());

        IntMemoryDB.addNewIssueBook(issueBook);
        jedis.hset(issueBook.getNic(),"isbn",issueBook.getISBN());
        jedis.hset(issueBook.getNic(),"issueDate", String.valueOf(issueBook.getIssueDate()));
        jedis.hset(issueBook.getNic(),"returnDate", String.valueOf(issueBook.getReturnDate()));

        int newQuantity=quantity-1;
        book.setQuantity(String.valueOf(newQuantity));

        txtNIC.clear();
        txtISBN.clear();

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }
}
