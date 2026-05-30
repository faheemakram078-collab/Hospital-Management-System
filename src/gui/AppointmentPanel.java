package gui;

import models.Appointment;
import services.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppointmentPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadTable();
    }

    private void buildUI() {
        // ---- Top: Filter Tools Panel ----
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton allBtn   = new JButton("All");
        JButton emergBtn = new JButton("Emergencies");
        
        // Fixed top filter buttons for Mac rendering compatibility
        styleButton(allBtn,   new Color(100, 100, 100));
        styleButton(emergBtn, new Color(200, 50, 50));
        
        allBtn.addActionListener(e   -> loadTable());
        emergBtn.addActionListener(e -> loadEmergencies());
        
        top.add(new JLabel("Filter:"));
        top.add(allBtn);
        top.add(emergBtn);
        add(top, BorderLayout.NORTH);

        // ---- Center: Appointments Table View ----
        String[] cols = {"ID", "Patient ID", "Doctor ID", "Date", "Time", "Status", "Notes"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Bottom: Action Buttons Management Panel ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton schedBtn    = new JButton("Schedule");
        JButton emergScBtn  = new JButton("Emergency");
        JButton cancelBtn   = new JButton("Cancel");
        JButton completeBtn = new JButton("Complete");
        JButton reschedBtn  = new JButton("Reschedule");

        styleButton(schedBtn,    new Color(30, 130, 76));
        styleButton(emergScBtn,  new Color(200, 50, 50));
        styleButton(cancelBtn,   new Color(192, 57, 43));
        styleButton(completeBtn, new Color(41, 128, 185));
        styleButton(reschedBtn,  new Color(142, 68, 173));

        schedBtn.addActionListener(e    -> showScheduleDialog(false));
        emergScBtn.addActionListener(e  -> showScheduleDialog(true));
        cancelBtn.addActionListener(e   -> cancelSelected());
        completeBtn.addActionListener(e -> completeSelected());
        reschedBtn.addActionListener(e  -> rescheduleSelected());

        bottom.add(schedBtn);
        bottom.add(emergScBtn);
        bottom.add(cancelBtn);
        bottom.add(completeBtn);
        bottom.add(reschedBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Appointment a : AppointmentService.getAllAppointments()) {
            tableModel.addRow(new Object[]{
                a.getAppointmentId(), a.getPatientId(), a.getDoctorId(),
                a.getDate(), a.getTime(), a.getStatus(), a.getNotes()
            });
        }
    }

    private void loadEmergencies() {
        tableModel.setRowCount(0);
        for (Appointment a : AppointmentService.getEmergencies()) {
            tableModel.addRow(new Object[]{
                a.getAppointmentId(), a.getPatientId(), a.getDoctorId(),
                a.getDate(), a.getTime(), a.getStatus(), a.getNotes()
            });
        }
    }

    private void showScheduleDialog(boolean isEmergency) {
        JTextField patientId = new JTextField();
        JTextField doctorId  = new JTextField();
        JTextField notes     = new JTextField();

        if (isEmergency) {
            Object[] fields = {
                "Patient ID:", patientId,
                "Doctor ID:",  doctorId,
                "Notes:",      notes
            };
            int r = JOptionPane.showConfirmDialog(this, fields,
                    "Schedule Emergency", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                boolean ok = AppointmentService.scheduleEmergency(
                    patientId.getText().trim(),
                    doctorId.getText().trim(),
                    notes.getText().trim());
                showResult(ok, "Emergency scheduled successfully!", "Failed — please verify IDs.");
                loadTable();
            }
        } else {
            JTextField date = new JTextField("2026-01-01");
            JTextField time = new JTextField("10:00");
            Object[] fields = {
                "Patient ID:", patientId,
                "Doctor ID:",  doctorId,
                "Date (YYYY-MM-DD):", date,
                "Time (HH:MM):", time,
                "Notes:", notes
            };
            int r = JOptionPane.showConfirmDialog(this, fields,
                    "Schedule Appointment", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                boolean ok = AppointmentService.scheduleAppointment(
                    patientId.getText().trim(), doctorId.getText().trim(),
                    date.getText().trim(), time.getText().trim(),
                    notes.getText().trim());
                showResult(ok, "Appointment scheduled successfully!", "Failed — please verify IDs.");
                loadTable();
            }
        }
    }

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        AppointmentService.cancelAppointment(id);
        loadTable();
    }

    private void completeSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        AppointmentService.completeAppointment(id);
        loadTable();
    }

    private void rescheduleSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { showSelectWarning(); return; }
        String id   = (String) tableModel.getValueAt(row, 0);
        JTextField date = new JTextField();
        JTextField time = new JTextField();
        Object[] fields = {
            "New Date (YYYY-MM-DD):", date,
            "New Time (HH:MM):", time
        };
        int r = JOptionPane.showConfirmDialog(this, fields,
                "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            AppointmentService.reschedule(id,
                date.getText().trim(), time.getText().trim());
            loadTable();
        }
    }

    private void showResult(boolean ok, String success, String fail) {
        if (ok) JOptionPane.showMessageDialog(this, success);
        else    JOptionPane.showMessageDialog(this, fail, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSelectWarning() {
        JOptionPane.showMessageDialog(this, "Please select an appointment row from the table first.",
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
        
        // Give explicit padding dimensions so the text doesn't compress or clip on Retina displays
        btn.setPreferredSize(new Dimension(130, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}