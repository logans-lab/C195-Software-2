package software2.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import software2.dbAccess.DBAppointments;
import software2.dbAccess.DBPassword;
import software2.model.Appointments;

import java.io.*;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * LoginFormController class
 * @author logan
 */
public class LoginFormController implements Initializable {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;
    @FXML private Label zoneIdLabel;
    @FXML private Label username;
    @FXML private Label password;
    @FXML private Button loginButton;
    @FXML private Label loginLabel;

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    public static int timeDifference;
    public static int startTime = 8;
    public static int endTime = 22;

    Alert alert;
    ResourceBundle rb;

    /**
     * On load, determines user's time zone and calculates start and end times for business hours that are the same
     * as 8:00 am to 10:00 pm EST, which are utilized in ViewAppointmentsController.java for loading the start
     * and end time combo boxes.
     * <p></p>
     * Also user's system language is determined and the login screen is shown with that language utilizing
     * resource bundles. English and French are available, otherwise defaulting to English.
     * @param url The location used to resolve relative paths for the root object, or null if the location
     *            is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was
     *                       not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // used to test french language and alternate timezones
//        Locale.setDefault(new Locale("fr"));
//        TimeZone.setDefault(TimeZone.getTimeZone("Europe/France"));
//        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
//        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));

        // system zone id
        ZoneId currentZoneId = ZoneId.of(TimeZone.getDefault().getID());

        // current time zone of user's system
        ZonedDateTime currentTimeZone = ZonedDateTime.of(LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalTime(), currentZoneId);

        // current time zone converted to UTC
        Instant currentToUTC = currentTimeZone.toInstant();

        // converts system default to East Coast Time
        ZonedDateTime utcToEST = currentTimeZone.withZoneSameInstant(ZoneId.of("America/New_York"));

        // converts UTC to local
        ZonedDateTime utcToLocal = currentToUTC.atZone(currentZoneId);

        // gets timezone offset and calculates clock offset for appointment working hours combo boxes
        timeDifference = utcToLocal.getHour() - utcToEST.getHour();
        startTime += timeDifference;
        endTime += timeDifference;
        if (timeDifference > 2) {
            endTime -= 24;
        } else if (timeDifference < -8) {
            startTime += 24;
        }

        // sets labels and buttons based on user's language using resource bundles
        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
            try {
                rb = ResourceBundle.getBundle("software2/utils/Appts", Locale.getDefault());
                username.setText(rb.getString("username"));
                password.setText(rb.getString("password"));
                loginLabel.setText(rb.getString("login"));
                loginButton.setText(rb.getString("login"));
                zoneIdLabel.setText(ZoneId.systemDefault().getId());

            } catch (MissingResourceException e) {
                e.printStackTrace();
            }
        } else { // if not english or french, set default to english
            try {
                Locale.setDefault(new Locale("en"));
                rb = ResourceBundle.getBundle("software2/utils/Appts", Locale.getDefault());
                username.setText(rb.getString("username"));
                password.setText(rb.getString("password"));
                loginLabel.setText(rb.getString("login"));
                loginButton.setText(rb.getString("login"));
                zoneIdLabel.setText(ZoneId.systemDefault().getId());

            } catch (MissingResourceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On login button click, data in text fields (username and password) are validated, otherwise giving an error
     * alert. Upon validation, an alert is displayed showing if the user has an upcoming appointment. A record of
     * login attempt gets written to "login_activity.txt" and the appointments view is loaded.
     * @param actionEvent For changing to next scene upon button click.
     * @throws IOException For failed or interrupted I/O operations.
     */
    public void loginButton(ActionEvent actionEvent) throws IOException {
        String user = usernameInput.getText();
        String pass = passwordInput.getText();
        boolean loginSuccess = DBPassword.getPassword(user, pass);

        String file = "login_activity.txt";
        PrintWriter txtFile = null;

        ObservableList<Appointments> appointments = DBAppointments.getAllAppointments();
        List<Appointments> tempList = new ArrayList<>();
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Appointment");
        alert.setHeaderText("Upcoming Appointment");

        // find upcoming appointments
        for (Appointments a : appointments) {
            long diff = ChronoUnit.MINUTES.between(LocalDateTime.now().toLocalTime(),
                    a.getStart().toLocalDateTime().toLocalTime());

            if (a.getUserName().equals(user) && diff > 0 && diff <= 15) {
                tempList.add(a);
            }
        }

        if (tempList.isEmpty()) {
            alert.setContentText(user.toUpperCase() + " doesn't have any upcoming appointments.");
        } else { // print last upcoming appointment in list
            alert.setContentText(user.toUpperCase() + " has an upcoming appointment\nAppointment ID: " +
                    tempList.get(tempList.size() - 1).getAppointmentId() +
                    " At: " + dateTimeFormatter.format(tempList.get(tempList.size() - 1).getStart().toLocalDateTime()));
        }

        if (loginSuccess) { // credentials valid
            // store login success in txt file
            try {
                txtFile = new PrintWriter(new FileWriter(file, true));
                txtFile.println("SUCCESS -> User: " + user + " At: " + Date.from(Instant.now()));
                txtFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // alert appts within 15 minutes or no appts
            alert.showAndWait();

            // load stage
            System.out.println("Logged in");
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(getClass().getResource("/software2/view/ViewAppointments.fxml"));
            Scene mainScene = new Scene(scene);
            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.show();

        } else { // credentials not valid
            // store login failure in txt file
            try {
                txtFile = new PrintWriter(new FileWriter(file, true));
                txtFile.println("FAILURE -> User: " + user + " At: " + Date.from(Instant.now()));
                txtFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // alert login failure
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("title"));
            alert.setHeaderText(rb.getString("header"));
            alert.setContentText(rb.getString("content"));
            alert.showAndWait();
        }
    }
}
