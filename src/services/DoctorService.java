package services;

import filehandling.DataLoader;
import models.Doctor;
import java.util.List;
import java.util.ArrayList;

public class DoctorService {

    public static boolean addDoctor(String name, String email, String password,
                                    String specialization, String availability) {
        for (Doctor d : DataLoader.loadDoctors()) {
            if (d.getEmail().equals(email)) return false;
        }
        String id = generateDoctorId();
        Doctor doc = new Doctor(id, name, email, password,
                                specialization, availability, 0);
        DataLoader.saveDoctor(doc);
        return true;
    }

    public static List<Doctor> getAllDoctors() {
        return DataLoader.loadDoctors();
    }

    public static Doctor getDoctorById(String id) {
        return DataLoader.findDoctor(id);
    }

    public static List<Doctor> searchBySpecialization(String keyword) {
        List<Doctor> results = new ArrayList<>();
        for (Doctor d : DataLoader.loadDoctors()) {
            if (d.getSpecialization().toLowerCase()
                 .contains(keyword.toLowerCase())) {
                results.add(d);
            }
        }
        return results;
    }

    public static boolean updateDoctor(String id, String name,
                                       String specialization,
                                       String availability) {
        Doctor d = DataLoader.findDoctor(id);
        if (d == null) return false;
        d.setName(name);
        d.setSpecialization(specialization);
        d.setAvailability(availability);
        DataLoader.updateDoctor(d);
        return true;
    }

    public static boolean deleteDoctor(String id) {
        if (DataLoader.findDoctor(id) == null) return false;
        DataLoader.deleteDoctor(id);
        return true;
    }

    public static String getDoctorSummary(String id) {
        Doctor d = DataLoader.findDoctor(id);
        if (d == null) return "Doctor not found.";
        return "ID             : " + d.getUserId()
             + "\nName           : Dr. " + d.getName()
             + "\nSpecialization : " + d.getSpecialization()
             + "\nAvailability   : " + d.getAvailability()
             + "\nPatients Handled: " + d.getPatientsHandled();
    }

    // FIXED: finds highest existing ID number instead of using list size
    private static String generateDoctorId() {
        List<Doctor> list = DataLoader.loadDoctors();
        int max = 0;
        for (Doctor d : list) {
            try {
                int num = Integer.parseInt(d.getUserId().substring(1));
                if (num > max) max = num;
            } catch (Exception e) {}
        }
        return String.format("D%03d", max + 1);
    }
}
