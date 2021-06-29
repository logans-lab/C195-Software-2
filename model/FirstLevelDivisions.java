package software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * First level divisions class.
 * @author logan
 */
public class FirstLevelDivisions {
    // no setters
    // int countryId is FK to Countries
    private int divisionId;
    private String division;
    private int countryId;
    private String countryName;

    private static ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();

    /**
     * First level division constructor.
     * @param divisionId First level division ID.
     * @param division First level division name.
     * @param countryId First level division country ID.
     * @param countryName First level division country name.
     */
    public FirstLevelDivisions(int divisionId, String division, int countryId, String countryName) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * Get first level division ID.
     * @return First level division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Get first level division name.
     * @return First level division name.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Get first level country ID.
     * @return First level country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Get first level country name.
     * @return First level country name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Get all first level divisions.
     * @return ObservableList of all first level divisions.
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions() {
        return allDivisions;
    }

    /**
     * Set all first level divisions using database query.
     * @param allDivisions ObservableList from database query.
     */
    public static void setAllDivisions(ObservableList<FirstLevelDivisions> allDivisions) {
        FirstLevelDivisions.allDivisions = allDivisions;
        System.out.println("Divisions set");
    }

    /**
     * First level divisions toString override method for displaying name and ID in combo boxes.
     * @return Concatenation of first level division name and first level division ID.
     */
    @Override
    public String toString() {
        return division + " [" + divisionId + "]";
    }
}
