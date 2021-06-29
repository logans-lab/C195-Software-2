package software2.dbAccess;

import software2.model.Users;
import software2.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database users class. Contains a R query to the database.
 * @author logan
 */
public class DBUsers {

    /**
     * Get all users query.
     * @return ObservableList of all users, not including their passwords.
     */
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> userList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT User_ID, User_Name FROM users ORDER BY User_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("User_ID");
                String username = resultSet.getString("User_Name");
                Users user = new Users(userId, username);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("error get all users");
            e.printStackTrace();
        }

        return userList;
    }
}
