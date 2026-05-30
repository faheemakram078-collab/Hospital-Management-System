package gui;

import models.Doctor;
import services.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DoctorPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public DoctorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadTable();
    }

    private void buildUI() {
        // ---- Top: search ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Search by specialization:"));
        searchField = new JTextField(20);
        
        // FIX: Increase height slightly for the search text field to look natural on Mac
        searchField.setPreferredSize(new Dimension(200, 30));
        
        JButton searchBtn  = new JButton("Search");
        JButton refreshBtn = new JButton("Show All");
        
        // Styled top layout items to avoid native bleaching bugs
        styleButton(searchBtn,  new Color(30, 80, 160));
        styleButton(refreshBtn, new Color(100, 100, 100));

        searchBtn.addActionListener(e  -> searchDoctors());
        refreshBtn.addActionListener(e -> loadTable());
        
        top.add(searchField);
        top.add(searchBtn);
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        // ---- Center: table ----
        String[] cols = {"ID", "Name", "Email",
                         "Specialization", "Availability", "Patients Handled"};
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
        JButton addBtn     = new JButton("Add Doctor");
        JButton updateBtn  = new JButton("Update");
        JButton deleteBtn  = new JButton("Delete");
        JButton summaryBtn = new JButton("View Summary");

        styleButton(addBtn,     new Color(30, 130, 76));
        styleButton(updateBtn,  new Color(41, 128, 185));
        styleButton(deleteBtn,  new Color(192, 57, 43));
        styleButton(summaryBtn, new Color(100, 100, 100));

        addBtn.addActionListener(e    -> showAddDialog());
        updateBtn.addActionListener(e -> showUpdateDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        summaryBtn.addActionListener(e -> showSummary());

        bottom.add(addBtn);
        bottom.add(updateBtn);
        bottom.add(deleteBtn);
        bottom.add(summaryBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Doctor d : DoctorService.getAllDoctors()) {
            tableModel.addRow(new Object[]{
                d.getUserId(), d.getName(), d.getEmail(),
                d.getSpecialization(), d.getAvailability(),
                d.getPatientsHandled()
            });
        }
    }

    private void searchDoctors() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { loadTable(); return; }
        tableModel.setRowCount(0);
        for (Doctor d : DoctorService.searchBySpecialization(kw)) {
            tableModel.addRow(new Object[]{
                d.getUserId(), d.getName(), d.getEmail(),
                d.getSpecialization(), d.getAvailability(),
                d.getPatientsHandled()
            });
        }
    }

    private void showAddDialog() {
        JTextField name   = new JTextField();
        JTextField email  = new JTextField();
        JTextField pass   = new JTextField();
        JTextField spec   = new JTextField();
        JTextField avail  = new JTextField();

        Object[] fields = {
            "Full Name:", name, "Email:", email,
            "Password:", pass,
            "Specialization (e.g. Cardiology):", spec,
            "Availability (e.g. Mon-Fri 9-5):", avail
        };

        int r = JOptionPane.showConfirmDialog(this, fields,
                "Add New Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            boolean ok = DoctorService.addDoctor(
                name.getText().trim(), email.getText().trim(),
                pass.getText().trim(), spec.getText().trim(),
                avail.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Doctor added!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed! Email may already exist.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }

        String id     = (String) tableModel.getValueAt(row, 0);
        JTextField name  = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField spec  = new JTextField((String) tableModel.getValueAt(row, 3));
        JTextField avail = new JTextField((String) tableModel.getValueAt(row, 4));

        Object[] fields = {
            "Name:", name,
            "Specialization:", spec,
            "Availability:", avail
        };

        int r = JOptionPane.showConfirmDialog(this, fields,
                "Update Doctor", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            DoctorService.updateDoctor(id, name.getText().trim(),
                spec.getText().trim(), avail.getText().trim());
            loadTable();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int c = JOptionPane.showConfirmDialog(this,
                "Delete doctor " + id + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            DoctorService.deleteDoctor(id);
            loadTable();
        }
    }

    private void showSummary() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        JOptionPane.showMessageDialog(this,
            DoctorService.getDoctorSummary(id),
            "Doctor Summary", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Balanced dimension layout to guarantee text strings like "View Summary" fit perfectly
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
