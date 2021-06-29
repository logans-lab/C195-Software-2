package software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contacts class.
 * @author logan
 */
public class Contacts {
    // no setters
    private int contactId;
    private String contactName;

    private static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    /**
     * Contacts constructor.
     * @param contactId Contact ID.
     * @param contactName Contact name.
     */
    public Contacts(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Get contact ID.
     * @return Contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Get contact name.
     * @return Contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Get all contacts.
     * @return ObservableList of all contacts.
     */
    public static ObservableList<Contacts> getAllContacts() {
        return allContacts;
    }

    /**
     * Set all contacts using database query.
     * @param allContacts ObservableList from database query.
     */
    public static void setAllContacts(ObservableList<Contacts> allContacts) {
        Contacts.allContacts = allContacts;
        System.out.println("Contacts set");
    }

    /**
     * Contacts toString override method for displaying name and ID in combo boxes.
     * @return Concatenation of contact name and contact ID.
     */
    @Override
    public String toString() {
        return contactName + " [" + contactId + "]";
    }
}
