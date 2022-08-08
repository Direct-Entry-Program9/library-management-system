package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import util.Navigation;
import util.Routes;

import java.io.IOException;

public class AdminDashboardFormController {
    public Button btnManageBook;
    public Button btnManageUsers;
    public Button btnBookIssue;
    public Button btnReturnBook;
    public Button btnReport;
    public Button btnSettings;
    public Button btnExit;

    public void initialize(){
        Platform.runLater(btnManageBook::requestFocus);
    }

    public void btnManageBookOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.BOOK_STORE);
    }

    public void btnManageUsersOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.USER_STORE);
    }

    public void btnBookIssueOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.BOOK_ISSUE);
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnSettingsOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.SETTING);
    }

    public void btnReportOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.REPORT);
    }

    public void btnReturnBookOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.BOOK_RETURN);
    }
}
