package software2.controller;

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
import software2.dbAccess.*;
import software2.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * View customers controller class.
 * @author logan
 */
public class ViewCustomersController implements Initializable {

    @FXML private TableView<Customers> customersTable;
    @FXML private TableColumn<Customers, Integer> customersTableId;
    @FXML private TableColumn<Customers, String> customersTableName;
    @FXML private TableColumn<Customers, String> customersTableAddress;
    @FXML private TableColumn<Customers, String> customersTablePostalCode;
    @FXML private TableColumn<Customers, String> customersTablePhone;
    @FXML private TableColumn<FirstLevelDivisions, String> customersTableDivisionName;
    @FXML private TableColumn<Countries, String> customersTableCountryName;

    @FXML private TextField customerId;
    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhone;
    @FXML private ComboBox<FirstLevelDivisions> customerDivision;
    @FXML private ComboBox<Countries> customerCountry;

    Alert alert;
    ObservableList<Countries> countryList = Countries.getAllCountries();

    /**
     * On load, tableview of customers is loaded. Loads combo box options for countries.
     * <p></p>
     * One lambda is added here utilizing a listener on the country combo box to load available
     * first level divisions for the selected country. This prevents the user from selecting a division
     * that is not within the selected country.
     * <p></p>
     * @param url The location used to resolve relative paths for the root object, or null if the location
     *        is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was
     *                       not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customersTableId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customersTableName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customersTableAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customersTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customersTablePhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customersTableDivisionName.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customersTableCountryName.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        customersTable.setItems(DBCustomers.getAllCustomers());
        customersTable.getSortOrder().add(customersTableId);

        // set country and division comboboxes. division is filtered by country selection
        customerCountry.setItems(Countries.getAllCountries());
        customerCountry.getSelectionModel().selectedItemProperty().addListener((observableValue, countries, t1) -> {
            if (!customerCountry.getSelectionModel().isEmpty()) { // prevents error when clearing customer info
                customerDivision.setItems(DBFirstLevelDivisions.getDivisionsByCountry(t1.getCountryId()));
            }
        });
    }

    /**
     * On add customer button pressed, performs check for missing information, adds customer to list,
     * refreshes tableview, and clears text fields.
     */
    public void addCustomerButton() {
        if (customerName.getText() == null ||
                customerAddress.getText() == null ||
                customerPostalCode.getText() == null ||
                customerPhone.getText() == null ||
                customerDivision.getValue() == null ||
                customerCountry.getValue() == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Fill out the form completely to add a new customer.");
            alert.showAndWait();
            return; // do nothing. prevent exception
        }

        String name = customerName.getText();
        String address = customerAddress.getText();
        String postal = customerPostalCode.getText();
        String phone = customerPhone.getText();
        FirstLevelDivisions division = customerDivision.getValue();

        // add customer
        DBCustomers.addCustomer(name, address, postal, phone, division.getDivisionId());

        // updates table
        Customers.setAllCustomers(DBCustomers.getAllCustomers());
        customersTable.setItems(Customers.getAllCustomers());
        customersTable.getSortOrder().add(customersTableId);

        // clear form
        clearCustomers();
    }

    /**
     * On update customer button pressed, performs check for missing information, updates customer to list, refreshes
     * tableview, and clears editable fields.
     */
    public void updateCustomerButton() {
        if (customerId.getText() == null ||
                customerName.getText() == null ||
                customerAddress.getText() == null ||
                customerPostalCode.getText() == null ||
                customerPhone.getText() == null ||
                customerDivision.getValue() == null ||
                customerCountry.getValue() == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Fill out the form completely to update an existing customer.");
            alert.showAndWait();
            return; // do nothing. prevent exception
        }

        int id = Integer.parseInt(customerId.getText());
        String name = customerName.getText();
        String address = customerAddress.getText();
        String postal = customerPostalCode.getText();
        String phone = customerPhone.getText();
        FirstLevelDivisions division = customerDivision.getValue();

        // updates customer
        DBCustomers.updateCustomer(id, name, address, postal, phone, division.getDivisionId());

        // updates table
        Customers.setAllCustomers(DBCustomers.getAllCustomers());
        customersTable.setItems(Customers.getAllCustomers());
        customersTable.getSortOrder().add(customersTableId);

        // clear form
        clearCustomers();
    }

    /**
     * On edit appointment button pressed, performs check to make sure an appointment has been selected from tableview,
     * then sets editable fields with appropriate values.
     */
    public void editCustomerButton() {
        if (!(customersTable.getSelectionModel().getSelectedItem() == null)) {
            Customers selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            FirstLevelDivisions div = DBFirstLevelDivisions.getDivisionById(selectedCustomer.getDivisionId());

            // get index of matching country id
            int selectedCountryId = 0;
            for (int i = 0; i < countryList.size(); i++) {
                if (countryList.get(i).getCountryId() == selectedCustomer.getCountryId()) {
                    selectedCountryId = countryList.indexOf(countryList.get(i));
                }
            }

            customerId.setText(String.valueOf(selectedCustomer.getCustomerId()));
            customerName.setText(selectedCustomer.getCustomerName());
            customerAddress.setText(selectedCustomer.getCustomerAddress());
            customerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
            customerPhone.setText(selectedCustomer.getCustomerPhone());
            customerCountry.setValue(countryList.get(selectedCountryId));
            customerDivision.setValue(div);
        }
    }

    /**
     * On delete customer button pressed, performs check to make sure a customer has been selected from
     * tableview, deletes customer, refreshes tableview, clears editable fields, and displays alert stating
     * a customer and their appointments has been deleted.
     */
    public void deleteCustomerButton() {
        if (!(customersTable.getSelectionModel().getSelectedItem() == null)) {
            Customers selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            int id = selectedCustomer.getCustomerId();
            String name = selectedCustomer.getCustomerName();

            // delete customer
            DBCustomers.deleteCustomer(id);

            // update table
            Customers.setAllCustomers(DBCustomers.getAllCustomers());
            customersTable.setItems(Customers.getAllCustomers());
            customersTable.getSortOrder().add(customersTableId);

            // clear form
            clearCustomers();

            // delete customer message
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Deleted");
            alert.setHeaderText("Customer Deleted");
            alert.setContentText("The following customer and their appointments have been deleted. \nCustomer ID: " +
                    id + "\nCustomer Name: " + name);
            alert.showAndWait();
        }
    }

    /**
     * On clear customer button pressed, calls clear customers method to clear all editable fields.
     */
    public void clearCustomerButton() {
        clearCustomers();
    }

    /**
     * Clear customers method clears all editable fields including combo boxes.
     */
    public void clearCustomers() {
        customerId.clear();
        customerName.clear();
        customerAddress.clear();
        customerPostalCode.clear();
        customerPhone.clear();
        customerCountry.getSelectionModel().clearSelection();
        customerDivision.setValue(null); // customerDivision.getSelectionModel().clearSelection() did not clear selection
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
