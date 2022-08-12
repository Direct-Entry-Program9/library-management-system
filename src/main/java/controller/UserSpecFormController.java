package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;

public class UserSpecFormController {
    public AnchorPane pneContainer;
    public Spinner<Integer> txtMaximumBook;
    public Button btnSaveChanges;

    public void initialize(){
        txtMaximumBook.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10,3));
        btnSaveChanges.setDefaultButton(true);
    }

    public void btnSaveChangesOnAction(ActionEvent actionEvent) {
        BookIssueFormController.maximumBook=txtMaximumBook.getValue();
        new Alert(Alert.AlertType.INFORMATION,"Successfully Change").show();
    }
}
