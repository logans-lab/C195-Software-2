package software2.dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.model.Reports;
import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Database reports class. Contains R queries to the database.
 * @author logan
 */
public class DBReports {

    /**
     * Get customer appointments query.
     * @return ObservableList of all customer appointments by month and type.
     */
    public static ObservableList<Reports> getCustomerAppointments() {
        ObservableList<Reports> reportsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT MONTH(Start), MONTHNAME(Start), Type, COUNT(*) FROM appointments GROUP BY " +
                    "MONTH(Start), MONTHNAME(Start), Type ORDER BY MONTH(Start);";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // monthNum needed for ordering customer appointment query non-alphabetically
                int monthNum = resultSet.getInt("MONTH(Start)");
                String monthName = resultSet.getString("MONTHNAME(Start)");
                String type = resultSet.getString("Type");
                int apptCount = resultSet.getInt("COUNT(*)");

                Reports report = new Reports(monthNum, monthName, type, apptCount);
                reportsList.add(report);
            }
        } catch (SQLException e) {
            System.out.println("error get customer appts");
            e.printStackTrace();
        }

        return reportsList;
    }

    /**
     * Get contact schedule query.
     * @return ObservableList of all appointments for each contact.
     */
    public static ObservableList<Reports> getContactSchedule() {
        ObservableList<Reports> reportsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Type, Description, Start, End, appointments.Customer_ID, " +
                    "customers.Customer_Name, appointments.Contact_ID, contacts.Contact_Name FROM appointments, " +
                    "contacts, customers WHERE appointments.Contact_ID = contacts.Contact_ID AND " +
                    "appointments.Customer_ID = customers.Customer_ID ORDER BY appointments.Contact_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String desc = resultSet.getString("Description");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");

                Reports report = new Reports(apptId, title, type, desc, start, end, customerId, customerName,
                        contactId, contactName);
                reportsList.add(report);
            }
        } catch (SQLException e) {
            System.out.println("error get contact schedule");
            e.printStackTrace();
        }

        return reportsList;
    }

    /**
     * Get user appointments query.
     * @return ObservableList of a count of appointments added for number of customers by each user.
     */
    public static ObservableList<Reports> getUserAppointments() {
        ObservableList<Reports> reportsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT appointments.User_ID, users.User_Name, COUNT(DISTINCT Appointment_ID), " +
                    "COUNT(DISTINCT Customer_ID) FROM appointments, users WHERE appointments.User_ID = users.User_ID " +
                    "GROUP BY appointments.User_ID, users.User_Name ORDER BY appointments.User_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                int apptCount = resultSet.getInt("COUNT(DISTINCT Appointment_ID)");
                int customerCount = resultSet.getInt("COUNT(DISTINCT Customer_ID)");

                Reports report = new Reports(userId, userName, apptCount, customerCount);
                reportsList.add(report);
            }
        } catch (SQLException e) {
            System.out.println("error get user appts");
            e.printStackTrace();
        }

        return reportsList;
    }
}
