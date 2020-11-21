package Home;

import Home.Model.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Load implements Initializable {
    @FXML
    public AnchorPane loadPane;
    @FXML
    public ImageView imgLoad;
    @FXML
    public VBox error;
    public static String ip = "10.0.5.12";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new load().start();
    }
    public void handleConnect(ActionEvent event){
        imgLoad.setVisible(true);
        error.setVisible(false);
        new load().start();
    }
    public void closeSession(ActionEvent event){
        System.exit(0);
    }
    class load extends Thread{
        public Boolean connect(){
            Boolean con = false;
            try {
                new Client(ip,5002);
                con = true;
            } catch (IOException e) {
                con = false;
            }
            return con;
        }
        public void loadHome(){
            if(connect()){
                try {
                    Stage window = new Stage();
                    Parent root2 = FXMLLoader.load(getClass().getResource("Home.fxml"));
                    window.setTitle("Theo dõi giá tiki");
                    InputStream stream = new FileInputStream("C:\\Users\\huyth\\IdeaProjects\\GiaoDienClientVS1\\src\\icon\\icons8_chart_increasing_with_yen_20px.png");
                    window.getIcons().add(new Image(stream));
                    Scene detail = new Scene(root2);
                    window.setScene(detail);
                    window.show();
                    loadPane.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Ket noi that bai");
                imgLoad.setVisible(false);
                error.setVisible(true);
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
