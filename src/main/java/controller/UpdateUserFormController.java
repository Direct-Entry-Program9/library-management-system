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
        jedis = new Jedis("127.0.0.1",10003);
        txtRegNumber.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {

        if (IntMemoryDB.isEmpty(txtFirstName.getText())){
            txtFirstName.requestFocus();
            return;
        }
        else if (!IntMemoryDB.isName(txtFirstName.getText())) {
            new Alert(Alert.AlertType.ERROR,"Name should be letter").showAndWait();
            txtFirstName.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtNIC.getText())){
            txtNIC.requestFocus();
            return;
        } else if (!IntMemoryDB.isValidNIC(txtNIC.getText())){
            new Alert(Alert.AlertType.ERROR,"Please enter valid NIC").showAndWait();
            txtNIC.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtAddress.getText())){
            txtAddress.requestFocus();
            return;
        } else if (!IntMemoryDB.isMobileNumber(txtMobileNumber.getText())) {
            new Alert(Alert.AlertType.ERROR,"Please enter Valid Mobile Number").showAndWait();
            txtMobileNumber.requestFocus();
            return;
        }


        User user = new User(txtRegNumber.getText(),txtFirstName.getText(),txtNIC.getText(),txtAddress.getText(),txtMobileNumber.getText());
        IntMemoryDB.updateUser(userIndex, user);

        String keyUser = "user-"+user.getRegistrationNumber();

        jedis.hset(keyUser,"regNumber",user.getRegistrationNumber());
        jedis.hset(keyUser,"name",user.getFullName());
        jedis.hset(keyUser,"nic",user.getNic());
        jedis.hset(keyUser,"address",user.getAddress());
        jedis.hset(keyUser,"contact",user.getPhoneNumber());

        new Alert(Alert.AlertType.INFORMATION, "User details will successfully updated to the system.").showAndWait();
        UserStoreFormController ctrl = (UserStoreFormController) Navigation.navigate(Routes.USER_STORE);
        ctrl.tblUsersDetails.refresh();
        btnUpdate.getScene().getWindow().hide();
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
