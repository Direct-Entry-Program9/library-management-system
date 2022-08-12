package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.net.URL;

public class SettingsFormController {
    public Button btnBack;
    public Label lblChangePassword;
    public Label lblUserSpecs;
    public Label lblAbout;
    public AnchorPane pneSettingsContainer;

    public void initialize(){
        lblAbout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    URL resource = this.getClass().getResource("/view/AboutForm.fxml");
                    settingsNavigate(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        lblChangePassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    URL resource = this.getClass().getResource("/view/ChangePasswordForm.fxml");
                    settingsNavigate(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        lblUserSpecs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    URL resource = this.getClass().getResource("/view/UserSpecForm.fxml");
                    settingsNavigate(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }

    public void settingsNavigate(URL url) throws IOException {
        Platform.runLater(()->{
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(url);
                Parent container = fxmlLoader.load();
                AnchorPane pneContainer = (AnchorPane) container.lookup("#pneContainer");
                pneSettingsContainer.getChildren().clear();
                pneSettingsContainer.getChildren().add(pneContainer);

                AnchorPane.setBottomAnchor(pneContainer,0.0);
                AnchorPane.setLeftAnchor(pneContainer,0.0);
                AnchorPane.setRightAnchor(pneContainer,0.0);
                AnchorPane.setTopAnchor(pneContainer,0.0);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
