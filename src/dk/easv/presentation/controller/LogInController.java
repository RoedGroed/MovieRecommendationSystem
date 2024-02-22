package dk.easv.presentation.controller;

import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private TextField userId;
    private AppModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new AppModel();
    }

    public void logIn(ActionEvent actionEvent) {
        model.loadUsers();
        boolean loginSuccessful = model.loginUserFromUsername(userId.getText());

        if(loginSuccessful){
            User currentUser = model.getObsLoggedInUser();
            openAppWindow(currentUser);

            Stage closingStage = (Stage) userId.getScene().getWindow();
            closingStage.close();

        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username or password");
            alert.showAndWait();
        }
    }

    private void openAppWindow(User currentUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/presentation/view/App.fxml"));
            Parent root = loader.load();

            AppController appController = loader.getController();
            appController.setModel(model); // Pass the model to the app controller
            appController.initializeWithUser(currentUser); // Initialize with the current user

            Stage stage = new Stage();
            //stage.setFullScreen(true);
            stage.setScene(new Scene(root));
            stage.setTitle("Movie Recommendation System 0.01 Beta");
            stage.show();

            // Close the login window (if open)
            // ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
            alert.showAndWait();
        }
    }

    public void signUp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sign-up closed, We're currently at user capacity. " +
                "\rPremium users get access instantly.");
        alert.showAndWait();
    }

}
