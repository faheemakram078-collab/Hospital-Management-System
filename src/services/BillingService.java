package services;

import filehandling.DataLoader;
import models.Bill;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class BillingService {

    public static final double CONSULTATION_FEE = 1500.0;
    public static final double LAB_FEE          = 2500.0;
    public static final double MEDICATION_FEE   = 800.0;

    public static boolean generateBill(String patientId,
                                       boolean hasConsultation,
                                       boolean hasLab,
                                       boolean hasMedication) {
        if (DataLoader.findPatient(patientId) == null) return false;

        double consultFee = hasConsultation ? CONSULTATION_FEE : 0;
        double labFee     = hasLab          ? LAB_FEE          : 0;
        double medFee     = hasMedication   ? MEDICATION_FEE   : 0;

        String id   = generateBillId();
        String date = LocalDate.now().toString();

        Bill bill = new Bill(id, patientId, consultFee,
                             labFee, medFee, date, "UNPAID");
        DataLoader.saveBill(bill);

        PatientService.updateCharges(patientId,
                                     consultFee + labFee + medFee);
        return true;
    }

    public static boolean markAsPaid(String billId) {
        List<Bill> list = DataLoader.loadBills();
        for (Bill b : list) {
            if (b.getBillId().equals(billId)) {
                b.setStatus("PAID");
                DataLoader.updateBill(b);
                return true;
            }
        }
        return false;
    }

    public static List<Bill> getAllBills() {
        return DataLoader.loadBills();
    }

    public static List<Bill> getBillsByPatient(String patientId) {
        List<Bill> results = new ArrayList<>();
        for (Bill b : DataLoader.loadBills()) {
            if (b.getPatientId().equals(patientId)) {
                results.add(b);
            }
        }
        return results;
    }

    public static List<Bill> getUnpaidBills() {
        List<Bill> results = new ArrayList<>();
        for (Bill b : DataLoader.loadBills()) {
            if (b.getStatus().equals("UNPAID")) {
                results.add(b);
            }
        }
        return results;
    }

    public static String getBillDetails(String billId) {
        for (Bill b : DataLoader.loadBills()) {
            if (b.getBillId().equals(billId)) {
                return b.getBreakdown();
            }
        }
        return "Bill not found.";
    }

    public static double getTotalRevenue() {
        double total = 0;
        for (Bill b : DataLoader.loadBills()) {
            if (b.getStatus().equals("PAID")) {
                total += b.getTotalAmount();
            }
        }
        return total;
    }

    // FIXED: finds highest existing ID number instead of using list size
    private static String generateBillId() {
        List<Bill> list = DataLoader.loadBills();
        int max = 0;
        for (Bill b : list) {
            try {
                int num = Integer.parseInt(b.getBillId().substring(1));
                if (num > max) max = num;
            } catch (Exception e) {}
        }
        return String.format("B%03d", max + 1);
    }
}
