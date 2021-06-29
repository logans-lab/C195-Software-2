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
import software2.dbAccess.DBReports;
import software2.model.Reports;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static software2.controller.LoginFormController.dateTimeFormatter;

/**
 * View reports controller class.
 * @author logan
 */
public class ViewReportsController implements Initializable {

    @FXML private Label reportLabel; // main label
    @FXML private Label reportLabel1; // month
    @FXML private Label reportLabel2; // type
    @FXML private Label reportLabel3; // count
    ObservableList<Reports> reportsList;

    /**
     * On load, no data loaded in display. Data loaded on buttons pressed within view.
     * @param url The location used to resolve relative paths for the root object, or null if the location
     *        is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was
     *                       not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // empty
    }

    /**
     * On customer appointments report button pressed, appends each column into separate StringBuilders and
     * displays report of customer appointments by type and month.
     */
    public void customerAppointmentsReport() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        reportLabel.setText("");
        reportLabel1.setText("");
        reportLabel2.setText("");
        reportLabel3.setText("");
        reportsList = DBReports.getCustomerAppointments();

        reportLabel.setText("Customer Appointments By Type and Month Report: \n\n");

        sb1.append("Month:\n");
        for (Reports reports : reportsList) {
            sb1.append(reports.getMonthName()).append("\n");
        }
        reportLabel1.setText(sb1.toString());

        sb2.append("Type:\n");
        for (Reports reports : reportsList) {
            sb2.append(reports.getType()).append("\n");
        }
        reportLabel2.setText(sb2.toString());

        sb3.append("Count:\n");
        for (Reports reports : reportsList) {
            sb3.append(reports.getAppointmentCount()).append("\n");
        }
        reportLabel3.setText(sb3.toString());
    }

    /**
     * On contact schedule report button pressed, separates appointments by contact and appends to StringBuilder.
     * Then displays report of the appointments scheduled by each contact.
     */
    public void contactScheduleReport() {
        reportLabel.setText("");
        reportLabel1.setText("");
        reportLabel2.setText("");
        reportLabel3.setText("");
        reportsList = DBReports.getContactSchedule();
        StringBuilder sb = new StringBuilder();

        sb.append("Appointments Schedule By Contact Report: \n");
        int id = 0;
        for (Reports r : reportsList) {
            if (r.getContactId() != id) {
                sb.append("\n" + r.getContactName().toUpperCase() + "\n");
                sb.append("Appt. ID: " + r.getAppointmentId() + " \t Title: " + r.getTitle() + " \t Type: " +
                        r.getType() + " \t Desc: " + r.getDescription() + " \t Start: " +
                        dateTimeFormatter.format(r.getStart().toLocalDateTime()) + " \t End: " +
                        dateTimeFormatter.format(r.getEnd().toLocalDateTime()) + " \t Customer ID: " +
                        r.getCustomerId() + "\n");
                id = r.getContactId();
            } else {
                sb.append("Appt. ID: " + r.getAppointmentId() + " \t Title: " + r.getTitle() + " \t Type: " +
                        r.getType() + " \t Desc: " + r.getDescription() + " \t Start: " +
                        dateTimeFormatter.format(r.getStart().toLocalDateTime()) + " \t End: " +
                        dateTimeFormatter.format(r.getEnd().toLocalDateTime()) + " \t Customer ID: " +
                        r.getCustomerId() + "\n");
            }
        }
        reportLabel.setText(sb.toString());
    }

    /**
     * On user appointments report button pressed, displays report of the number of appointments scheduled for the
     * number of customers, which were added by each user.
     * <p></p>
     * One lambda is added here which iterates over each object in reportsList and appends the text to the
     * StringBuilder.
     */
    public void userAppointmentsReport() {
        reportLabel.setText("");
        reportLabel1.setText("");
        reportLabel2.setText("");
        reportLabel3.setText("");
        reportsList = DBReports.getUserAppointments();
        System.out.println(reportsList.toArray().length);
        StringBuilder sb = new StringBuilder();
        sb.append("Appointments Added By User Report:" + "\n");

        reportsList.forEach(r -> sb.append("\n" + r.getUserName().toUpperCase() + "\n" +
                r.getAppointmentCount() + " appointments scheduled for " + r.getCustomerCount() + " customers\n"));
        reportLabel.setText(sb.toString());
    }

    /**
     * On view appointments button pressed, loads appointment view.
     * @param actionEvent For changing to next scene upon button click.
     * @throws IOException For failed or interrupted I/O operations.
     */
    public void viewAppointmentsButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/software2/view/ViewAppointments.fxml"));
        Scene mainScene = new Scene(scene);
        stage.setScene(mainScene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * On view customers button pressed, loads customer view.
     * @param actionEvent For changing to next scene upon button click.
     * @throws IOException For failed or interrupted I/O operations.
     */
    public void viewCustomersButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/software2/view/ViewCustomers.fxml"));
        Scene mainScene = new Scene(scene);
        stage.setScene(mainScene);
        stage.centerOnScreen();
        stage.show();
    }
}
