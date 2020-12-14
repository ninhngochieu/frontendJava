package Home;

import Home.Model.Client;
import Home.Model.RSA;
import Home.Model.Singleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Load implements Initializable {
    @FXML
    public AnchorPane loadPane;
    @FXML
    public ImageView imgLoad;
    public VBox error;
    public TextField ipaddress;
    public static String ip = "192.168.1.74";
    private static double xOffset = 0;
    private static double yOffset = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        getOldIP();
        RSA.genaKeyPari();
        //new load().start();

    }
    private void getOldIP(){
        try {
            Scanner sc = new Scanner(new File("src//Home//OldIP.txt"));
            ipaddress.setText(sc.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setOldIP() {
        try {
            FileWriter w = new FileWriter("src//Home//OldIP.txt");

            w.write(ipaddress.getText());
            w.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleConnect(ActionEvent event){
        if(!ipaddress.getText().matches("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Ip không hợp lệ vd : 192.168.1.1");
            alert.showAndWait();
        }
        else{
            imgLoad.setVisible(true);
            error.setVisible(false);
            ipaddress.setVisible(false);
            ip = ipaddress.getText();
            new load().start();
        }
    }
    public void closeSession(ActionEvent event){
        System.exit(0);
    }
    class load extends Thread{
        public void loadHome(){
            Client cl = new Client();
            if(cl.startClient(ip,5003)){
                try {
                    Client.senData("keyPRP","publicKey="+Singleton.publicKey);
                    Stage window = new Stage();
                    Parent root2 = FXMLLoader.load(getClass().getResource("Home.fxml"));
                    window.setTitle("Theo dõi giá tiki");
                    window.resizableProperty().setValue(false);
                    InputStream stream = new FileInputStream("src/icon/icons8_chart_increasing_with_yen_20px.png");
                    window.getIcons().add(new Image(stream));
                    Scene detail = new Scene(root2);
                    window.setScene(detail);
                    window.show();
                    loadPane.getScene().getWindow().hide();
                    setOldIP();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Ket noi that bai");
                imgLoad.setVisible(false);
                error.setVisible(true);
                ipaddress.setVisible(true);
            }
        }
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //new Client("192.168.2.52",5002);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    loadHome();
                }
            });
            super.run();
        }
    }
}
