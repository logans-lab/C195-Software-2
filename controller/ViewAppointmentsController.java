package software2.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import software2.dbAccess.DBAppointments;
import software2.dbAccess.DBCustomers;
import software2.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

import static software2.controller.LoginFormController.*;

/**
 * View Appointment Controller class
 * @author logan
 */
public class ViewAppointmentsController implements Initializable {

    @FXML private TableView<Appointments> appointmentsTable;
    @FXML private TableColumn<Appointments, Integer> appointmentsId;
    @FXML private TableColumn<Appointments, String> appointmentsTitle;
    @FXML private TableColumn<Appointments, String> appointmentsDescription;
    @FXML private TableColumn<Appointments, String> appointmentsLocation;
    @FXML private TableColumn<Appointments, String> appointmentsContact;
    @FXML private TableColumn<Appointments, String> appointmentsType;
    @FXML private TableColumn<Appointments, String> appointmentsStart;
    @FXML private TableColumn<Appointments, String> appointmentsEnd;
    @FXML private TableColumn<Customers, String> appointmentsCustomerName;

    @FXML private RadioButton viewAllAppointments;
    @FXML private RadioButton viewMonthAppointments;
    @FXML private RadioButton viewWeekAppointments;

    @FXML private TextField apptId;
    @FXML private TextField apptTitle;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private ComboBox<Contacts> apptContact;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ComboBox<LocalTime> apptStartTime;
    @FXML private DatePicker apptEndDate;
    @FXML private ComboBox<LocalTime> apptEndTime;
    @FXML private ComboBox<Customers> apptCustomer;
    @FXML private ComboBox<Users> apptUser;

    Alert alert;
    ObservableList<Users> usersList = Users.getAllUsers();
    ObservableList<Contacts> contactsList = Contacts.getAllContacts();

