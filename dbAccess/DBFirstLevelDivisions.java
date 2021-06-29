package software2.dbAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.model.FirstLevelDivisions;
import software2.utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database first level division class. Contains R queries to the database.
 * @author logan
 */
public class DBFirstLevelDivisions {

    /**
     * Get all divisions query.
     * @return ObservableList of all first level divisions.
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions() {
        ObservableList<FirstLevelDivisions> divisionList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Division_ID, Division, first_level_divisions.Country_ID, countries.Country FROM " +
                    "first_level_divisions, countries WHERE countries.Country_ID = first_level_divisions.Country_ID;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                String country = resultSet.getString("Country");

                FirstLevelDivisions divisions = new FirstLevelDivisions(divisionId, division, countryId, country);
                divisionList.add(divisions);
            }
        } catch (SQLException e) {
            System.out.println("error get all divisions");
            e.printStackTrace();
        }

        return divisionList;
    }

    /**
     * Get divisions by country ID.
     * @param num Country ID.
     * @return ObservableList of all first level divisions that match the country ID.
     */
    public static ObservableList<FirstLevelDivisions> getDivisionsByCountry(int num) {
        ObservableList<FirstLevelDivisions> divisionList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Division_ID, Division, first_level_divisions.Country_ID, countries.Country FROM " +
                    "first_level_divisions, countries WHERE first_level_divisions.Country_ID = countries.Country_ID " +
                    "AND first_level_divisions.Country_ID = ?;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                String country = resultSet.getString("Country");

                FirstLevelDivisions divisions = new FirstLevelDivisions(divisionId, division, countryId, country);
                divisionList.add(divisions);
            }
        } catch (SQLException e) {
            System.out.println("error get division by country");
            e.printStackTrace();
        }

        return divisionList;
    }

    /**
     * Get division by ID. Used in populating customer information into editable fields.
     * @param num First level division ID.
     * @return First level division object matching division ID.
     */
    public static FirstLevelDivisions getDivisionById(int num) {
        FirstLevelDivisions divisions = null;

        try {
            String sql = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions WHERE Division_ID = ?;";
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, num);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String division = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");
                String country = null;

                divisions = new FirstLevelDivisions(divisionId, division, countryId, country);
            }
        } catch (SQLException e) {
            System.out.println("error get division by division id");
            e.printStackTrace();
        }

        return divisions;
    }
}
