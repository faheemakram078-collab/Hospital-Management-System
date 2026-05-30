package gui;

import models.Patient;
import services.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadTable();
    }

    private void buildUI() {
        // ---- Top: search bar ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search by name:"));
        searchField = new JTextField(20);
        
        // Balanced sizing adjustment for Mac text input rows
        searchField.setPreferredSize(new Dimension(200, 30));
        
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Show All");

        // Styled top layout elements to bypass native background bleaching bugs
        styleButton(searchBtn,  new Color(30, 80, 160));
        styleButton(refreshBtn, new Color(100, 100, 100));

        searchBtn.addActionListener(e -> searchPatients());
        refreshBtn.addActionListener(e -> loadTable());

        top.add(searchField);
        top.add(searchBtn);
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        // ---- Center: table ----
        String[] columns = {"ID", "Name", "Email", "Blood Group",
                            "Medical History", "Total Charges"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Bottom: action buttons ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addBtn    = new JButton("Add Patient");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton historyBtn = new JButton("Add History");
        JButton summaryBtn = new JButton("View Summary");

        styleButton(addBtn,    new Color(30, 130, 76));
        styleButton(updateBtn, new Color(41, 128, 185));
        styleButton(deleteBtn, new Color(192, 57, 43));
        styleButton(historyBtn, new Color(142, 68, 173));
        styleButton(summaryBtn, new Color(100, 100, 100));

        addBtn.addActionListener(e    -> showAddDialog());
        updateBtn.addActionListener(e -> showUpdateDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        historyBtn.addActionListener(e -> showHistoryDialog());
        summaryBtn.addActionListener(e -> showSummary());

        bottom.add(addBtn);
        bottom.add(updateBtn);
        bottom.add(deleteBtn);
        bottom.add(historyBtn);
        bottom.add(summaryBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // ---- Load all patients into table ----
    private void loadTable() {
        tableModel.setRowCount(0);
        for (Patient p : PatientService.getAllPatients()) {
            tableModel.addRow(new Object[]{
                p.getUserId(), p.getName(), p.getEmail(),
                p.getBloodGroup(), p.getMedicalHistory(),
                "Rs. " + p.getTotalCharges()
            });
        }
    }

    private void searchPatients() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) { loadTable(); return; }
        tableModel.setRowCount(0);
        for (Patient p : PatientService.searchByName(keyword)) {
            tableModel.addRow(new Object[]{
                p.getUserId(), p.getName(), p.getEmail(),
                p.getBloodGroup(), p.getMedicalHistory(),
                "Rs. " + p.getTotalCharges()
            });
        }
    }

    // ---- Add patient dialog ----
    private void showAddDialog() {
        JTextField name  = new JTextField();
        JTextField email = new JTextField();
        JTextField pass  = new JTextField();
        JTextField blood = new JTextField();

        Object[] fields = {
            "Full Name:", name,
            "Email:",     email,
            "Password:",  pass,
            "Blood Group (A+/B- etc):", blood
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                     "Add New Patient", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            boolean ok = PatientService.addPatient(
                name.getText().trim(), email.getText().trim(),
                pass.getText().trim(), blood.getText().trim());

            if (ok) {
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed! Email may already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---- Update dialog ----
    private void showUpdateDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }

        String id    = (String) tableModel.getValueAt(row, 0);
        JTextField name  = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField email = new JTextField((String) tableModel.getValueAt(row, 2));
        JTextField blood = new JTextField((String) tableModel.getValueAt(row, 3));

        Object[] fields = {
            "Name:",        name,
            "Email:",       email,
            "Blood Group:", blood
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                     "Update Patient", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            PatientService.updatePatient(id, name.getText().trim(),
                email.getText().trim(), blood.getText().trim());
            loadTable();
        }
    }

    // ---- Delete selected ----
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }

        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                      "Delete patient " + id + "?",
                      "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PatientService.deletePatient(id);
            loadTable();
        }
    }

    // ---- Add history entry ----
    private void showHistoryDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }

        String id    = (String) tableModel.getValueAt(row, 0);
        String entry = JOptionPane.showInputDialog(this,
                       "Enter history entry for patient " + id + ":");
        if (entry != null && !entry.trim().isEmpty()) {
            PatientService.addMedicalHistory(id, entry.trim());
            loadTable();
        }
    }

    // ---- View summary ----
    private void showSummary() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }

        String id      = (String) tableModel.getValueAt(row, 0);
        String summary = PatientService.getPatientSummary(id);
        JOptionPane.showMessageDialog(this, summary,
            "Patient Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSelectWarning() {
        JOptionPane.showMessageDialog(this,
            "Please select a row first.",
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
        
        // Balanced dimensions to ensure multi-word strings like "View Summary" fit without clipping
        btn.setPreferredSize(new Dimension(135, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
