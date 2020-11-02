package Home;

import Home.DTO.CommentDTO;
import Home.Model.Singleton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Comment implements Initializable {
    @FXML
    public Label nameCustomer;
    public Label dateSumitted;
    public Label level;
    public Label Star;
    public Label contentCommnet;
    public JFXListView<CommentDTO> listComment;
    public VBox itemContent;
    static Singleton arr = Singleton.getInstance();
    public ObservableList<CommentDTO> dataCommnet = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(CommentDTO comment : arr.commentDTOS){

            dataCommnet.add(comment);
        }
    }
    static class Cell extends ListCell<CommentDTO>{
        @FXML
        public Label nameCustomer;
        public Label dateSumitted;
        public Label level;
        public Label Star;
        public Label contentCommnet;
        public VBox itemContent;
    }
}
