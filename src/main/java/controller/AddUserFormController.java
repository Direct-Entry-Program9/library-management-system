package controller;

import db.IntMemoryDB;
import db.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class AddUserFormController {
    public TextField txtRegNumber;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtMobileNumber;
    public Button btnAdd;
    Jedis jedis;

    public void initialize(){
        String stringRegNumber = generateNewId();
        txtRegNumber.setText(stringRegNumber);
        txtRegNumber.setDisable(true);


        jedis = new Jedis("127.0.0.1",10002);

    }

    public void btnAddOnAction(ActionEvent actionEvent) throws IOException {

        String regNumber = txtRegNumber.getText();
        String fullName = txtFirstName.getText() + " "+ txtLastName.getText();
        String nic = txtNIC.getText();
        String address = txtAddress.getText();
        String mobileNumber = txtMobileNumber.getText();

        if (IntMemoryDB.isEmpty(regNumber)){
            txtRegNumber.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(txtFirstName.getText())){
            txtFirstName.requestFocus();
            return;
        }
        else if (!IntMemoryDB.isName(fullName)) {
            new Alert(Alert.AlertType.ERROR,"Name should be letter").showAndWait();
            txtFirstName.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(nic)){
            txtNIC.requestFocus();
            return;
        } else if (!IntMemoryDB.isValidNIC(nic)){
            new Alert(Alert.AlertType.ERROR,"Please enter valid NIC").showAndWait();
            txtNIC.requestFocus();
            return;
        } else if (IntMemoryDB.isEmpty(address)){
            txtAddress.requestFocus();
            return;
        } else if (!IntMemoryDB.isMobileNumber(mobileNumber)) {
            new Alert(Alert.AlertType.ERROR,"Please enter Valid Mobile Number").showAndWait();
            txtMobileNumber.requestFocus();
            return;
        } else if (txtLastName.getText().isEmpty()) {
            txtLastName.setText("");
        }

        User user = new User(regNumber,fullName,nic,address,mobileNumber);
        boolean success = IntMemoryDB.addNewUser(user);

        if (success){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ? ", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Your user will successfully added to the system.");
            Optional<ButtonType> selectedOption = alert.showAndWait();
            if (selectedOption.get()==ButtonType.OK){
                UserStoreFormController ctrl = (UserStoreFormController) Navigation.navigate(Routes.USER_STORE);
                jedis.hset(user.getRegistrationNumber(),"name",user.getFullName());
                jedis.hset(user.getRegistrationNumber(),"nic",user.getNic());
                jedis.hset(user.getRegistrationNumber(),"address",user.getAddress());
                jedis.hset(user.getRegistrationNumber(),"contact",user.getPhoneNumber());
                ctrl.tblUsersDetails.refresh();
                btnAdd.getScene().getWindow().hide();

            }else {
                IntMemoryDB.removeUser(user.getNic());
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "This User is already available in your library").showAndWait();
        }

    }
    private String generateNewId(){

        if (IntMemoryDB.getUserDataBase().size()==0) return "M001";
        ArrayList<User> userDataBase = IntMemoryDB.getUserDataBase();
        User lastUser = userDataBase.get(userDataBase.size() - 1);
        String registrationNumber = lastUser.getRegistrationNumber();
        int lastUserRegNumberInt = Integer.parseInt(registrationNumber.substring(1));
        int newUserRegNumberInt = lastUserRegNumberInt + 1;

        if (newUserRegNumberInt<=9){
            return  "M00"+newUserRegNumberInt;
        } else if (newUserRegNumberInt<=99) {
            return  "M0"+newUserRegNumberInt;
        } else {
            return "M"+newUserRegNumberInt;
        }

    }
}
