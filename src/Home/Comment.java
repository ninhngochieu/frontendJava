package Home;

import Home.Model.Singleton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Comment implements Initializable {
    @FXML
    public JFXListView<CommentDTO> listComments;
    public TableView<CommentDTO> tableComment;
    public TableColumn<CommentDTO,String> nameUser;
    public TableColumn<CommentDTO,String> dateComment;
    public TableColumn<CommentDTO,Float> reviewStar;
    public TableColumn<CommentDTO,String> title;
    public TableColumn<CommentDTO,String> content;
    public TableColumn<CommentDTO,String> like;
    static Singleton arr = Singleton.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<CommentDTO> dataCommnet = FXCollections.observableArrayList();
        for(CommentDTO comment : arr.commentDTOS){
            dataCommnet.add(comment);
        }
        listComments.setCellFactory(param -> new Comment.Cell());
        listComments.setItems(dataCommnet);

    }
    static class Cell extends ListCell<CommentDTO> {
        public VBox vBox = new VBox();
        public HBox info = new HBox();
        public VBox infoUser = new VBox();
        public ImageView avatar = new ImageView();
        public Label fullname = new Label();
        public Label dateComment = new Label();
        public Label reviewStar = new Label();
        public Label title = new Label();
        public Label content = new Label();
        public Label like = new Label();
        public Rating rating = new Rating();
        public Cell(){
            super();
            fullname.setFont(new Font("Helvetica",14));
            fullname.setTextFill(Color.web("rgb(36, 36, 36)"));

            dateComment.setTextFill(Color.web("rgb(153, 153, 153)"));
            dateComment.setFont(new Font("Helvetica",12));

            infoUser.setAlignment(Pos.CENTER_LEFT);
            infoUser.setPadding(new Insets(0,0,0,12));

            rating.setPadding(new Insets(0,0,0,5));
            rating.setPrefWidth(10);
            rating.setPrefHeight(10);

            title.setPadding(new Insets(0,0,0,5));
            title.setFont(Font.font("Helvetica", FontWeight.BOLD,16));

            content.setPadding(new Insets(0,0,0,5));
            content.setFont(new Font("Helvetica",13));
            content.setWrapText(true);

            like.setPadding(new Insets(5,5,5,5));
            like.setFont(new Font("Helvetica",16));
            like.setStyle("-fx-border-color: rgb(13, 92, 182);");

            try {
                InputStream stream = new FileInputStream("src/icon/icons8_male_user_50px.png");
                Image image = new Image(stream);
                avatar.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            infoUser.getChildren().addAll(fullname,dateComment);
            info.getChildren().addAll(avatar,infoUser);
            vBox.getChildren().addAll(info,rating,title,content
            ,like);
            vBox.setAlignment(Pos.CENTER_LEFT);

        }
        protected void updateItem(CommentDTO commentDTO, boolean b) {

            super.updateItem(commentDTO, b);
            setGraphic(null);
            setText(null);
            if (b || commentDTO == null || commentDTO.getFull_name() == null) {
                setText(null);
            }
            else{
                fullname.setText(commentDTO.getFull_name());
                dateComment.setText("Nhận xét vào ngày "+commentDTO.getPurchased_at());
                rating.setPartialRating(true);
                rating.setRating(commentDTO.getRating());
                title.setText(commentDTO.getTitle());
                content.setText(commentDTO.getContent());
                String likes = String.valueOf(commentDTO.getThank_count());
                like.setText("Lượt like "+ likes);
                setGraphic(vBox);
            }
        }
    }
}
