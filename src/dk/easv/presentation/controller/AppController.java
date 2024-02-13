package dk.easv.presentation.controller;

import dk.easv.entities.*;
import dk.easv.presentation.model.AppModel;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    @FXML
    private HBox favHbox;
    @FXML
    private MFXScrollPane spFav;
    @FXML
    private MFXScrollPane spRec;
    @FXML
    private MFXScrollPane spTop;
    @FXML
    private HBox recommendedHbox;
    @FXML
    private HBox top10Hbox;
    @FXML
    private Label title;
    private AppModel model;
    private long timerStartMillis = 0;
    private String timerMsg = "";
    private int currentIndexRecommended = 0;
    private int currentIndexTop10 = 0;
    private final int LOAD_COUNT = 10;
    private static final double SCROLL_AMOUNT = 550;

    private void startTimer(String message) {
        timerStartMillis = System.currentTimeMillis();
        timerMsg = message;
    }

    private void stopTimer() {
        System.out.println(timerMsg + " took : " + (System.currentTimeMillis() - timerStartMillis) + "ms");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setModel(AppModel model) {
        this.model = model;

        startTimer("Load users");
        model.loadUsers();
        stopTimer();
    }

    public void initializeWithUser(User currentUser) {
        if (currentUser != null) {
            updateMovieLists(currentUser);
        }
    }

    private void updateMovieLists(User currentUser) {
        List<Movie> recommendedMovies = model.getRecommendedMovies(currentUser);
        List<Movie> topMoviesNotSeen = model.getTopMoviesUserHasNotSeen(currentUser);
        List<Movie> favMovies = model.getTopMoviesUserHasSeen(currentUser);

        displayMovies(recommendedHbox, recommendedMovies);
        displayMovies(top10Hbox, topMoviesNotSeen);
        displayMovies(favHbox, favMovies);
    }

    private void displayMovies(HBox hbox, List<Movie> movies) {
        hbox.getChildren().clear(); // Clear previous content if any
        hbox.setSpacing(25);

        int displayLimit = Math.min(movies.size(), 10);
        for (int i = 0; i < displayLimit; i++) {
            Movie movie = movies.get(i);
            ImageView imageView = createImageView(movie);
            VBox labelOverlay = createLabelOverlay(movie);

            StackPane stackPane = new StackPane(imageView, labelOverlay);
            stackPane.setAlignment(Pos.BOTTOM_CENTER);

            hbox.getChildren().add(stackPane);
        }
    }

    private ImageView createImageView(Movie movie) {
        ImageView imageView = new ImageView(new Image(new File(movie.getImagePath()).toURI().toString(), true));
        imageView.setFitWidth(250); // Set the width of the movie image
        imageView.setFitHeight(150); // Set the height of the movie image
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    private VBox createLabelOverlay(Movie movie) {
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.getStyleClass().add("movie-title");

        VBox labelBox = new VBox(titleLabel);
        labelBox.setAlignment(Pos.BOTTOM_CENTER);
        labelBox.setPadding(new Insets(5));

        // Make sure the labelBox doesn't block the entire image view
        labelBox.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);

        return labelBox;
    }

    @FXML
    private void scrollLeftRec(ActionEvent actionEvent) {
        double hValue = spRec.getHvalue() - SCROLL_AMOUNT / spRec.getContent().getBoundsInLocal().getWidth();
        spRec.setHvalue(Math.max(hValue, 0)); // Ensure we don't scroll past the beginning
    }

    @FXML
    private void scrollRightRec(ActionEvent actionEvent) {
        double hValue = spRec.getHvalue() + SCROLL_AMOUNT / spRec.getContent().getBoundsInLocal().getWidth();
        spRec.setHvalue(Math.min(hValue, 1)); // Ensure we don't scroll past the end
    }

    @FXML
    private void scrollLeftTop(ActionEvent actionEvent) {
        double hValue = spTop.getHvalue() - SCROLL_AMOUNT / spTop.getContent().getBoundsInLocal().getWidth();
        spTop.setHvalue(Math.max(hValue, 0)); // Ensure we don't scroll past the beginning
    }

    @FXML
    private void scrollRightTop(ActionEvent actionEvent) {
        double hValue = spTop.getHvalue() + SCROLL_AMOUNT / spTop.getContent().getBoundsInLocal().getWidth();
        spTop.setHvalue(Math.min(hValue, 1)); // Ensure we don't scroll past the end
    }

    @FXML
    private void scrollLeftFav(ActionEvent actionEvent) {
        double hValue = spFav.getHvalue() - SCROLL_AMOUNT / spFav.getContent().getBoundsInLocal().getWidth();
        spFav.setHvalue(Math.max(hValue, 0)); // Ensure we don't scroll past the end
    }

    @FXML
    private void scrollRightFav(ActionEvent actionEvent) {
        double hValue = spFav.getHvalue() + SCROLL_AMOUNT / spFav.getContent().getBoundsInLocal().getWidth();
        spFav.setHvalue(Math.min(hValue, 1)); // Ensure we don't scroll past the end
    }

    @FXML
    private void onLogout() throws IOException {
        // Close the current window
        Stage stage = (Stage) favHbox.getScene().getWindow();
        stage.close();

        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
        Parent root = loader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(root));
        loginStage.setTitle("Login");
        loginStage.show();
    }

}
