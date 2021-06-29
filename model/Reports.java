package software2.model;

import java.sql.Timestamp;

/**
 * Reports class.
 * @author logan
 */
public class Reports {

    private int appointmentId;
    private String title;
    private String type;
    private String description;
    private Timestamp start;
    private Timestamp end;
    private int customerId;
    private String customerName;
    private int contactId;
    private String contactName;
    private int userId;
    private String userName;
    private int appointmentCount;
    private int customerCount;
    private int monthNum;
    private String monthName;

    /**
     * Report constructor for contact schedule query.
     * @param appointmentId Appointment ID.
     * @param title Appointment title.
     * @param type Appointment type.
     * @param description Appointment description.
     * @param start Appointment start timestamp.
     * @param end Appointment end timestamp.
     * @param customerId Appointment customer ID.
     * @param customerName Customer Name.
     * @param contactId Appointment contact ID.
     * @param contactName Contact name.
     */
    public Reports(int appointmentId, String title, String type, String description, Timestamp start, Timestamp end,
                   int customerId, String customerName, int contactId, String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Reports constructor for user appointments query.
     * @param userId Appointment user ID.
     * @param userName Username.
     * @param appointmentCount Appointment count.
     * @param customerCount Customer count.
     */
    public Reports(int userId, String userName, int appointmentCount, int customerCount) {
        this.userId = userId;
        this.userName = userName;
        this.appointmentCount = appointmentCount;
        this.customerCount = customerCount;
    }

    /**
     * Reports constructor for customer appointments query.
     * @param monthNum Number of month.
     * @param monthName Month name.
     * @param type Appointment type.
     * @param apptCount Appointment count.
     */
    public Reports(int monthNum, String monthName, String type, int apptCount) {
        this.monthNum = monthNum;
        this.monthName = monthName;
        this.type = type;
        this.appointmentCount = apptCount;
    }

    /**
     * Get appointment ID.
     * @return Appointment ID.
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
     * Get appointment type.
     * @return Appointment type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get appointment description.
     * @return Appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get appointment start timestamp.
     * @return Appointment start timestamp.
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Get appointment end timestamp.
     * @return Appointment end timestamp.
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
     * Get appointment contact ID.
     * @return Appointment contact ID.
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
     * Get appointment user ID.
     * @return Appointment user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get username.
     * @return Username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get appointment count.
     * @return Appointment count.
     */
    public int getAppointmentCount() {
        return appointmentCount;
    }

    /**
     * Get customer count.
     * @return Customer count.
     */
    public int getCustomerCount() {
        return customerCount;
    }

    /**
     * Get month number.
     * @return Month number.
     */
    public int getMonthNum() {
        return monthNum;
    }

    /**
     * Get month name.
     * @return Month name.
     */
    public String getMonthName() {
        return monthName;
    }
}
