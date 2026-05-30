package services;

import models.Appointment;
import models.Patient;
import models.Doctor;
import filehandling.DataLoader;

import java.util.List;
import java.util.ArrayList;

public class AppointmentService {

    /**
     * Retrieves all appointments registered in the system.
     */
    public static List<Appointment> getAllAppointments() {
        return DataLoader.loadAppointments();
    }

    /**
     * Filters and returns a list of all appointments marked as emergencies.
     */
    public static List<Appointment> getEmergencies() {
        List<Appointment> emergencies = new ArrayList<>();
        for (Appointment a : getAllAppointments()) {
            if (a.getStatus() != null && a.getStatus().equalsIgnoreCase("EMERGENCY")) {
                emergencies.add(a);
            } else if (a.getNotes() != null && a.getNotes().toLowerCase().contains("emergency")) {
                emergencies.add(a);
            }
        }
        return emergencies;
    }

    /**
     * Schedules a standard appointment after validating that the Patient and Doctor exist.
     */
    public static boolean scheduleAppointment(String patientId, String doctorId, String date, String time, String notes) {
        Patient patient = DataLoader.findPatient(patientId);
        Doctor doctor = DataLoader.findDoctor(doctorId);

        if (patient == null || doctor == null) {
            return false;
        }

        String appointmentId = generateAppointmentId();
        Appointment appointment = new Appointment(
            appointmentId, patientId, doctorId, date, time, "SCHEDULED", notes
        );

        DataLoader.saveAppointment(appointment);
        return true;
    }

    /**
     * Schedules an immediate emergency appointment tagged as ASAP.
     */
    public static boolean scheduleEmergency(String patientId, String doctorId, String notes) {
        Patient patient = DataLoader.findPatient(patientId);
        Doctor doctor = DataLoader.findDoctor(doctorId);

        if (patient == null || doctor == null) {
            return false;
        }

        String appointmentId = generateAppointmentId();
        Appointment appointment = new Appointment(
            appointmentId, patientId, doctorId, "TODAY", "ASAP", "EMERGENCY", notes
        );

        DataLoader.saveAppointment(appointment);
        return true;
    }

    /**
     * Cancels an existing appointment using its ID.
     */
    public static boolean cancelAppointment(String appointmentId) {
        for (Appointment a : getAllAppointments()) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.setStatus("CANCELLED");
                DataLoader.updateAppointment(a);
                return true;
            }
        }
        return false;
    }

    /**
     * Marks an appointment status as COMPLETED.
     */
    public static boolean completeAppointment(String appointmentId) {
        for (Appointment a : getAllAppointments()) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.setStatus("COMPLETED");
                DataLoader.updateAppointment(a);
                return true;
            }
        }
        return false;
    }

    /**
     * Reschedules an appointment to a new date and time.
     */
    public static boolean reschedule(String appointmentId, String newDate, String newTime) {
        for (Appointment a : getAllAppointments()) {
            if (a.getAppointmentId().equals(appointmentId)) {
                a.setDate(newDate);
                a.setTime(newTime);
                a.setStatus("SCHEDULED");
                DataLoader.updateAppointment(a);
                return true;
            }
        }
        return false;
    }

    /**
     * Automatically generates a incremental unique Appointment ID (e.g., Appt001, Appt002).
     */
    private static String generateAppointmentId() {
        List<Appointment> list = getAllAppointments();
        int nextIdNum = list.size() + 1;
        return String.format("Appt%03d", nextIdNum);
    }
}
