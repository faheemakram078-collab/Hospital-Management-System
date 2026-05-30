package models;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    private String status; // "SCHEDULED", "COMPLETED", "CANCELLED"
    private String notes;  // e.g., "Nil"

    public Appointment(String appointmentId, String patientId, String doctorId, String date, String time, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = (notes == null || notes.trim().isEmpty()) ? "Nil" : notes;
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId; }
    public String getDoctorId()      { return doctorId; }
    public String getDate()          { return date; }
    public String getTime()          { return time; }
    public String getStatus()        { return status; }
    public String getNotes()         { return notes; }

    // Setters
    public void setDate(String date)     { this.date = date; }
    public void setTime(String time)     { this.time = time; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes)   { this.notes = notes; }

    @Override
    public String toString() {
        return "Appt #" + appointmentId + " | Patient: " + patientId + " | Doctor: " + doctorId + " | " + date + " " + time;
    }

    // Format output layout: appointmentId|patientId|doctorId|date|time|status|notes
    public String toFileString() {
        return appointmentId + "|" + patientId + "|" + doctorId + "|" + date + "|" + time + "|" + status + "|" + notes;
    }

    public static Appointment fromFileString(String line) {
        // Crucial Fix: Using split limit -1 protects trailing empty slots from breaking
        String[] p = line.split("\\|", -1);
        
        String appId = p[0];
        String patId = p[1];
        String docId = p[2];
        String date = p[3];
        String time = p[4];
        String status = p[5];
        String notes = (p.length > 6 && !p[6].trim().isEmpty()) ? p[6] : "Nil";
        
        return new Appointment(appId, patId, docId, date, time, status, notes);
    }
}