    /**
     * On load, tableview of appointments is loaded. Sets toggle group for 3 views: all appointments, appointments
     * within the month (30 days), and appointments within the week (7 days). Also loads combo box options for
     * customers, users, and contacts.
     * <p></p>
     * Two lambdas are used in setting the appointment tableview for the start date/time and end date/time. These both
     * utilize formatting from the dateTimeFormatter and set the cell value factory for each column, making the
     * date/time easier to read.
     * <p></p>
     * Two lambdas are added utilizing listeners. One lambda is on the toggle group to re-load appointments as needed
     * on each toggle selection. The second lambda is on the start time combo box to load available end times that only
     * come after the chosen start time. This prevents the user from selecting an end time that is before or equal to
     * the start time. The time combo boxes also display converted available local times that correspond with the
     * business hours of 8:00 am to 10:00 pm EST, eliminating the option for the user to select a time that is not
     * a working hour as per headquarters business hours in EST time zone.
     * <p></p>
     * Two lambdas were added on the datepickers for start and end dates. These ensured that the selected appointment
     * from the table fully populated the datepickers with the associated dates.
     * @param url The location used to resolve relative paths for the root object, or null if the location
     *        is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was
     *                       not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set appointment table
        appointmentsId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentsTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentsDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentsLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentsContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentsType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentsStart.setCellValueFactory((TableColumn.CellDataFeatures<Appointments, String> t) ->
                new ReadOnlyObjectWrapper<>(t.getValue().getStart().toLocalDateTime().format(dateTimeFormatter)));
        appointmentsEnd.setCellValueFactory((TableColumn.CellDataFeatures<Appointments, String> t) ->
                new ReadOnlyObjectWrapper<>(t.getValue().getEnd().toLocalDateTime().format(dateTimeFormatter)));
        appointmentsCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentsTable.setItems(DBAppointments.getAllAppointments());
        appointmentsTable.getSortOrder().add(appointmentsStart);

        // set appointment views
        ToggleGroup appointmentsTG = new ToggleGroup();
        this.viewAllAppointments.setToggleGroup(appointmentsTG);
        this.viewMonthAppointments.setToggleGroup(appointmentsTG);
        this.viewWeekAppointments.setToggleGroup(appointmentsTG);
        viewAllAppointments.setSelected(true);

        // refresh appointment views based on toggle with lambda
        appointmentsTG.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (t1.equals(viewAllAppointments)) {
                appointmentsTable.setItems(DBAppointments.getAllAppointments());
                appointmentsTable.getSortOrder().add(appointmentsStart);
            } else if (t1.equals(viewMonthAppointments)) {
                appointmentsTable.setItems(DBAppointments.getAppointmentsByTime(30));
                appointmentsTable.getSortOrder().add(appointmentsStart);
            } else if (t1.equals(viewWeekAppointments)) {
                appointmentsTable.setItems(DBAppointments.getAppointmentsByTime(7));
                appointmentsTable.getSortOrder().add(appointmentsStart);
            }
        });

        // load combo boxes
        Customers.setAllCustomers(DBCustomers.getAllCustomers());
        apptCustomer.setItems(Customers.getAllCustomers());
        apptUser.setItems(usersList);
        apptContact.setItems(contactsList);

        // load start time combo box
        LocalTime open = LocalTime.of(startTime,0);
        LocalTime localOpen = open; // local variable for use within lambda
        LocalTime close = LocalTime.of(endTime,0);
        LocalTime night = LocalTime.of(23,45);
        LocalTime midnight = LocalTime.of(0, 0);

        if (startTime < endTime) { // business hours are within same day
            while (open.isBefore(close)) {
                apptStartTime.getItems().add(open);
                open = open.plusMinutes(15);
            }
        } else if (startTime > endTime) { // business hours loops past midnight
            ObservableList<LocalTime> startList = FXCollections.observableArrayList();

            while (open.isBefore(night)) {
                startList.add(open);
                open = open.plusMinutes(15);
            }
            startList.add(night);
            while (midnight.isBefore(close)) {
                startList.add(midnight);
                midnight = midnight.plusMinutes(15);
            }
            apptStartTime.setItems(startList);
        }

        // listener to only display appt end times after selected start time with lambda for converting business hours
        apptStartTime.getSelectionModel().selectedItemProperty().addListener((observableValue, localTime, t1) -> {
            ObservableList<LocalTime> endTimesList = FXCollections.observableArrayList();
            LocalTime localClose = close.plusMinutes(15); // local variable within lambda
            LocalTime localMidnight = LocalTime.of(0,0); // local variable within lambda
            apptEndTime.getSelectionModel().clearSelection();
            apptEndTime.getItems().removeAll();

            if (!apptStartTime.getSelectionModel().isEmpty()) {
                if (startTime < endTime) { // business hours are within same day
                    while (t1.isBefore(close)) {
                        endTimesList.add(t1.plusMinutes(15));
                        t1 = t1.plusMinutes(15);
                    }
                    apptEndTime.setItems(endTimesList);
                } else if (startTime > endTime) { // business hours loop past midnight
                    if (t1.isBefore(night) && (t1.equals(localOpen) || t1.isAfter(localOpen))) {
                        // after start & before 23:45
                        while (t1.isBefore(night)) {
                            endTimesList.add(t1.plusMinutes(15));
                            t1 = t1.plusMinutes(15);
                        }
                        while (localMidnight.isBefore(close.plusMinutes(15))) {
                            endTimesList.add(localMidnight);
                            localMidnight = localMidnight.plusMinutes(15);
                        }
                    } else if (t1.equals(night)) { // time selected is 23:45
                        while (localMidnight.isBefore(localClose)) {
                            endTimesList.add(localMidnight);
                            localMidnight = localMidnight.plusMinutes(15);
                        }
                    } else if (t1.isBefore(close) && (t1.equals(localMidnight) || t1.isAfter(localMidnight))) {
                        // time selected is midnight or after
                        while (t1.isBefore(close)) {
                            endTimesList.add(t1.plusMinutes(15));
                            t1 = t1.plusMinutes(15);
                        }
                    }
                    apptEndTime.setItems(endTimesList);
                }
            }
        });

        // date lambda lambdas to load date. rarely the datepickers were blank after clicking "Edit".
        apptStartDate.setOnAction(e -> apptStartDate.setValue(apptStartDate.getValue()));
        apptEndDate.setOnAction(e -> apptEndDate.setValue(apptEndDate.getValue()));
    }

    /**
     * Method used for checking overlap between existing appointments and the new or updated appointment for the
     * customer.
     * @param appointmentsList List of current appointments. Used to compare all of the customer's appointments.
     * @param customerId Customer ID used to search the list of appointments.
     * @param start Start timestamp for determining overlap.
     * @param end End timestamp for determining overlap.
     * @return true if there is an overlapping appointment date/time, otherwise false.
     */
    public boolean checkAppointmentOverlap(ObservableList<Appointments> appointmentsList, Customers customerId,
                                           Timestamp start, Timestamp end) {
        // if overlapping appts return true
        for (Appointments a : appointmentsList) {
            if (a.getCustomerId() == customerId.getCustomerId()) {
                if (a.getEnd().after(start) && a.getStart().before(end)) {
                    return true;
                }
            }
        }
        // no overlapping appts return false
        return false;
    }

