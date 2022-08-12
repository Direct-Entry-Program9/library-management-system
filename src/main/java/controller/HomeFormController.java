package controller;

import db.Book;
import db.IntMemoryDB;
import db.IssueBook;
import db.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class HomeFormController {
    public Button btnLogout;
    public AnchorPane pneContainer;
    Jedis jedis2;

    public void initialize(){

        jedis2 = new Jedis("127.0.0.1", 10003);

        Set<String> books = jedis2.keys("book*");
        for (String key : books) {
            String isbn = jedis2.hget(key, "book-isbn");
            String name = jedis2.hget(key, "book-name");
            String author = jedis2.hget(key, "book-author");
            String qt = jedis2.hget(key, "book-qt");
            Book book = new Book(isbn, name, author, qt);
            IntMemoryDB.getBookDataBase().add(book);
        }
        Set<String> regNumbers = jedis2.keys("user*");
        ArrayList<String> sortedRegNumbers = new ArrayList<>();
        for (String regNumber : regNumbers) {
            sortedRegNumbers.add(regNumber);
        }
        Collections.sort(sortedRegNumbers);
        for (String key : sortedRegNumbers) {
            String regNumber = jedis2.hget(key, "regNumber");
            String name = jedis2.hget(key, "name");
            String nic = jedis2.hget(key, "nic");
            String address = jedis2.hget(key, "address");
            String contact = jedis2.hget(key, "contact");
            User user = new User(regNumber, name, nic, address,contact);
            IntMemoryDB.getUserDataBase().add(user);
        }
        Set<String> issueBooks = jedis2.keys("issueBook*");
        for (String key : issueBooks) {
            String nic = jedis2.hget(key,"nic");
            String isbn = jedis2.hget(key, "isbn");
            String issueDate = jedis2.hget(key, "issueDate");
            String returnDate = jedis2.hget(key, "returnDate");
            IssueBook issueBook = new IssueBook(nic, isbn, LocalDate.parse(issueDate), LocalDate.parse(returnDate));
            IntMemoryDB.getIssueBookDataBase().add(issueBook);

        }

        Platform.runLater(() ->{
            try {
                Navigation.navigate(Routes.LOGIN);
                btnLogout.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        jedis2.shutdown();
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.LOGIN);
    }
}
