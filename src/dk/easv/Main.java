package dk.easv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("presentation/view/LogIn.fxml"));
        primaryStage.setTitle("Movie Recommendation System 0.01 Beta");
        // primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root));

        /*
        Makes the login window unscalable.
        primaryStage.setMinWidth(914);
        primaryStage.setMaxWidth(914);
        primaryStage.setMinHeight(512);
        primaryStage.setMaxHeight(512);
        */


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
