package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class ChangePasswordFormController {
    public AnchorPane pneContainer;
    public JFXPasswordField pwdCurrentPassword;
    public Label lblCurrentPassword;
    public JFXPasswordField pwdNewPassword;
    public Label lblNewPassword;
    public JFXPasswordField pwdReEnter;
    public Label lblReEnter;
    public JFXButton btnChangePassword;
    Jedis jedis2;

    public void initialize(){
        jedis2 = new Jedis("127.0.0.1", 10003);

        pwdCurrentPassword.clear();
        pwdCurrentPassword.requestFocus();
        pwdNewPassword.clear();
        pwdNewPassword.setDisable(true);
        pwdReEnter.clear();
        pwdReEnter.setDisable(true);
        btnChangePassword.setDisable(true);
        btnChangePassword.setDefaultButton(true);
        pwdCurrentPassword.requestFocus();
        lblCurrentPassword.setText("Invalid Password ❌");
        lblNewPassword.setText("*8-10 characters required.");
        lblReEnter.setText(null);

        pwdCurrentPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {

                if (!LoginFormController.getAdminPassword().equals(current)){
                    lblCurrentPassword.setText("Invalid Password ❌");
                    lblCurrentPassword.setTextFill(Color.RED);
                    pwdNewPassword.setDisable(true);
                    btnChangePassword.setDisable(true);
                    pwdReEnter.clear();
                }else {
                    lblCurrentPassword.setText("Valid Password ✅");
                    lblCurrentPassword.setTextFill(Color.GREEN);
                    pwdNewPassword.setDisable(false);
                }
            }
        });

        pwdNewPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String current) {
                if (!isValidPassword(current)){
                    pwdReEnter.setDisable(true);
                    lblNewPassword.setText("*8-10 characters required.");
                    lblNewPassword.setTextFill(Color.RED);
                    btnChangePassword.setDisable(true);
                    pwdReEnter.clear();
                }else {
                    pwdReEnter.setDisable(false);
                    lblNewPassword.setText("Good to go ✅");
                    lblNewPassword.setTextFill(Color.GREEN);
                }
            }
        });

        pwdReEnter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String current) {
                if (!pwdNewPassword.getText().equals(current)){
                    lblReEnter.setText("not matched ❎");
                    lblReEnter.setTextFill(Color.RED);
                    btnChangePassword.setDisable(true);
                }else {
                    lblReEnter.setText("Matched ✅");
                    lblReEnter.setTextFill(Color.GREEN);
                    btnChangePassword.setDisable(false);
                }
            }
        });
    }

    public void btnChangePasswordOnAction(ActionEvent actionEvent) {

        Optional<ButtonType> buttonOption = new Alert(Alert.AlertType.INFORMATION, "Do you want to change admin password ?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonOption.get()==ButtonType.NO){
            pwdNewPassword.requestFocus();
            return;
        }

        jedis2.set("password",pwdReEnter.getText());
        LoginFormController.setAdminPassword(pwdReEnter.getText());
        new Alert(Alert.AlertType.INFORMATION,"Successfully changed password").show();
        initialize();

    }

    public boolean isValidPassword(String input){
        if (input.trim().length()<8 || input.trim().length()>10) return false;
        return true;
    }

}
