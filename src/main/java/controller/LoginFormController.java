package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import util.Navigation;
import util.Routes;

import java.io.IOException;

public class LoginFormController {
    public PasswordField txtPassword;
    public Button btnLogin;
    private static String adminPassword = "Manelka";
    Jedis jedis2;

    public void initialize(){
        Platform.runLater(txtPassword::requestFocus);
        jedis2 = new Jedis("127.0.0.1", 10003);

        adminPassword=jedis2.get("password");
    }
    public static String getAdminPassword() {
        return adminPassword;
    }

    public static void setAdminPassword(String adminPassword) {
        LoginFormController.adminPassword = adminPassword;
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        if (txtPassword.getText().isBlank()){
            new Alert(Alert.AlertType.ERROR,"Password can't be empty").showAndWait();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        } else if (!txtPassword.getText().equals(adminPassword)) {
            new Alert(Alert.AlertType.ERROR,"Password INVALID. Please enter valid password").showAndWait();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }

        Navigation.navigate(Routes.ADMIN_DASHBOARD);

    }

}
