package Home;

import Home.DTO.History;
import Home.Model.Client;
import Home.Model.Singleton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public FlowPane paneD;
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
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        for(History h : arr.histories){
            XYChart.Data<String,Number> day = new XYChart.Data<>(h.getLastUpdate(), h.getPrice());
            day.setNode(new HoveredThresholdNodea(h.getLastUpdate(), h.getPrice()));
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
        lineChart.setAnimated(true);
        lineChart.getData().add(series);
        date_end.setFocusTraversable(false);
        date_start.setFocusTraversable(false);
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
        String start = date_start.getEditor().getText();
        String end = date_end.getEditor().getText();
        if(start.matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}") && end.matches("[0-9]{1,2}(/|-)[0-9]{1,2}(/|-)[0-9]{4}")){
            String key = "id="+id+"&start="+date_start.getValue().toString()+"&end="+date_end.getValue().toString();
            Client.senData("fillter_history",key);
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            lineChart.getData().remove(0);
            for(History h : arr.histories){
                XYChart.Data<String,Number> day = new XYChart.Data<>(h.getLastUpdate(), h.getPrice());
                day.setNode(new HoveredThresholdNodea(h.getLastUpdate(), h.getPrice()));
                series.getData().addAll(day);
            }
            series.setName(arr.product.getName());
            lineChart.getData().add(series);
            maxPrice.setText(formatter.format(arr.product.getMax_price())+" Đ");
            minPrice.setText(formatter.format(arr.product.getMin_price())+" Đ");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Ngày tháng không hợp lệ");
            alert.showAndWait();
        }
    }
    public void handleOpenComment(ActionEvent event) throws IOException {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Comment.fxml"));
            Scene comment = new Scene(root);
            Stage window = new Stage();
            window.setScene(comment);
            window.setTitle("Theo dõi giá tiki");
            InputStream stream = new FileInputStream("src/icon/icons8_chart_increasing_with_yen_20px.png");
            window.getIcons().add(new Image(stream));
            window.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            window.initOwner(((Node)event.getTarget()).getScene().getWindow());
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class HoveredThresholdNodea extends StackPane {

        public HoveredThresholdNodea(String string, int object) {
            setPrefSize(5, 5);
            final Label label = createDataThresholdLabel(string, object);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                }
            });
        }

        private Label createDataThresholdLabel(String string, int object) {
            String prices = formatter.format(object)+"Đ";
            final Label label = new Label(prices + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
            label.setOpacity(0.8);
            if (string.equals("engine1")) {
                label.setTextFill(Color.RED);
                label.setStyle("-fx-border-color: #12A6F0;");
            } else if (string.equals("engine2")) {
                label.setTextFill(Color.ORANGE);
                label.setStyle("-fx-border-color: #12A6F0;");
            } else {
                label.setTextFill(Color.GREEN);
                label.setStyle("-fx-border-radius: 2;-fx-background-color: #DCDCDC;");
            }

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }
}
