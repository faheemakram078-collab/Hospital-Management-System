package gui;

import models.User;

import javax.swing.*;
import java.awt.*;

// MainFrame is the outer window.
// It swaps panels (Login → Dashboard) using CardLayout.

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel     cardPanel;

    public MainFrame() {
        setTitle("Hospital Management System");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // center on screen

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        // Start with login screen
        cardPanel.add(new LoginPanel(this), "LOGIN");
        add(cardPanel);

        cardLayout.show(cardPanel, "LOGIN");
        setVisible(true);
    }

    // Called by LoginPanel after successful login
    // Builds the dashboard for the logged-in user's role
    public void showDashboard(User user) {
        // Remove old dashboard if exists
        cardPanel.removeAll();
        cardPanel.add(new LoginPanel(this), "LOGIN");

        // Build role-based dashboard
        JPanel dashboard = buildDashboard(user);
        cardPanel.add(dashboard, "DASHBOARD");
        cardLayout.show(cardPanel, "DASHBOARD");
    }

    private JPanel buildDashboard(User user) {
        JPanel root = new JPanel(new BorderLayout());

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 80, 160));
        topBar.setPreferredSize(new Dimension(0, 50));

        JLabel welcome = new JLabel("  Welcome, " + user.getName()
                                    + "  [" + user.getRole() + "]");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        topBar.add(welcome, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(200, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        
        // CRITICAL MAC FIX: Forces opaque layout rendering to stop background bleaching on macOS Aqua LookAndFeel
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        logoutBtn.setBorderPainted(false);
        
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setFocusPainted(false);
        // Balance layout dimensions for top bar alignment
        logoutBtn.setPreferredSize(new Dimension(95, 34));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        logoutBtn.addActionListener(e -> {
            cardPanel.removeAll();
            cardPanel.add(new LoginPanel(this), "LOGIN");
            cardLayout.show(cardPanel, "LOGIN");
        });
        
        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rightBar.setBackground(new Color(30, 80, 160));
        rightBar.add(logoutBtn);
        topBar.add(rightBar, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);

        // ---- Tabbed pane (role-based tabs) ----
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        String role = user.getRole();

        // ADMIN sees everything
        if (role.equals("ADMIN")) {
            tabs.addTab("Patients",     new PatientPanel());
            tabs.addTab("Doctors",      new DoctorPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
            tabs.addTab("Billing",      new BillingPanel());
            tabs.addTab("Admin",        new AdminPanel());
        }

        // DOCTOR sees patients and their own appointments
        else if (role.equals("DOCTOR")) {
            tabs.addTab("Patients",     new PatientPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
        }

        // PATIENT sees only their own info and billing
        else if (role.equals("PATIENT")) {
            tabs.addTab("My Info",   new PatientPanel());
            tabs.addTab("My Bills",  new BillingPanel());
        }

        root.add(tabs, BorderLayout.CENTER);
        return root;
    }
}