    /**
     * On add appointment button pressed, performs check for missing information, existing appointment overlap for
     * customer, adds appointment to list, refreshes tableview, and clears text fields.
     */
    public void addAppointmentButton() {
        if (apptTitle.getText() == null ||
                apptDescription.getText() == null ||
                apptLocation.getText() == null ||
                apptType.getText() == null ||
                apptStartDate.getValue() == null ||
                apptStartTime.getValue() == null ||
                apptEndDate.getValue() == null ||
                apptEndTime.getValue() == null ||
                apptCustomer.getValue() == null ||
                apptUser.getValue() == null ||
                apptContact.getValue() == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Fill out the form completely to add a new appointment.");
            alert.showAndWait();
            return; // do nothing. prevent exception
        }

        String title = apptTitle.getText();
        String desc = apptDescription.getText();
        String loc = apptLocation.getText();
        String type = apptType.getText();
        Timestamp start = Timestamp.valueOf(LocalDateTime.of(apptStartDate.getValue(),
                apptStartTime.getSelectionModel().getSelectedItem()));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(apptEndDate.getValue(),
                apptEndTime.getSelectionModel().getSelectedItem()));
        Customers custId = apptCustomer.getValue();
        Users userId = apptUser.getValue();
        Contacts contactId = apptContact.getValue();

        Appointments.setAllAppointments(DBAppointments.getAllAppointments());
        ObservableList<Appointments> appointmentsList = Appointments.getAllAppointments();

