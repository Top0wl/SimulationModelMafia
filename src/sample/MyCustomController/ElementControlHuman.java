package sample.MyCustomController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ElementControlHuman extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public HBox RedList;

    @FXML
    public HBox BlackList;

    @FXML
    public ImageView PlayerImage;

    @FXML
    public Label PlayerEloquence;

    @FXML
    public Label PlayerExperience;

    @FXML
    public Label PlayerLogic;

    @FXML
    public Label PlayerIntuition;

    @FXML
    public Label PlayerName;


    public ElementControlHuman() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyCustomController.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void AddInBlackList(int NumPerson) {
        Label l1 = new Label(Integer.toString(NumPerson + 1));
        l1.setStyle("-fx-border-color: Black;" + "-fx-border-width: 2;" + "-fx-text-fill: Black;");
        l1.setFont(new Font("System Bold", 13));
        BlackList.getChildren().add(l1);
    }

    public void AddInRedList(int NumPerson) {
        Label l1 = new Label(Integer.toString(NumPerson + 1));
        l1.setStyle("-fx-border-color: Black;" + "-fx-border-width: 2;" + "-fx-text-fill: Black;");
        l1.setFont(new Font("System Bold", 13));
        RedList.getChildren().add(l1);
    }

    public void DeleteInBlackList(int NumPerson, int Total) {
        Label l = new Label((NumPerson + 1) + "(" + Total + ")");
        String var1 = l.getText();

        for (var item: BlackList.getChildren()) {
            Label temp = (Label) item;
            String var2 = temp.getText();
            if(var1.equals(var2)) {
                BlackList.getChildren().remove(item);
                break;
            }
        }
    }

    public void DeleteInRedList(int NumPerson, int Total) {
        Label l = new Label((NumPerson + 1) + "(" + Total + ")");
        String var1 = l.getText();

        for (var item: RedList.getChildren()) {
            Label temp = (Label) item;
            String var2 = temp.getText();
            if(var1.equals(var2)) {
                RedList.getChildren().remove(item);
                break;
            }
        }
    }

    public void SwipeRedToBlack(int NumPerson, int Total) {
        Label l = new Label((NumPerson + 1) + "(" + Total + ")");
        l.setStyle("-fx-border-color: Black;" + "-fx-border-width: 2;" + "-fx-text-fill: Black;");
        l.setFont(new Font("System Bold", 13));
        String var1 = l.getText();
        for (var item: RedList.getChildren()) {
            Label temp = (Label) item;
            String var2 = temp.getText();
            if(var1.equals(var2)) {
                item.setStyle("-fx-border-color: Black;" + "-fx-border-width: 2;" + "-fx-text-fill: Black;");
                BlackList.getChildren().add(item);
                RedList.getChildren().remove(item);
                break;
            }
        }
    }

    public void SwipeBlackToRed(int NumPerson, int Total) {
        Label l = new Label((NumPerson + 1) + "(" + Total + ")");
        String var1 = l.getText();
        for (var item: BlackList.getChildren()) {
            Label temp = (Label) item;
            String var2 = temp.getText();
            if(var1.equals(var2)) {
                RedList.getChildren().add(item);
                BlackList.getChildren().remove(item);
            }
        }
    }
}
