package gui;

import models.Bill;
import services.BillingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillingPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public BillingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadTable();
    }

    private void buildUI() {
        // ---- Top ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton allBtn    = new JButton("All Bills");
        JButton unpaidBtn = new JButton("Unpaid Only");
        JLabel  revenue   = new JLabel();

        // Applied MacBook Aqua styling to top navigation buttons as well
        styleButton(allBtn,    new Color(100, 100, 100));
        styleButton(unpaidBtn, new Color(192, 57, 43));

        allBtn.addActionListener(e -> loadTable());
        unpaidBtn.addActionListener(e -> loadUnpaid());

        top.add(allBtn);
        top.add(unpaidBtn);
        top.add(new JLabel("     "));
        top.add(revenue);
        add(top, BorderLayout.NORTH);

        // Update revenue label
        revenue.setText("Total Revenue Collected: Rs. "
                         + BillingService.getTotalRevenue());

        // ---- Center: table ----
        String[] cols = {"Bill ID", "Patient ID", "Consultation",
                         "Lab", "Medication", "Total", "Date", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Bottom: buttons ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton generateBtn = new JButton("Generate Bill");
        JButton payBtn      = new JButton("Mark as Paid");
        JButton detailBtn   = new JButton("View Details");

        styleButton(generateBtn, new Color(30, 130, 76));
        styleButton(payBtn,      new Color(41, 128, 185));
        styleButton(detailBtn,   new Color(100, 100, 100));

        generateBtn.addActionListener(e -> showGenerateDialog());
        payBtn.addActionListener(e      -> markPaid());
        detailBtn.addActionListener(e   -> viewDetails());

        bottom.add(generateBtn);
        bottom.add(payBtn);
        bottom.add(detailBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Bill b : BillingService.getAllBills()) {
            tableModel.addRow(new Object[]{
                b.getBillId(), b.getPatientId(),
                b.getConsultationFee(), b.getLabFee(),
                b.getMedicationFee(),  b.getTotalAmount(),
                b.getDate(), b.getStatus()
            });
        }
    }

    private void loadUnpaid() {
        tableModel.setRowCount(0);
        for (Bill b : BillingService.getUnpaidBills()) {
            tableModel.addRow(new Object[]{
                b.getBillId(), b.getPatientId(),
                b.getConsultationFee(), b.getLabFee(),
                b.getMedicationFee(),  b.getTotalAmount(),
                b.getDate(), b.getStatus()
            });
        }
    }

    private void showGenerateDialog() {
        JTextField patientId = new JTextField();
        JCheckBox consult    = new JCheckBox("Consultation  Rs."
                               + BillingService.CONSULTATION_FEE, true);
        JCheckBox lab        = new JCheckBox("Lab Tests      Rs."
                               + BillingService.LAB_FEE);
        JCheckBox medication = new JCheckBox("Medication     Rs."
                               + BillingService.MEDICATION_FEE);

        Object[] fields = {
            "Patient ID:", patientId,
            "Services included:", consult, lab, medication
        };

        int r = JOptionPane.showConfirmDialog(this, fields,
                "Generate Bill", JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) {
            boolean ok = BillingService.generateBill(
                patientId.getText().trim(),
                consult.isSelected(),
                lab.isSelected(),
                medication.isSelected());

            if (ok) {
                JOptionPane.showMessageDialog(this, "Bill generated!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed — patient ID not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void markPaid() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        BillingService.markAsPaid(id);
        loadTable();
    }

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        JOptionPane.showMessageDialog(this,
            BillingService.getBillDetails(id),
            "Bill Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSelectWarning() {
        JOptionPane.showMessageDialog(this, "Please select a row first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Corrected button styler configured specifically for MacBook display layouts.
     */
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        
        // CRITICAL MAC FIX: Forces opaque layout rendering to stop background bleaching 
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Give structural dimensions so text strings don't compress out of bounds
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
