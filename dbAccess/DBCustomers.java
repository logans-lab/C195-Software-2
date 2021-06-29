package software2.dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.model.Customers;
import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database customers class. Contains CRUD queries to the database.
 * @author logan
 */
public class DBCustomers {

    /**
     * Get all customers query.
     * @return ObservableList of all customers.
     */
    public static ObservableList<Customers> getAllCustomers() {
        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, " +
                    "Division, first_level_divisions.Country_ID, Country FROM customers, first_level_divisions, " +
                    "countries WHERE customers.Division_ID = first_level_divisions.Division_ID AND " +
                    "first_level_divisions.Country_ID = countries.Country_ID ORDER BY Customer_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                Customers customers = new Customers(customerId, customerName, address, postalCode, phone, divisionId,
                        divisionName, countryId, countryName);
                customerList.add(customers);
            }
        } catch (SQLException e) {
            System.out.println("error get all customers");
            e.printStackTrace();
        }

        return customerList;
    }

    /**
     * Add customer query.
     * @param name Customer name.
     * @param address Customer address.
     * @param postal Customer postal code.
     * @param phone Customer phone number.
     * @param divisionId Customer first level division ID.
     */
    public static void addCustomer(String name, String address, String postal, String phone, int divisionId) {
        try {
            String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                    "Division_ID) VALUES (NULL,?,?,?,?,?);";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postal);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, divisionId);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error add customer");
            e.printStackTrace();
        }
    }

    /**
     * Update customer query.
     * @param id Customer ID.
     * @param name Customer name.
     * @param address Customer address.
     * @param postal Customer postal code.
     * @param phone Customer phone number.
     * @param divisionId Customer first level division ID.
     */
    public static void updateCustomer(int id, String name, String address, String postal, String phone, int divisionId) {
        try {
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, " +
                    "Division_ID = ? WHERE Customer_ID = ?;";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postal);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, divisionId);
            preparedStatement.setInt(6, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error update customer");
            e.printStackTrace();
        }
    }

    /**
     * Delete customer query. Deletes appointments first as they are foreign key dependant on customer.
     * @param id Customer ID.
     */
    public static void deleteCustomer(int id) {
        try {
            String sql = "DELETE FROM appointments WHERE Customer_ID = ?;";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error delete customer appointments");
            e.printStackTrace();
        }

        try {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?;";

            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("error delete customer");
            e.printStackTrace();
        }
    }
}
