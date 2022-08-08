package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import db.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UserDetailsFormController {
    public AnchorPane pneCard;
    public Label lblRegNumber;
    public Label lblFullName;
    public Label lblNIC;
    public Label lblAddress;
    public Label lblMobileNumber;
    public ImageView imgQR;
    public Button btnDownload;
    public Label lblClose;

    public void setData(User user) throws WriterException {
        lblAddress.setText(user.getAddress());
        lblFullName.setText(user.getFullName());
        lblRegNumber.setText(user.getRegistrationNumber());
        lblNIC.setText(user.getNic());
        lblMobileNumber.setText(user.getPhoneNumber());

        String secretWord=lblNIC.getText();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(secretWord, BarcodeFormat.QR_CODE, 139, 147);

        WritableImage writableImage = new WritableImage(139,147);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < bitMatrix.getWidth(); x++) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                if (bitMatrix.get(x,y)) pixelWriter.setColor(x,y, Color.BLACK);
                else pixelWriter.setColor(x,y,Color.WHITE);
            }
        }
        imgQR.setImage(writableImage);
    }

    public void btnDownloadOnAction(ActionEvent actionEvent) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getenv("HOME")));
        fileChooser.setInitialFileName("Member_ID_Card.png");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG File (*.png)","*.png"));
        File fileLocation = fileChooser.showSaveDialog(btnDownload.getScene().getWindow());

        BufferedImage bufferedQRImage = SwingFXUtils.fromFXImage(pneCard.snapshot(new SnapshotParameters(),null), null);
        boolean saved = ImageIO.write(bufferedQRImage, "png", fileLocation);
        if (saved){
            new Alert(Alert.AlertType.INFORMATION,"Successfully Downloaded").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Failed to download").show();
        }

    }

    public void lblOnMouseClicked(MouseEvent mouseEvent) {
        lblMobileNumber.getScene().getWindow().hide();
    }
}
