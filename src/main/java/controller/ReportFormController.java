package controller;

import db.Book;
import db.IntMemoryDB;
import db.IssueBook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ReportFormController {
    public Button btnBack;
    public TableView<IssueBook> tblIssueReport;

    public void initialize(){

        tblIssueReport.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblIssueReport.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("iSBN"));
        tblIssueReport.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        tblIssueReport.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        tblIssueReport.setItems(FXCollections.observableList(IntMemoryDB.getIssueBookDataBase()));

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD);
    }
}
