package controller;

import com.google.zxing.WriterException;
import db.Book;
import db.IntMemoryDB;
import db.User;
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
import javafx.stage.StageStyle;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class UserStoreFormController {
    public TableView<User> tblUsersDetails;
    public Button btnAddNewUser;
    public TextField txtSearch;
    public Button btnRemoveUser;
    public Button btnUpdateUser;
    public Button btnBack;
    Jedis jedis;

    public void initialize(){

        jedis = new Jedis("127.0.0.1",10003);

        ObservableList<User> olUser = FXCollections.observableArrayList(IntMemoryDB.getUserDataBase());
        tblUsersDetails.setItems(olUser);

        tblUsersDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        tblUsersDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tblUsersDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblUsersDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblUsersDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                if (prev.equals(current)) return;
                ObservableList<User> olSearchedUsers = FXCollections.observableArrayList(IntMemoryDB.findUsers(current));
                tblUsersDetails.setItems(olSearchedUsers);
            }
        });

        tblUsersDetails.setRowFactory(tv ->{
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && (!row.isEmpty())) {
                    User selUser = row.getItem();
                    try {
                        URL resource = this.getClass().getResource("/view/UserDetailsForm.fxml");
                        FXMLLoader fxmlLoader = new FXMLLoader(resource);
                        Parent parent = fxmlLoader.load();
                        Stage stage = new Stage(StageStyle.UNDECORATED);
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL);

                        UserDetailsFormController ctrl = fxmlLoader.getController();
                        ctrl.setData(selUser);

                        stage.show();
                        stage.centerOnScreen();

                    } catch (IOException | WriterException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }

    public void btnUpdateUserOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = this.getClass().getResource("/view/UpdateUserForm.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent parent = fxmlLoader.load();
        UpdateUserFormController ctrl = fxmlLoader.getController();

        int selectedIndex = tblUsersDetails.getSelectionModel().getSelectedIndex();
        User selectedUser = tblUsersDetails.getSelectionModel().getSelectedItem();

        if (selectedUser==null){
            new Alert(Alert.AlertType.ERROR, "Please Select One User").showAndWait();
            return;
        }

        ctrl.setData(selectedUser,selectedIndex);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Update User Detail");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.centerOnScreen();
    }

    public void btnRemoveUserOnAction(ActionEvent actionEvent) {
        User selectedUser = tblUsersDetails.getSelectionModel().getSelectedItem();
        if (selectedUser==null){
            new Alert(Alert.AlertType.ERROR, "Please Select One User").showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to remove this User from library ? ", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> selectedOption = alert.showAndWait();
        if (selectedOption.get()==ButtonType.YES){
            ObservableList<User> olUsers = tblUsersDetails.getItems();
            olUsers.remove(selectedUser);
            jedis.del("user-"+selectedUser.getRegistrationNumber());
            IntMemoryDB.removeUser(selectedUser.getNic());
        }
        tblUsersDetails.getSelectionModel().clearSelection();
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/view/AddUserForm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.centerOnScreen();
    }
}
