package controller;

import db.Book;
import db.IntMemoryDB;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Stack;

public class BookStoreFormController {
    public TableView<Book> tblBooksDetails;
    public Button btnAddNewBook;
    public TextField txtSearch;
    public Button btnRemoveBook;
    public Button btnUpdateBook;
    public Button btnBack;
    Jedis jedis;


    public void initialize(){

        jedis = new Jedis("127.0.0.1", 10001);

        ObservableList<Book> olBooks = FXCollections.observableArrayList(IntMemoryDB.getBookDataBase());
        tblBooksDetails.setItems(olBooks);

        tblBooksDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbnNumber"));
        tblBooksDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblBooksDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tblBooksDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantity"));

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                if (prev.equals(current)) return;
                ObservableList<Book> olSearchBooks = FXCollections.observableArrayList(IntMemoryDB.findBooks(current));
                tblBooksDetails.setItems(olSearchBooks);
            }
        });

    }



    public void btnAddNewBookOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/view/AddBookForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.centerOnScreen();
    }

    public void btnRemoveBookOnAction(ActionEvent actionEvent) {
        Book selectedBook = tblBooksDetails.getSelectionModel().getSelectedItem();
        if (selectedBook==null){
            new Alert(Alert.AlertType.ERROR, "Please Select One Book").showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to remove this book from library ? ", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> selectedOption = alert.showAndWait();
        if (selectedOption.get()==ButtonType.YES){
            ObservableList<Book> olBooks = tblBooksDetails.getItems();
            olBooks.remove(selectedBook);
            IntMemoryDB.removeBook(selectedBook.getIsbnNumber());
            jedis.del(selectedBook.getIsbnNumber());
        }
        tblBooksDetails.getSelectionModel().clearSelection();


    }

    public void btnUpdateBookOnAction(ActionEvent actionEvent) throws IOException {

        URL resource = this.getClass().getResource("/view/UpdateBookForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent parent = fxmlLoader.load();
        UpdateBookFormController ctrl = fxmlLoader.getController();

        Book selectedBook = tblBooksDetails.getSelectionModel().getSelectedItem();
        int selectedIndex = tblBooksDetails.getSelectionModel().getSelectedIndex();

        if (selectedBook==null){
            new Alert(Alert.AlertType.ERROR, "Please Select One Book").showAndWait();
            return;
        }

        ctrl.setData(selectedBook,selectedIndex);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Update Book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.centerOnScreen();

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }
}
