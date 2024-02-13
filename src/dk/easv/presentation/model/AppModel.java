package dk.easv.presentation.model;

import dk.easv.entities.*;
import dk.easv.logic.LogicManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class AppModel {

    LogicManager logic = new LogicManager();
    // Models of the data in the view
    private final ObservableList<User>  obsUsers = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity>  obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();

    public void loadUsers(){
        obsUsers.clear();
        obsUsers.addAll(logic.getAllUsers());
    }

    public ObservableList<User> getObsUsers() {
        return obsUsers;
    }

    public ObservableList<Movie> getObsTopMovieSeen() {
        return obsTopMovieSeen;
    }

    public ObservableList<Movie> getObsTopMovieNotSeen() {
        return obsTopMovieNotSeen;
    }

    public ObservableList<UserSimilarity> getObsSimilarUsers() {
        return obsSimilarUsers;
    }

    public ObservableList<TopMovie> getObsTopMoviesSimilarUsers() {
        return obsTopMoviesSimilarUsers;
    }

    public User getObsLoggedInUser() {
        return obsLoggedInUser.get();
    }

    public SimpleObjectProperty<User> obsLoggedInUserProperty() {
        return obsLoggedInUser;
    }

    public void setObsLoggedInUser(User obsLoggedInUser) {
        this.obsLoggedInUser.set(obsLoggedInUser);
    }

    public boolean loginUserFromUsername(String userName) {
        User user = logic.getUser(userName);
        if (user != null){
            obsLoggedInUser.set(user);
            return true;
        } else {
            return false;
        }
    }

    public List<Movie> getRecommendedMovies(User currentUser) {
        // First, get the list of top movies from similar people, which are TopMovie objects
        List<TopMovie> topMovies = logic.getTopMoviesFromSimilarPeople(currentUser);

        // Then, convert the List<TopMovie> to List<Movie>
        return topMovies.stream()
                .map(TopMovie::getMovie) // This assumes TopMovie has a method getMovie()
                .collect(Collectors.toList());
    }

    public List<Movie> getTopMoviesUserHasNotSeen(User currentUser) {
        return logic.getTopAverageRatedMoviesUserDidNotSee(currentUser);
    }

    public List<Movie> getTopMoviesUserHasSeen(User currentUser){
        return logic.getTopAverageRatedMovies(currentUser);
    }
}
