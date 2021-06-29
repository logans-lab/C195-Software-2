package software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Countries class.
 * @author logan
 */
public class Countries {
    // no setters
    private int countryId;
    private String country;

    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    /**
     * Countries constructor.
     * @param countryId Country ID.
     * @param country Country name.
     */
    public Countries(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * Get country ID.
     * @return Country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Get country name.
     * @return Country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get all countries.
     * @return ObservableList of all countries.
     */
    public static ObservableList<Countries> getAllCountries() {
        return allCountries;
    }

    /**
     * Set all countries using database query.
     * @param allCountries ObservableList from database query.
     */
    public static void setAllCountries(ObservableList<Countries> allCountries) {
        Countries.allCountries = allCountries;
        System.out.println("Countries set");
    }

    /**
     * Countries toString override method for displaying name and ID in combo boxes.
     * @return Concatenation of country name and country ID.
     */
    @Override
    public String toString() {
        return country + " [" + countryId + "]";
    }
}
