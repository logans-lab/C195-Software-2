package software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Users class.
 * @author logan
 */
public class Users {
    // no setters
    private int userId;
    private String username;

    private static ObservableList<Users> allUsers = FXCollections.observableArrayList();

    /**
     * Users constructor.
     * @param userId User ID.
     * @param username Username.
     */
    public Users(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * Get user ID.
     * @return User ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get username.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get all users.
     * @return ObservableList of all users.
     */
    public static ObservableList<Users> getAllUsers() {
        return allUsers;
    }

    /**
     * Set all users using database query.
     * @param allUsers ObservableList from database query.
     */
    public static void setAllUsers(ObservableList<Users> allUsers) {
        Users.allUsers = allUsers;
        System.out.println("Users set");
    }

    /**
     * Users toString override method for displaying name and ID in combo boxes.
     * @return Concatenation of username and user ID.
     */
    @Override
    public String toString() {
        return username + " [" + userId + "]";
    }
}
