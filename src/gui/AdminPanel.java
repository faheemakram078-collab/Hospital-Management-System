package gui;

import models.Admin;
import services.PatientService;
import services.DoctorService;
import services.AppointmentService;
import services.BillingService;
import filehandling.DataLoader;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        JLabel title = new JLabel("Admin Control Panel", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(30, 80, 160));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(3, 2, 16, 16));
        center.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // ---- Load Live Dashboard Cards Metrics ----
        center.add(makeCard("Total Patients",
            String.valueOf(PatientService.getAllPatients().size()),
            new Color(41, 128, 185)));

        center.add(makeCard("Total Doctors",
            String.valueOf(DoctorService.getAllDoctors().size()),
            new Color(39, 174, 96)));

        center.add(makeCard("Total Appointments",
            String.valueOf(AppointmentService.getAllAppointments().size()),
            new Color(142, 68, 173)));

        center.add(makeCard("Unpaid Bills",
            String.valueOf(BillingService.getUnpaidBills().size()),
            new Color(192, 57, 43)));

        center.add(makeCard("Total Revenue",
            "Rs. " + BillingService.getTotalRevenue(),
            new Color(30, 130, 76)));

        center.add(makeCard("Emergencies",
            String.valueOf(AppointmentService.getEmergencies().size()),
            new Color(200, 100, 0)));

        add(center, BorderLayout.CENTER);

        // ---- System Action Bar ----
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addAdminBtn   = new JButton("Add Admin Account");
        JButton refreshBtn    = new JButton("Refresh Stats");

        // Fixed styling helper to accommodate macOS rendering specifications
        styleButton(addAdminBtn,  new Color(30, 80, 160));
        styleButton(refreshBtn,   new Color(100, 100, 100));

        addAdminBtn.addActionListener(e  -> showAddAdminDialog());
        refreshBtn.addActionListener(e   -> refreshStats(center));

        bottom.add(addAdminBtn);
        bottom.add(refreshBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel makeCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setPreferredSize(new Dimension(200, 100));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(new Color(220, 240, 255));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setForeground(Color.WHITE);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    private void showAddAdminDialog() {
        JTextField name  = new JTextField();
        JTextField email = new JTextField();
        JTextField pass  = new JTextField();
        JTextField dept  = new JTextField();

        Object[] fields = {
            "Name:", name, 
            "Email:", email,
            "Password:", pass, 
            "Department:", dept
        };

        int r = JOptionPane.showConfirmDialog(this, fields,
                "Register New Administrator", JOptionPane.OK_CANCEL_OPTION);

        if (r == JOptionPane.OK_OPTION) {
            int next = DataLoader.loadAdmins().size() + 1;
            String id = String.format("A%03d", next);
            
            Admin a = new Admin(id, name.getText().trim(),
                                email.getText().trim(),
                                pass.getText().trim(),
                                dept.getText().trim());
            DataLoader.saveAdmin(a);
            JOptionPane.showMessageDialog(this, "Admin account registered successfully! Generated ID: " + id);
        }
    }

    private void refreshStats(JPanel center) {
        center.removeAll();
        center.add(makeCard("Total Patients",
            String.valueOf(PatientService.getAllPatients().size()),
            new Color(41, 128, 185)));
        center.add(makeCard("Total Doctors",
            String.valueOf(DoctorService.getAllDoctors().size()),
            new Color(39, 174, 96)));
        center.add(makeCard("Total Appointments",
            String.valueOf(AppointmentService.getAllAppointments().size()),
            new Color(142, 68, 173)));
        center.add(makeCard("Unpaid Bills",
            String.valueOf(BillingService.getUnpaidBills().size()),
            new Color(192, 57, 43)));
        center.add(makeCard("Total Revenue",
            "Rs. " + BillingService.getTotalRevenue(),
            new Color(30, 130, 76)));
        center.add(makeCard("Emergencies",
            String.valueOf(AppointmentService.getEmergencies().size()),
            new Color(200, 100, 0)));
            
        center.revalidate();
        center.repaint();
    }

    /**
     * Corrected button styler configured specifically for Mac display profiles.
     */
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        
        // CRITICAL MAC FIXES: Forces native Aqua engine to recognize custom colors and boundaries
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false); 
        
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Give explicit padding room so text strings don't compress out of bounds
        btn.setPreferredSize(new Dimension(185, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
