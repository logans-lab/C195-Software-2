// OVERVIEW
Course: WGU C195
Application Title: Software2
Purpose: Build a working GUI scheduling desktop application that connects with a database in real time.
         Contains 4 views: log-in authentication, appointments view, customer view, and reports view.
Author: Logan
Version: 1.0
Date: May 20, 2021


// SPECIFICATIONS AND VERSIONS
IDE:                        IntelliJ IDEA 2020.2.2
JDK:                        jdk-11.0.8
JavaFX:                     javafx-sdk-11.0.2
MySQL Connector Driver:     mysql-connector-java-8.0.22 (loaded into Libraries using Maven)


// HOW TO RUN THE PROGRAM
This is a 3-view application with a login prompt only displayed at startup. The 3 views can be easily accessed by their
respective buttons on each of the other views.

Download files into configuration below and load into IntelliJ. Select specified JDK, SDK, and MySQL driver.
Run Main.java. You will first be prompted to login using the login view. Once logged in, can easily navigate between
    the other 3 views.
Login view:
    Enter credentials. Default username and password are 'test' and 'test'.
Appointments view:
    Can toggle between all appointments, this month's appointments, and this week's appointments at the top.
    View and select an appointment in the table.
    Once selected, the appointment can be Deleted or Edited.
    Editing a selected appointment populates the editable fields on the bottom half of the view, which can then be Updated.
    Add creates a new appointment from the information in the editable fields and populates the table.
    Navigate to Customer View and Reports View via the buttons.
Customer View:
    Contains the same buttons, functionality, and layout as appointments.
    Appointments are linked to customers so deleting a customer will delete all of their appointments automatically.
    Navigate to Appointment View and Reports View via the buttons.
Reports View:
    Customer Appointments displays the number of appointments by month and type.
    Contact Schedule displays a list of appointments for each contact.
    Appointments By User displays the total number of appointments for the total number of customers, which were added
        by each user.
    Navigate to Appointment View and Customer View via the buttons.
    
// FILE STRUCTURE

