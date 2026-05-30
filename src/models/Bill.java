package models;

public class Bill {

    private String billId;
    private String patientId;
    private double consultationFee;
    private double labFee;
    private double medicationFee;
    private double totalAmount;
    private String date;
    private String status;   // "PAID" or "UNPAID"

    public Bill(String billId, String patientId, double consultationFee,
                double labFee, double medicationFee, String date, String status) {
        this.billId          = billId;
        this.patientId       = patientId;
        this.consultationFee = consultationFee;
        this.labFee          = labFee;
        this.medicationFee   = medicationFee;
        this.totalAmount     = consultationFee + labFee + medicationFee;
        this.date            = date;
        this.status          = status;
    }

    // Getters
    public String getBillId()          { return billId; }
    public String getPatientId()       { return patientId; }
    public double getConsultationFee() { return consultationFee; }
    public double getLabFee()          { return labFee; }
    public double getMedicationFee()   { return medicationFee; }
    public double getTotalAmount()     { return totalAmount; }
    public String getDate()            { return date; }
    public String getStatus()          { return status; }

    public void setStatus(String status) { this.status = status; }

    // Detailed breakdown string (shown in GUI)
    public String getBreakdown() {
        return "Bill ID    : " + billId
             + "\nPatient ID : " + patientId
             + "\nConsultation: Rs. " + consultationFee
             + "\nLab Tests  : Rs. " + labFee
             + "\nMedication : Rs. " + medicationFee
             + "\n----------------------------"
             + "\nTOTAL      : Rs. " + totalAmount
             + "\nStatus     : " + status;
    }

    // Format: billId|patientId|consultFee|labFee|medFee|date|status
    public String toFileString() {
        return billId + "|" + patientId + "|" + consultationFee + "|"
               + labFee + "|" + medicationFee + "|" + date + "|" + status;
    }

    public static Bill fromFileString(String line) {
        String[] p = line.split("\\|");
        return new Bill(p[0], p[1],
                        Double.parseDouble(p[2]),
                        Double.parseDouble(p[3]),
                        Double.parseDouble(p[4]),
                        p[5], p[6]);
    }
}
