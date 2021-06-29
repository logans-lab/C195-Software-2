package software2.dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.model.Contacts;
import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database contacts class. Contains a R query to the database.
 * @author logan
 */
public class DBContacts {

    /**
     * Get all contacts query.
     * @return ObservableList of all contacts.
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Contact_ID, Contact_Name FROM contacts ORDER BY Contact_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                Contacts contacts = new Contacts(contactId, contactName);
                contactList.add(contacts);
            }
        } catch (SQLException e) {
            System.out.println("error get all users");
            e.printStackTrace();
        }

        return contactList;
    }
}
