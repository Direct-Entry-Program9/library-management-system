package controller;

import db.Book;
import db.IntMemoryDB;
import db.IssueBook;
import db.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    Jedis jedis;
    Jedis jedis1;
    Jedis jedis2;

    public void initialize(){

        jedis = new Jedis("127.0.0.1", 10001);
        jedis1 = new Jedis("127.0.0.1", 10002);
        jedis2 = new Jedis("127.0.0.1", 10003);

        Set<String> isbnNumbers = jedis.keys("*");
        for (String key : isbnNumbers) {
            String name = jedis.hget(key, "book-name");
            String author = jedis.hget(key, "book-author");
            String qt = jedis.hget(key, "book-qt");
            Book book = new Book(key, name, author, qt);
            IntMemoryDB.getBookDataBase().add(book);
        }
        Set<String> regNumbers = jedis1.keys("*");
        ArrayList<String> sortedRegNumbers = new ArrayList<>();
        for (String regNumber : regNumbers) {
            sortedRegNumbers.add(regNumber);
        }
        Collections.sort(sortedRegNumbers);
        for (String key : sortedRegNumbers) {
            String name = jedis1.hget(key, "name");
            String nic = jedis1.hget(key, "nic");
            String address = jedis1.hget(key, "address");
            String contact = jedis1.hget(key, "contact");
            User user = new User(key, name, nic, address,contact);
            IntMemoryDB.getUserDataBase().add(user);
        }
        Set<String> userNICs = jedis2.keys("*");
        for (String key : userNICs) {
            String isbn = jedis2.hget(key, "isbn");
            String issueDate = jedis2.hget(key, "issueDate");
            String returnDate = jedis2.hget(key, "returnDate");
            IssueBook issueBook = new IssueBook(key, isbn, LocalDate.parse(issueDate), LocalDate.parse(returnDate));
            IntMemoryDB.getIssueBookDataBase().add(issueBook);

        }

        Platform.runLater(() ->{
            try {
                Navigation.navigate(Routes.LOGIN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void btnLogoutOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.LOGIN);
    }
}
