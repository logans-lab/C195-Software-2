package software2.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import software2.dbAccess.*;
import software2.model.*;
import software2.utils.DBConnection;

/**
 * Main class. Extends Application as this is a GUI application.
 * @author logan
 */
public class Main extends Application {

    /**
     * Start method loads the login view.
     * @param stage Creates new stage for the login form.
     * @throws Exception For failed or interrupted operations.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/software2/view/LoginForm.fxml"));
        Scene loginForm = new Scene(root);
        stage.setScene(loginForm);
        stage.show();
    }

    /**
     * Main method to start application. First, connects to the database, then loads all read-only table information
     * into ObservableLists for use throughout the application. Launch application. Finally, closes connection on exit.
     * @param args Standard input.
     */
    public static void main(String[] args) {
        DBConnection.startConnection();
        Countries.setAllCountries(DBCountries.getAllCountries());
        FirstLevelDivisions.setAllDivisions(DBFirstLevelDivisions.getAllDivisions());
        Users.setAllUsers(DBUsers.getAllUsers());
        Contacts.setAllContacts(DBContacts.getAllContacts());
        launch(args);
        DBConnection.closeConnection();
    }
}
