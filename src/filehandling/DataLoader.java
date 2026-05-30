package filehandling;

import models.*;
import java.util.*;

// DataLoader converts raw file lines into Java objects and back.
// FileManager handles the physical file.
// DataLoader handles the conversion — two responsibilities, two classes.

public class DataLoader {

    // Standard copies matching FileManager paths to satisfy direct panel references
    public static final String PATIENTS_FILE = "data/patients.txt";
    public static final String DOCTORS_FILE = "data/doctors.txt";
    public static final String ADMINS_FILE = "data/admins.txt";
    public static final String APPOINTMENTS_FILE = "data/appointments.txt";
    public static final String BILLS_FILE = "data/bills.txt";

    // ---------------------------------------------------------------
    // PATIENTS
    // ---------------------------------------------------------------
    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();
        List<String> lines = FileManager.readAll(PATIENTS_FILE);
        for (String line : lines) {
            try {
                patients.add(Patient.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Skipping bad patient line: " + line);
            }
        }
        return patients;
    }

    public static void savePatient(Patient p) {
        FileManager.appendLine(PATIENTS_FILE, p.toFileString());
    }

    public static void updatePatient(Patient p) {
        FileManager.updateById(PATIENTS_FILE, p.getUserId(), p.toFileString());
    }

    public static void deletePatient(String patientId) {
        FileManager.deleteById(PATIENTS_FILE, patientId);
    }

    public static Patient findPatient(String patientId) {
        String line = FileManager.findById(PATIENTS_FILE, patientId);
        if (line == null) return null;
        return Patient.fromFileString(line);
    }

    // ---------------------------------------------------------------
    // DOCTORS
    // ---------------------------------------------------------------
    public static List<Doctor> loadDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        List<String> lines = FileManager.readAll(DOCTORS_FILE);
        for (String line : lines) {
            try {
                doctors.add(Doctor.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Skipping bad doctor line: " + line);
            }
        }
        return doctors;
    }

    public static void saveDoctor(Doctor d) {
        FileManager.appendLine(DOCTORS_FILE, d.toFileString());
    }

    public static void updateDoctor(Doctor d) {
        FileManager.updateById(DOCTORS_FILE, d.getUserId(), d.toFileString());
    }

    public static void deleteDoctor(String doctorId) {
        FileManager.deleteById(DOCTORS_FILE, doctorId);
    }

    public static Doctor findDoctor(String doctorId) {
        String line = FileManager.findById(DOCTORS_FILE, doctorId);
        if (line == null) return null;
        return Doctor.fromFileString(line);
    }

    // ---------------------------------------------------------------
    // ADMINS
    // ---------------------------------------------------------------
    public static List<Admin> loadAdmins() {
        List<Admin> admins = new ArrayList<>();
        List<String> lines = FileManager.readAll(ADMINS_FILE);
        for (String line : lines) {
            try {
                admins.add(Admin.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Skipping bad admin line: " + line);
            }
        }
        return admins;
    }

    public static void saveAdmin(Admin a) {
        FileManager.appendLine(ADMINS_FILE, a.toFileString());
    }

    public static void updateAdmin(Admin a) {
        FileManager.updateById(ADMINS_FILE, a.getUserId(), a.toFileString());
    }

    public static void deleteAdmin(String adminId) {
        FileManager.deleteById(ADMINS_FILE, adminId);
    }

    // ---------------------------------------------------------------
    // APPOINTMENTS
    // ---------------------------------------------------------------
    public static List<Appointment> loadAppointments() {
        List<Appointment> list = new ArrayList<>();
        List<String> lines = FileManager.readAll(APPOINTMENTS_FILE);
        for (String line : lines) {
            try {
                list.add(Appointment.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Skipping bad appointment line: " + line);
            }
        }
        return list;
    }

    public static void saveAppointment(Appointment a) {
        FileManager.appendLine(APPOINTMENTS_FILE, a.toFileString());
    }

    public static void updateAppointment(Appointment a) {
        FileManager.updateById(APPOINTMENTS_FILE,
                               a.getAppointmentId(), a.toFileString());
    }

    public static void deleteAppointment(String appointmentId) {
        FileManager.deleteById(APPOINTMENTS_FILE, appointmentId);
    }

    // ---------------------------------------------------------------
    // BILLS
    // ---------------------------------------------------------------
    public static List<Bill> loadBills() {
        List<Bill> list = new ArrayList<>();
        List<String> lines = FileManager.readAll(BILLS_FILE);
        for (String line : lines) {
            try {
                list.add(Bill.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Skipping bad bill line: " + line);
            }
        }
        return list;
    }

    public static void saveBill(Bill b) {
        FileManager.appendLine(BILLS_FILE, b.toFileString());
    }

    public static void updateBill(Bill b) {
        FileManager.updateById(BILLS_FILE, b.getBillId(), b.toFileString());
    }

    // ---------------------------------------------------------------
    // LOGIN CHECK
    // ---------------------------------------------------------------
    public static models.User login(String email, String password) {
        // Check patients
        for (Patient p : loadPatients()) {
            if (p.getEmail().equals(email) && p.getPassword().equals(password)) {
                return p;
            }
        }
        // Check doctors
        for (Doctor d : loadDoctors()) {
            if (d.getEmail().equals(email) && d.getPassword().equals(password)) {
                return d;
            }
        }
        // Check admins
        for (Admin a : loadAdmins()) {
            if (a.getEmail().equals(email) && a.getPassword().equals(password)) {
                return a;
            }
        }
        return null;  // login failed
    }
}
