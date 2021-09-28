package app.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Battleship");
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("welcome.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
//        stage.setTitle("Battleship");
//        stage.setScene(scene);
        scene.setOnKeyPressed(keyEvent -> {
            switch(keyEvent.getCode()){
                case ENTER -> {
                    System.out.println("Twoj stary kliknal enter");
                    break;
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}