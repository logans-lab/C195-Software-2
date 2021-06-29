package software2.dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.model.*;
import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Database appointments class. Contains CRUD queries to the database.
 * @author logan
 */
public class DBAppointments {

    /**
     * Get all appointments query.
     * @return ObservableList of all appointments.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "appointments.Customer_ID, Customer_Name, appointments.User_ID, User_Name, appointments.Contact_ID, " +
                    "Contact_Name FROM appointments, users, customers, contacts WHERE appointments.Customer_ID = " +
                    "customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = " +
                    "contacts.Contact_ID ORDER BY appointments.Appointment_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String desc = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");

                Appointments appointments = new Appointments(apptId, title, desc, location, type, start, end,
                        customerId, customerName, userId, userName, contactId, contactName);
                appointmentList.add(appointments);
            }
        } catch (SQLException e) {
            System.out.println("error get all appointments");
            e.printStackTrace();
        }

        return appointmentList;
    }

    /**
     * Get appointments by time query.
     * @param time Time in number of days from query date.
     * @return ObservableList of appointments that are within number of days.
     */
    public static ObservableList<Appointments> getAppointmentsByTime(int time) {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "appointments.Customer_ID, Customer_Name, appointments.User_ID, User_Name, appointments.Contact_ID, " +
                    "Contact_Name FROM appointments, users, customers, contacts WHERE appointments.Customer_ID = " +
                    "customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = " +
                    "contacts.Contact_ID AND DATEDIFF(Start, NOW()) <= ? AND DATEDIFF(Start, NOW()) >= 0 ORDER BY " +
                    "appointments.Appointment_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, time);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int apptId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String desc = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp start = resultSet.getTimestamp("Start");
                Timestamp end = resultSet.getTimestamp("End");
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                int userId = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");

                Appointments appointments = new Appointments(apptId, title, desc, location, type, start, end,
                        customerId, customerName, userId, userName, contactId, contactName);
                appointmentList.add(appointments);
            }
        } catch (SQLException e) {
            System.out.println("error get timed appointments");
            e.printStackTrace();
        }

        return appointmentList;
    }

    /**
     * Add appointment query.
     * @param title Appointment title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param start Appointment start timestamp.
     * @param end Appointment end timestamp.
     * @param customerId Appointment customer ID.
     * @param userId Appointment user ID.
     * @param contactId Appointment contact ID.
     */
    public static void addAppointment(String title, String description, String location, String type, Timestamp start,
                                      Timestamp end, int customerId, int userId, int contactId) {
        try {
            String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "Customer_ID, User_ID, Contact_ID) VALUES (NULL,?,?,?,?,?,?,?,?,?);";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, start);
            preparedStatement.setTimestamp(6, end);
            preparedStatement.setInt(7, customerId);
            preparedStatement.setInt(8, userId);
            preparedStatement.setInt(9, contactId);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error add appointment");
            e.printStackTrace();
        }
    }

    /**
     * Update appointment query.
     * @param id Appointment ID.
     * @param title Appointment title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param start Appointment start.
     * @param end Appointment end.
     * @param customerId Appointment customer ID.
     * @param userId Appointment user ID.
     * @param contactId Appointment contact ID.
     */
    public static void updateAppointment(int id, String title, String description, String location, String type,
                                         Timestamp start, Timestamp end, int customerId, int userId, int contactId) {
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, " +
                    "End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, start);
            preparedStatement.setTimestamp(6, end);
            preparedStatement.setInt(7, customerId);
            preparedStatement.setInt(8, userId);
            preparedStatement.setInt(9, contactId);
            preparedStatement.setInt(10, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error update appointment");
            e.printStackTrace();
        }
    }

    /**
     * Delete appointment query.
     * @param id Appointment ID.
     */
    public static void deleteAppointment(int id) {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error delete appointment");
            e.printStackTrace();
        }
    }
}
