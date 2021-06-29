package software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

/**
 * Appointments class.
 * @author logan
 */
public class Appointments {
    // contactId is FK from Contacts
    // userId is FK from Users
    // customerId is FK from Customers
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerId;
    private String customerName;
    private int userId;
    private String userName;
    private int contactId;
    private String contactName;

    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

    /**
     * Appointments constructor.
     * @param appointmentId Appointment ID.
     * @param title Appointment title.
     * @param description Appointment description.
     * @param location Appointment location.
     * @param type Appointment type.
     * @param start Appointment start date and time.
     * @param end Appointment end date and time.
     * @param customerId Appointment customer ID.
     * @param customerName Appointment customer name.
     * @param userId Appointment user ID.
     * @param userName Appointment username.
     * @param contactId Appointment contact ID.
     * @param contactName Appointment contact name.
     */
    public Appointments(int appointmentId, String title, String description, String location, String type,
                        Timestamp start, Timestamp end, int customerId, String customerName, int userId,
                        String userName, int contactId, String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
        this.userName = userName;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Get appointment ID.
     * @return appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Get appointment title.
     * @return Appointment title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get appointment description.
     * @return Appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get appointment location.
     * @return Appointment location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get appointment type.
     * @return Appointment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get appointment start timestamp.
     * @return Appointment start.
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Get appointment end timestamp.
     * @return Appointment end.
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Get appointment customer ID.
     * @return Appointment customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Get appointment customer name.
     * @return Appointment customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Get appointment user ID.
     * @return Appointment user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get appointment username.
     * @return Appointment username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get appointment contact ID.
     * @return Appointment contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Get appointment contact name.
     * @return Appointment contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Get all appointments.
     * @return ObservableList of all appointments.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Set all appointments using database query.
     * @param allAppointments ObservableList from database query.
     */
    public static void setAllAppointments(ObservableList<Appointments> allAppointments) {
        Appointments.allAppointments = allAppointments;
        System.out.println("appointments set");
    }
}
