package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class Navigation {

    private static AnchorPane pneContainer;

    public static void init(AnchorPane pneContainer){
        Navigation.pneContainer=pneContainer;
    }

    public static Object navigate(Routes route) throws IOException {
        URL resource = null;
        pneContainer.getChildren().clear();
        switch (route){
            case LOGIN:
                resource = Navigation.class.getResource("/view/LoginForm.fxml");
                break;
            case ADMIN_DASHBOARD:
                resource = Navigation.class.getResource("/view/AdminDashboardForm.fxml");
                break;
            case BOOK_STORE:
                resource = Navigation.class.getResource("/view/BookStoreForm.fxml");
                break;
            case USER_STORE:
                resource = Navigation.class.getResource("/view/UserStoreForm.fxml");
                break;
            case BOOK_ISSUE:
                resource = Navigation.class.getResource("/view/BookIssueForm.fxml");
                break;
            case BOOK_RETURN:
                resource = Navigation.class.getResource("/view/BookReturnForm.fxml");
                break;
            case REPORT:
                resource = Navigation.class.getResource("/view/ReportForm.fxml");
                break;
            case SETTING:
                resource = Navigation.class.getResource("/view/SettingsForm.fxml");
                break;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent container = fxmlLoader.load();
        pneContainer.getChildren().add(container);

        AnchorPane.setBottomAnchor(container,0.0);
        AnchorPane.setLeftAnchor(container,0.0);
        AnchorPane.setRightAnchor(container,0.0);
        AnchorPane.setTopAnchor(container,0.0);

        return fxmlLoader.getController();
    }

}
