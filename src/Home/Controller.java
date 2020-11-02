package Home;

import Home.DTO.Category;
import Home.Model.Client;
import Home.Model.Singleton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ObservableList<Category> categoryList = FXCollections.observableArrayList();
    public ObservableList<Product> dataProduct = FXCollections.observableArrayList();
    public TextField keyWord;
    public JFXComboBox<Category> listCategory;
    static Singleton arr = Singleton.getInstance();
    public static Client cl;
    static String idCategory;
    @FXML
    public Label totalProduct;
    public Label nameCategory;
    public Pagination pageHome;
    public JFXListView<Product> listProduct;
    public void handleDetail(ActionEvent event) throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("DetailProduct.fxml"));
        Scene detail = new Scene(root2);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(detail);
        window.show();
    }
    public void handelSearch(ActionEvent event){
        search(1);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       cl.senData("getAllCategory","");
       categoryList.add(new Category("1","Tất cả",""));
       for(Category a : arr.categories){
           categoryList.add(a);
       }
       String[] a = {"a","huythng","bbb"};
       listCategory.setItems(categoryList);
       pageHome.setPageCount(1);
       pageHome.currentPageIndexProperty().addListener((observableValue, oldpage, nextpage) -> {
           int page = (int) nextpage+1;
           System.out.println(page);
           search(page);
       });
    }
    public void search(int page){
        listProduct.getItems().clear();
        if(idCategory == null){
            idCategory = "1";
        }
        String key = "key="+keyWord.getText()+"&idCategory="+idCategory+"&page="+page;
        cl.senData("search",key);
        totalProduct.setText(String.valueOf(arr.page.getTotal())+" Sản phẩm");
        getNameCatogory();
        pageHome.setPageCount(arr.page.getTotal_page());
        pageHome.setCurrentPageIndex(page-1);
        addDataTable();
    }
    public void getNameCatogory(){
        if(idCategory.equals("1")){
            nameCategory.setText("Tất cả");
        }
        else{
            Category ca = listCategory.getSelectionModel().getSelectedItem();
            nameCategory.setText(ca.getName());
        }
    }
    public void getIdCategory(ActionEvent event){
        Category ca = listCategory.getSelectionModel().getSelectedItem();
        idCategory = ca.getId();
    }
    public void addDataTable(){
        for(Product pro : arr.products){
            dataProduct.add(pro);
        }
        listProduct.setCellFactory(param -> new Cell());
        listProduct.setItems(dataProduct);
    }
    static class Cell extends ListCell<Product>{
        public ImageView imageView = new ImageView();
        public HBox hbox = new HBox();
        public Label name = new Label();
        public Label price = new Label();
        public Button detail = new Button("Chi tiết");
        @FXML
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        public Cell(){
            super();
            //style image
            imageView.setFitHeight(120);
            imageView.setFitWidth(100);
            //end
            //style name
            name.setPrefWidth(420);
            name.setWrapText(true);
            name.setPadding(new Insets(0,0,0,20));
            name.setStyle("-fx-padding-left:20;");
            name.setFont(new Font("Helvetica",18));
            //end
            //stylePrice
            price.setPrefWidth(200);
            hbox.setFillHeight(true);
            price.setFont(new Font("Helvetica",18));
            price.setPadding(new Insets(0,0,0,15));
            price.setTextFill(Color.web("#ff3945"));
            price.setStyle("-fx-color:#ff3945;");
            //end
            //style button detail
            detail.setStyle("-fx-background-color: #0174DF;-fx-cursor: hand;");
            detail.setPrefHeight(40);
            detail.setPrefWidth(90);
            detail.setFont(new Font("Arial",16));
            detail.setTextFill(Color.WHITE);

            //end
            hbox.getChildren().addAll(imageView,name,price,detail);
            hbox.setAlignment(Pos.CENTER);
            detail.setOnAction(e -> {
                Product b = getListView().getItems().get(getIndex());
                cl.senData("detailProduct","id="+b.getId());
                DetailProduct de = new DetailProduct();
                de.idProduct(b.getId());
                loadDetail(b.getId());

            });

        }
        protected void updateItem(Product product, boolean b) {

            super.updateItem(product, b);
            setGraphic(null);
            setText(null);
            if (b || product == null || product.getName() == null) {
                setText(null);
            }
            else{

                Image image = new Image(product.getImage());
                imageView.setImage(image);
                name.setText(product.getName());
                String prices = formatter.format(product.getPrice())+" VNĐ";
                price.setText(prices);
                setGraphic(hbox);
            }
        }
        public void loadDetail(String id){
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("DetailProduct.fxml"));
                Scene detail = new Scene(root);
                Stage window = new Stage();
                window.setScene(detail);
                window.setTitle("Theo dõi giá tiki");
                InputStream stream = new FileInputStream("C:\\Users\\huyth\\IdeaProjects\\GiaoDienClientVS1\\src\\icon\\icons8_chart_increasing_with_yen_20px.png");
                window.getIcons().add(new Image(stream));
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


