package services;

import filehandling.DataLoader;
import models.Patient;
import java.util.List;
import java.util.ArrayList;

public class PatientService {

    public static boolean addPatient(String name, String email,
                                     String password, String bloodGroup) {
        List<Patient> existing = DataLoader.loadPatients();
        for (Patient p : existing) {
            if (p.getEmail().equals(email)) {
                return false;
            }
        }
        String id = generatePatientId();
        Patient newPatient = new Patient(id, name, email, password,
                                         bloodGroup, "", 0.0);
        DataLoader.savePatient(newPatient);
        return true;
    }

    public static List<Patient> getAllPatients() {
        return DataLoader.loadPatients();
    }

    public static Patient getPatientById(String id) {
        return DataLoader.findPatient(id);
    }

    public static List<Patient> searchByName(String keyword) {
        List<Patient> results = new ArrayList<>();
        for (Patient p : DataLoader.loadPatients()) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    public static boolean updatePatient(String id, String newName,
                                        String newEmail, String bloodGroup) {
        Patient p = DataLoader.findPatient(id);
        if (p == null) return false;
        p.setName(newName);
        p.setEmail(newEmail);
        p.setBloodGroup(bloodGroup);
        DataLoader.updatePatient(p);
        return true;
    }

    public static boolean addMedicalHistory(String patientId, String entry) {
        Patient p = DataLoader.findPatient(patientId);
        if (p == null) return false;
        p.addToHistory(entry);
        DataLoader.updatePatient(p);
        return true;
    }

    public static boolean updateCharges(String patientId, double amount) {
        Patient p = DataLoader.findPatient(patientId);
        if (p == null) return false;
        p.setTotalCharges(p.getTotalCharges() + amount);
        DataLoader.updatePatient(p);
        return true;
    }

    public static boolean deletePatient(String id) {
        if (DataLoader.findPatient(id) == null) return false;
        DataLoader.deletePatient(id);
        return true;
    }

    public static String getPatientSummary(String id) {
        Patient p = DataLoader.findPatient(id);
        if (p == null) return "Patient not found.";
        return "ID         : " + p.getUserId()
             + "\nName       : " + p.getName()
             + "\nEmail      : " + p.getEmail()
             + "\nBlood Group: " + p.getBloodGroup()
             + "\nHistory    : " + p.getMedicalHistory()
             + "\nTotal Charges: Rs. " + p.getTotalCharges();
    }

    // FIXED: finds highest existing ID number instead of using list size
    private static String generatePatientId() {
        List<Patient> list = DataLoader.loadPatients();
        int max = 0;
        for (Patient p : list) {
            try {
                int num = Integer.parseInt(p.getUserId().substring(1));
                if (num > max) max = num;
            } catch (Exception e) {}
        }
        return String.format("P%03d", max + 1);
    }
}
