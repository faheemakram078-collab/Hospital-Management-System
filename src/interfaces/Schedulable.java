package interfaces;

// INTERFACE: anything that can be scheduled (appointments).
public interface Schedulable {
    boolean scheduleAppointment(String date, String time);  // returns true if successful
    boolean cancelAppointment(String appointmentId);        // returns true if cancelled
}
