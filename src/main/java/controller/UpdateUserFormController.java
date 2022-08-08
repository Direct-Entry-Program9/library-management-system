package controller;

import db.Book;
import db.IntMemoryDB;
import db.User;
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

public class UpdateUserFormController {
    public TextField txtRegNumber;
    public TextField txtFirstName;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtMobileNumber;
    public Button btnUpdate;
    private int userIndex;
    Jedis jedis;

    public void initialize(){
        jedis = new Jedis("127.0.0.1",10002);
        txtRegNumber.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        User user = new User(txtRegNumber.getText(),txtFirstName.getText(),txtNIC.getText(),txtAddress.getText(),txtMobileNumber.getText());
        boolean success = IntMemoryDB.updateUser(userIndex, user);
        jedis.hset(user.getRegistrationNumber(),"name",user.getFullName());
        jedis.hset(user.getRegistrationNumber(),"nic",user.getNic());
        jedis.hset(user.getRegistrationNumber(),"address",user.getAddress());
        jedis.hset(user.getRegistrationNumber(),"contact",user.getPhoneNumber());

        if (success){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ? ", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Your user will successfully updated to the system.");
            Optional<ButtonType> selectedOption = alert.showAndWait();
            if (selectedOption.get()==ButtonType.OK){
                UserStoreFormController ctrl = (UserStoreFormController) Navigation.navigate(Routes.USER_STORE);
                ctrl.tblUsersDetails.refresh();
                btnUpdate.getScene().getWindow().hide();
            }
        }
    }

    public void setData(User user, int userIndex){
        txtRegNumber.setText(user.getRegistrationNumber());
        txtFirstName.setText(user.getFullName());
        txtNIC.setText(user.getNic());
        txtAddress.setText(user.getAddress());
        txtMobileNumber.setText(user.getPhoneNumber());
        this.userIndex=userIndex;
    }
}
