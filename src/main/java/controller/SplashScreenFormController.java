package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import redis.clients.jedis.Jedis;
import util.Navigation;

import java.io.IOException;
import java.net.URL;

public class SplashScreenFormController {

    public Rectangle pgbProgress;
    public Rectangle pgbLoading;
    public Label lblLoading;

    public void initialize(){

        Timeline timeline = new Timeline();
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pgbLoading.setWidth(pgbLoading.getWidth()+10);
                lblLoading.setText("Connecting with the database...!");
            }
        });
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(1500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pgbLoading.setWidth(pgbLoading.getWidth()+100);
                lblLoading.setText("Loading data...!");
            }
        });
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(2500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pgbLoading.setWidth(pgbLoading.getWidth()+100);
                lblLoading.setText("Setting up the UI");
            }
        });
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(3000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pgbLoading.setWidth(pgbProgress.getWidth());
                lblLoading.setText("Complete...!");
            }
        });
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(3500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                URL resource = this.getClass().getResource("/view/HomeForm.fxml");
                try {
                    Parent parent = FXMLLoader.load(resource);
                    AnchorPane pneContainer = (AnchorPane) parent.lookup("#pneContainer");
                    Navigation.init(pneContainer);
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Library Clone");
                    stage.show();

                    stage.centerOnScreen();
                    pgbLoading.getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timeline.getKeyFrames().addAll(keyFrame1,keyFrame2,keyFrame3,keyFrame4,keyFrame5);
        timeline.playFromStart();
    }

}
