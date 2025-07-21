// src/Main.java

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoleSelectionPage());
    }
}

class RoleSelectionPage extends JFrame {
    public RoleSelectionPage() {
        setTitle("Event Booking System - Select Role");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Choose Role", SwingConstants.CENTER);
        JButton adminBtn = new JButton("Admin");
        JButton guestBtn = new JButton("Guest");

        adminBtn.addActionListener(e -> {
            dispose();
            new LoginPage("admin");
        });
        guestBtn.addActionListener(e -> {
            dispose();
            new LoginPage("guest");
        });

        setLayout(new GridLayout(3, 1));
        add(label);
        add(adminBtn);
        add(guestBtn);

        setVisible(true);
    }
}

class LoginPage extends JFrame {
    private final String role;

    public LoginPage(String role) {
        this.role = role;
        setTitle("Login - " + role);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (SystemManager.validateLogin(role, username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                if (role.equals("admin")) new AdminMainPage();
                else new UserCategoryPage();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterPage(role);
        });

        backBtn.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        setLayout(new GridLayout(4, 2));
        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(loginBtn); add(registerBtn);
        add(backBtn);

        setVisible(true);
    }
}

class RegisterPage extends JFrame {
    public RegisterPage(String role) {
        setTitle("Register - " + role);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("New Username:");
        JLabel passLabel = new JLabel("New Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (SystemManager.registerUser(role, username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new LoginPage(role);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new LoginPage(role);
        });

        setLayout(new GridLayout(3, 2));
        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(registerBtn); add(backBtn);

        setVisible(true);
    }
}

class AdminMainPage extends JFrame {
    public AdminMainPage() {
        setTitle("Admin Main Page");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton manageEventBtn = new JButton("Add/Delete Event");
        JButton salesBtn = new JButton("View Sales Report");
        JButton backBtn = new JButton("Logout");

        manageEventBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "(To Be Implemented) Add/Delete Event Page"));
        salesBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "(To Be Implemented) Sales Report Page"));
        backBtn.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        setLayout(new GridLayout(3, 1));
        add(manageEventBtn);
        add(salesBtn);
        add(backBtn);

        setVisible(true);
    }
}

class UserCategoryPage extends JFrame {
    public UserCategoryPage() {
        setTitle("Event Categories");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Select Event Category", SwingConstants.CENTER);
        JButton healthBtn = new JButton("Health Seminars");
        JButton techBtn = new JButton("Technology Seminars");
        JButton financeBtn = new JButton("Financial Seminars");
        JButton backBtn = new JButton("Logout");

        healthBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "(To Be Implemented) Health Events"));
        techBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "(To Be Implemented) Tech Events"));
        financeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "(To Be Implemented) Finance Events"));
        backBtn.addActionListener(e -> {
            dispose();
            new RoleSelectionPage();
        });

        setLayout(new GridLayout(5, 1));
        add(label);
        add(healthBtn);
        add(techBtn);
        add(financeBtn);
        add(backBtn);

        setVisible(true);
    }
}

class SystemManager {
    private static final String USER_FILE = "users.txt";

    public static boolean validateLogin(String role, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(role) && parts[1].equals(username) && parts[2].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String role, String username, String password) {
        if (userExists(role, username)) return false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(role + "," + username + "," + password);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean userExists(String role, String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(role) && parts[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}