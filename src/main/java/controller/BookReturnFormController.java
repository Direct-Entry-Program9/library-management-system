package controller;

import com.jfoenix.controls.JFXComboBox;
import db.Book;
import db.IntMemoryDB;
import db.IssueBook;
import db.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.util.ArrayList;

public class BookReturnFormController {
    public TextField txtIssueDate;
    public TextField txtReturnDate;
    public Button btnReturn;
    public Button btnBack;
    public JFXComboBox<String> txtISBN;
    public TextField txtName;
    public JFXComboBox<String> txtNIC;
    Jedis jedis;

    public void initialize(){

        jedis = new Jedis("127.0.0.1",10003);

        txtName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                ArrayList<User> users = IntMemoryDB.findUsers(current);
                ArrayList<String> nicList = new ArrayList<>();
                for (User user : users) {
                    nicList.add(user.getNic());
                }

                txtNIC.setItems(FXCollections.observableList(nicList));

                txtNIC.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                        if (current==null){
                            return;
                        }
                        ArrayList<IssueBook> issueBooks = IntMemoryDB.findIssueBooks(current);

                        ArrayList<String> isbnList = new ArrayList<>();
                        for (IssueBook issueBook : issueBooks) {
                            isbnList.add(issueBook.getISBN());
                        }

                        txtISBN.setItems(FXCollections.observableList(isbnList));

                        txtISBN.valueProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                                if (current==null){
                                    return;
                                }

                                IssueBook issueBook = IntMemoryDB.findIssueBook(current);
                                txtIssueDate.setText(String.valueOf(issueBook.getIssueDate()));
                                txtReturnDate.setText(String.valueOf(issueBook.getReturnDate()));
                            }
                        });
                    }
                });



            }
        });

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }

    public void btnReturnOnAction(ActionEvent actionEvent) {

        ArrayList<IssueBook> issueBookDataBase = IntMemoryDB.getIssueBookDataBase();
        IssueBook returnBook = null;

        if (txtISBN.getValue()==null){
            new Alert(Alert.AlertType.ERROR,"Invalid Proceed. Please select return book").showAndWait();
            return;
        }
        for (IssueBook issueBook : issueBookDataBase) {
            if (issueBook.getISBN().equalsIgnoreCase(txtISBN.getValue()) && issueBook.getNic().equalsIgnoreCase(txtNIC.getValue())){
                returnBook = issueBook;
            }
        }
        if (returnBook==null){
            new Alert(Alert.AlertType.ERROR,"Invalid Proceed. Already return book").showAndWait();
            return;
        }

        Book returnBookSetQt = IntMemoryDB.findBook(txtISBN.getValue());
        int currentQt = Integer.parseInt(returnBookSetQt.getQuantity());
        returnBookSetQt.setQuantity(String.valueOf(currentQt+1));

        new Alert(Alert.AlertType.INFORMATION,"Successfully Return Book").show();

        String keyIssueBook = "issueBook-"+returnBook.getNic()+"-"+returnBook.getISBN();

        IntMemoryDB.removeIssueBook(returnBook);
        jedis.del(keyIssueBook);

        txtName.clear();
        txtNIC.setValue(null);
        txtISBN.setValue(null);
        txtReturnDate.clear();
        txtIssueDate.clear();

        initialize();

    }
}
