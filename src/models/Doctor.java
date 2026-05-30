package models;

import interfaces.Schedulable;

// INHERITANCE + INTERFACE
public class Doctor extends User implements Schedulable {

    private String specialization;   // e.g. "Cardiology"
    private String availability;     // e.g. "Mon-Fri 9am-5pm"
    private int patientsHandled;

    public Doctor(String userId, String name, String email,
                  String password, String specialization,
                  String availability, int patientsHandled) {
        super(userId, name, email, password, "DOCTOR");
        this.specialization  = specialization;
        this.availability    = availability;
        this.patientsHandled = patientsHandled;
    }

    @Override
    public String getDisplayInfo() {
        return "Dr. " + getName() + " | Specialization: " + specialization
               + " | Available: " + availability;
    }

    // INTERFACE METHODS
    @Override
    public boolean scheduleAppointment(String date, String time) {
        // Logic handled by AppointmentService; this just confirms doctor is available
        System.out.println("Appointment scheduled with Dr. " + getName());
        patientsHandled++;
        return true;
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        System.out.println("Appointment " + appointmentId + " cancelled.");
        return true;
    }

    // Getters
    public String getSpecialization()  { return specialization; }
    public String getAvailability()    { return availability; }
    public int    getPatientsHandled() { return patientsHandled; }

    // Setters
    public void setSpecialization(String s) { this.specialization = s; }
    public void setAvailability(String a)   { this.availability = a; }

    // Format: userId|name|email|password|DOCTOR|specialization|availability|patientsHandled
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + specialization + "|"
               + availability + "|" + patientsHandled;
    }

    public static Doctor fromFileString(String line) {
        String[] p = line.split("\\|");
        return new Doctor(p[0], p[1], p[2], p[3], p[5], p[6], Integer.parseInt(p[7]));
    }
}
