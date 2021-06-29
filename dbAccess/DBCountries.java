package software2.dbAccess;

import software2.model.Countries;
import software2.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Databse countries class. Contains a R query to the database.
 * @author logan
 */
public class DBCountries {

    /**
     * Get all countries query.
     * @return ObservableList of all countries.
     */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Country_ID, Country FROM countries ORDER BY Country_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int countryId = resultSet.getInt("Country_ID");
                String country = resultSet.getString("Country");

                Countries countries = new Countries(countryId, country);
                countryList.add(countries);
            }
        } catch (SQLException e) {
            System.out.println("error get all countries");
            e.printStackTrace();
        }

        return countryList;
    }
}
