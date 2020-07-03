package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class Main extends Application {
    public Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        controller = new Controller();

        Group group = new Group();

        Scene scene = new Scene(group, 300, 400);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 886, 750));
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);
    }

}