        if (start.equals(end) || start.after(end)) { // redundant check for time lambda
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Check Appointment Date/Time");
            alert.setHeaderText("Check Appointment Date/Time");
            alert.setContentText("Appointment start date and time must be before the end date and time.");
            alert.showAndWait();
        } else if (checkAppointmentOverlap(appointmentsList, custId, start, end)) { //verify customer doesn't have overlapping appt
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Overlapping Appointment");
            alert.setHeaderText("Overlapping Appointment");
            alert.setContentText("Customer: " + custId.getCustomerName() + " has an existing appointment that " +
                    "overlaps with the new appointment.");
            alert.showAndWait();
        } else { // all checks have passed, add appt
            // create appt
            DBAppointments.addAppointment(title, desc, loc, type, start, end, custId.getCustomerId(),
                    userId.getUserId(), contactId.getContactId());

            // update table
            Appointments.setAllAppointments(DBAppointments.getAllAppointments());
            appointmentsTable.setItems(Appointments.getAllAppointments());
            appointmentsTable.getSortOrder().add(appointmentsStart);

            // clear form
            clearAppts();
        }
    }

    /**
     * On update appointment button pressed, performs check for missing information, existing appointment overlap for
     * customer minus currently selected appointment that's being updated, adds appointment to list, refreshes
     * tableview, and clears editable fields.
     * <p></p>
     * A lambda is utilized to remove appointments from the ObservableList of all appointments for the selected
     * appointment ID before passing the list to the appointment overlap check method.
     */
    public void updateAppointmentButton() {
        if (apptId.getText() == null ||
                apptTitle.getText() == null ||
                apptDescription.getText() == null ||
                apptLocation.getText() == null ||
                apptType.getText() == null ||
                apptStartDate.getValue() == null ||
                apptStartTime.getValue() == null ||
                apptEndDate.getValue() == null ||
                apptEndTime.getValue() == null ||
                apptCustomer.getValue() == null ||
                apptUser.getValue() == null ||
                apptContact.getValue() == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Fill out the form completely to update an appointment.");
            alert.showAndWait();
            return; // do nothing. prevent exception
        }

        int id = Integer.parseInt(apptId.getText());
        String title = apptTitle.getText();
        String desc = apptDescription.getText();
        String loc = apptLocation.getText();
        String type = apptType.getText();
        Timestamp start = Timestamp.valueOf(LocalDateTime.of(apptStartDate.getValue(),
                apptStartTime.getSelectionModel().getSelectedItem()));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(apptEndDate.getValue(),
                apptEndTime.getSelectionModel().getSelectedItem()));
        Customers custId = apptCustomer.getValue();
        Users userId = apptUser.getValue();
        Contacts contactId = apptContact.getValue();

        // removed selected appointment from time check
        Appointments.setAllAppointments(DBAppointments.getAllAppointments());
        ObservableList<Appointments> appointmentsList = Appointments.getAllAppointments();
        appointmentsList.removeIf(a -> a.getAppointmentId() == id);

        if (start.equals(end) || start.after(end)){ // redundant check for time lambda
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Check Appointment Date/Time");
            alert.setHeaderText("Check Appointment Date/Time");
            alert.setContentText("Appointment start date and time must be before the end date and time.");
            alert.showAndWait();
        } else if (checkAppointmentOverlap(appointmentsList, custId, start, end)) {// verify customer doesn't have overlapping appt
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Overlapping Appointment");
            alert.setHeaderText("Overlapping Appointment");
            alert.setContentText("Customer: " + custId.getCustomerName() + " has an existing appointment that " +
                    "overlaps with the updated appointment.");
            alert.showAndWait();
        } else { // all checks have passed, add appt
            // create appt
            DBAppointments.updateAppointment(id, title, desc, loc, type, start, end, custId.getCustomerId(),
                    userId.getUserId(), contactId.getContactId());

            // update table
            Appointments.setAllAppointments(DBAppointments.getAllAppointments());
            appointmentsTable.setItems(Appointments.getAllAppointments());
            appointmentsTable.getSortOrder().add(appointmentsStart);

            // clear form
            clearAppts();
        }
    }

    /**
     * On edit appointment button pressed, performs check to make sure an appointment has been selected from tableview,
     * then sets editable fields with appropriate values.
     */
    public void editAppointmentButton() {
        if (!(appointmentsTable.getSelectionModel().getSelectedItem() == null)) {
            Appointments selectedAppt = appointmentsTable.getSelectionModel().getSelectedItem();
            Customers.setAllCustomers(DBCustomers.getAllCustomers());
            ObservableList<Customers> customersList = Customers.getAllCustomers();

            // get index of matching customer id
            int selectedCustId = 0;
            for (int i = 0; i < customersList.size(); i++) {
                if (customersList.get(i).getCustomerId() == selectedAppt.getCustomerId()) {
                    selectedCustId = customersList.indexOf(customersList.get(i));
                }
            }
            // get index of matching user id
            int selectedUserId = 0;
            for (int i = 0; i < usersList.size(); i++) {
                if (usersList.get(i).getUserId() == selectedAppt.getUserId()) {
                    selectedUserId = usersList.indexOf(usersList.get(i));
                }
            }
            // get index of matching contact id
            int selectedContactId = 0;
            for (int i = 0; i < contactsList.size(); i++) {
                if (contactsList.get(i).getContactId() == selectedAppt.getContactId()) {
                    selectedContactId = contactsList.indexOf(contactsList.get(i));
                }
            }

            Customers customer = customersList.get(selectedCustId);
            Users user = usersList.get(selectedUserId);
            Contacts contact = contactsList.get(selectedContactId);

            apptId.setText(String.valueOf(selectedAppt.getAppointmentId()));
            apptTitle.setText(selectedAppt.getTitle());
            apptDescription.setText(selectedAppt.getDescription());
            apptLocation.setText(selectedAppt.getLocation());
            apptType.setText(selectedAppt.getType());
            apptStartDate.setValue(selectedAppt.getStart().toLocalDateTime().toLocalDate());
            apptStartTime.setValue(selectedAppt.getStart().toLocalDateTime().toLocalTime());
            apptEndDate.setValue(selectedAppt.getEnd().toLocalDateTime().toLocalDate());
            apptEndTime.setValue(selectedAppt.getEnd().toLocalDateTime().toLocalTime());
            apptCustomer.setValue(customer);
            apptUser.setValue(user);
            apptContact.setValue(contact);
        }
    }

    /**
     * On delete appointment button pressed, performs check to make sure an appointment has been selected from
     * tableview, deletes appointment, refreshes tableview, clears editable fields, and displays alert stating
     * an appointment has been deleted.
     */
    public void deleteAppointmentButton() {
        if (!(appointmentsTable.getSelectionModel().getSelectedItem() == null)) {
            Appointments selectedAppt = appointmentsTable.getSelectionModel().getSelectedItem();
            int id = selectedAppt.getAppointmentId();
            String type = selectedAppt.getType();

            // delete appt
            DBAppointments.deleteAppointment(id);

            // update table
            Appointments.setAllAppointments(DBAppointments.getAllAppointments());
            appointmentsTable.setItems(Appointments.getAllAppointments());
            appointmentsTable.getSortOrder().add(appointmentsStart);

            // clear form
            clearAppts();

            // canceled appt message
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Cancelled");
            alert.setHeaderText("Appointment Cancelled");
            alert.setContentText("Appointment ID: " + id + " \nAppointment Type: " + type);
            alert.showAndWait();
        }
    }

    /**
     * On clear appointment button pressed, calls clear appointments method to clear all editable fields.
     */
    public void clearAppointmentButton() {
        clearAppts();
    }

    /**
     * Clear appointments method clears all editable fields including combo boxes and datepickers.
     */
    public void clearAppts() {
        apptId.clear();
        apptTitle.clear();
        apptDescription.clear();
        apptLocation.clear();
        apptContact.getSelectionModel().clearSelection();
        apptType.clear();
        apptStartDate.getEditor().clear();
        apptStartTime.getSelectionModel().clearSelection();
        apptEndDate.getEditor().clear();
        apptEndTime.getSelectionModel().clearSelection();
        apptCustomer.setValue(null); //apptCustomer.getSelectionModel().clearSelection() did not clear selection
        apptUser.getSelectionModel().clearSelection();
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

    /**
     * On view reports button pressed, loads reports view.
     * @param actionEvent For changing to next scene upon button click.
     * @throws IOException For failed or interrupted I/O operations.
     */
    public void viewReportsButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/software2/view/ViewReports.fxml"));
        Scene mainScene = new Scene(scene);
        stage.setScene(mainScene);
        stage.centerOnScreen();
        stage.show();
    }
}
