package Home;

import Home.Model.Client;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Load.fxml"));
        InputStream stream = new FileInputStream("C:\\Users\\huyth\\IdeaProjects\\GiaoDienClientVS1\\src\\icon\\icons8_chart_increasing_with_yen_20px.png");
        primaryStage.getIcons().add(new Image(stream));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
