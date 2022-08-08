package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import util.Navigation;
import util.Routes;

import java.io.IOException;

public class SettingsFormController {
    public Button btnBack;

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }
}
