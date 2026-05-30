package gui;

import filehandling.DataLoader;
import filehandling.FileManager;
import models.User;
import models.Admin;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private MainFrame mainFrame;

    // Input fields
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255));
        buildUI();
        seedDefaultAdmin();  // creates a default admin on first run
    }

    private void buildUI() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // ---- Title ----
        JLabel title = new JLabel("Hospital Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 80, 160));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(title, g);

        JLabel subtitle = new JLabel("Please login to continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        g.gridy = 1;
        add(subtitle, g);

        // ---- Email ----
        g.gridy = 2; g.gridwidth = 1; g.gridx = 0;
        add(new JLabel("Email:"), g);
        emailField = new JTextField(20);
        // Mac layout dimension balancing
        emailField.setPreferredSize(new Dimension(220, 32));
        g.gridx = 1;
        add(emailField, g);

        // ---- Password ----
        g.gridy = 3; g.gridx = 0;
        add(new JLabel("Password:"), g);
        passwordField = new JPasswordField(20);
        // Mac layout dimension balancing
        passwordField.setPreferredSize(new Dimension(220, 32));
        g.gridx = 1;
        add(passwordField, g);

        // ---- Login Button ----
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(30, 80, 160));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // CRITICAL MAC FIX: Forces opaque layout rendering to stop background bleaching on macOS Aqua LookAndFeel
        loginBtn.setOpaque(true);
        loginBtn.setContentAreaFilled(true);
        loginBtn.setBorderPainted(false);
        
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(220, 40));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        g.gridy = 4; g.gridx = 0; g.gridwidth = 2;
        add(loginBtn, g);

        // ---- Message Label (shows errors) ----
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        g.gridy = 5;
        add(messageLabel, g);

        // ---- Default credentials hint ----
        JLabel hint = new JLabel("Default Admin — email: admin@hospital.com  password: admin123",
                                  SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        g.gridy = 6;
        add(hint, g);

        // ---- Action ----
        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin()); // Enter key
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
            return;
        }

        User user = DataLoader.login(email, password);

        if (user == null) {
            messageLabel.setText("Invalid email or password. Try again.");
            passwordField.setText("");
        } else {
            messageLabel.setText("");
            mainFrame.showDashboard(user);  // hand off to MainFrame
        }
    }

    // Creates a default admin account on very first run
    // so you can always log in
    private void seedDefaultAdmin() {
        if (!FileManager.exists(FileManager.ADMINS_FILE, "A000")) {
            Admin defaultAdmin = new Admin("A000", "Administrator",
                                           "admin@hospital.com",
                                           "admin123", "General");
            DataLoader.saveAdmin(defaultAdmin);
        }
    }
}
