package Home;

import Home.DTO.History;
import Home.Model.Client;
import Home.Model.Singleton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class DetailProduct implements Initializable {
    Singleton arr = Singleton.getInstance();
    static String id;
    @FXML
    public AnchorPane paneDetail;
    public AreaChart<String,Number> lineChart;
    public ImageView image;
    public Label nameProduct;
    public Label priceProduct;
    public DatePicker date_start;
    public DatePicker date_end;
    public Label minPrice;
    public Label maxPrice;
    DecimalFormat formatter = new DecimalFormat("###,###,###");
    public void handleBackHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene detail = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    }
    public void idProduct(String idc){
        id = idc;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberAxis yAxis = new NumberAxis();
        CategoryAxis xAxis = new CategoryAxis();
        yAxis.setForceZeroInRange(false);
        xAxis.setAnimated(false);
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        for(History h : arr.histories){
            XYChart.Data<String,Number> day = new XYChart.Data<>(h.getLastUpdate(), h.getPrice());
            series.getData().addAll(day);
        }
        Image img = new Image(arr.product.getImage());
        image.setImage(img);
        nameProduct.setText(arr.product.getName());
        String prices = formatter.format(arr.product.getPrice())+" VNĐ";
        priceProduct.setText(prices);
        maxPrice.setText(formatter.format(arr.product.getMax_price())+" Đ");
        minPrice.setText(formatter.format(arr.product.getMin_price())+" Đ");
        series.setName(arr.product.getName());
        lineChart.setAnimated(false);
        lineChart.getData().add(series);
    }
    public void handleWebView(ActionEvent event){
        Runtime rt = Runtime.getRuntime();
        String url = "https://tiki.vn/p"+id+".html";
        try {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleFillterDate(ActionEvent event){
        String start = date_start.getValue().toString();
        String end = date_end.getValue().toString();
        String key = "id="+id+"&start="+start+"&end="+end;
        Client.senData("fillter_history",key);
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        lineChart.getData().remove(0);
        for(History h : arr.histories){
            XYChart.Data<String,Number> day = new XYChart.Data<>(h.getLastUpdate(), h.getPrice());
            series.getData().addAll(day);
        }
        series.setName(arr.product.getName());
        lineChart.getData().add(series);
        maxPrice.setText(formatter.format(arr.product.getMax_price())+" Đ");
        minPrice.setText(formatter.format(arr.product.getMin_price())+" Đ");
    }
    public void reloadSecne(){

    }
    public void handleOpenComment(ActionEvent event) throws IOException {
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("Comment.fxml"));
        paneDetail.getChildren().add(newLoadedPane);
    }
}
