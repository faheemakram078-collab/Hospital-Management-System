package models;

// INHERITANCE: Patient extends User — gets all User fields for free
// INTERFACE: implements Billable — must provide calculateBill() and getBillSummary()
import interfaces.Billable;

public class Patient extends User implements Billable {

    private String bloodGroup;
    private String medicalHistory;  // stored as a single string, entries separated by ";"
    private double totalCharges;

    public Patient(String userId, String name, String email,
                   String password, String bloodGroup,
                   String medicalHistory, double totalCharges) {
        // super() calls the parent (User) constructor
        super(userId, name, email, password, "PATIENT");
        this.bloodGroup     = bloodGroup;
        this.medicalHistory = medicalHistory;
        this.totalCharges   = totalCharges;
    }

    // POLYMORPHISM: each subclass gives its own version of getDisplayInfo()
    @Override
    public String getDisplayInfo() {
        return "Patient: " + getName() + " | Blood: " + bloodGroup
               + " | History: " + medicalHistory;
    }

    // INTERFACE METHOD: must implement because Patient implements Billable
    @Override
    public double calculateBill() {
        return totalCharges;
    }

    @Override
    public String getBillSummary() {
        return "Patient: " + getName() + "\nTotal Charges: Rs. " + totalCharges;
    }

    // Getters
    public String getBloodGroup()     { return bloodGroup; }
    public String getMedicalHistory() { return medicalHistory; }
    public double getTotalCharges()   { return totalCharges; }

    // Setters
    public void setBloodGroup(String bloodGroup)         { this.bloodGroup = bloodGroup; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public void setTotalCharges(double totalCharges)     { this.totalCharges = totalCharges; }

    // Add a new entry to history (appends with semicolon)
    public void addToHistory(String entry) {
        if (this.medicalHistory.isEmpty()) {
            this.medicalHistory = entry;
        } else {
            this.medicalHistory += ";" + entry;
        }
    }

    // Save to file — extends parent's toFileString()
    // Format: userId|name|email|password|PATIENT|bloodGroup|medicalHistory|totalCharges
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + bloodGroup + "|"
               + medicalHistory + "|" + totalCharges;
    }

    // Rebuild a Patient object from one line read from patients.txt
    public static Patient fromFileString(String line) {
        String[] p = line.split("\\|");
        return new Patient(p[0], p[1], p[2], p[3], p[5], p[6], Double.parseDouble(p[7]));
    }
}
