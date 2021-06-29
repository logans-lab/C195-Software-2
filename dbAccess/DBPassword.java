package software2.dbAccess;

import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database password class. Contains a R query to the database.
 * @author logan
 */
public final class DBPassword {

    /**
     * Get password query. Doesn't return actual password, only whether entered username and password match.
     * @param username User's username.
     * @param password User's password.
     * @return Boolean true if username and password match in the system. Otherwise returns false.
     */
    public static boolean getPassword(String username, String password) {
        boolean answer = false;

        try {
            String sql = "SELECT User_Name, Password FROM users WHERE User_Name = ? AND Password = ?;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                answer = true;
            } // if not, username and/or password are not valid

        } catch (SQLException e) {
            System.out.println("error get password");
            e.printStackTrace();
        }

        return answer;
    }
}
