package Program;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginAndRegister extends JFrame {
    private static final String USER_FILE = "C:\\Degree y2\\OO\\AssignmentV2\\Database\\User\\User.txt";
    private static final String ADMIN_FILE = "C:\\Degree y2\\OO\\AssignmentV2\\Database\\Admin\\Admin.txt";

    // Make actionBox a field so we can switch it after registering
    private JComboBox<String> roleBox;
    private JComboBox<String> actionBox;

    public LoginAndRegister() {
        setTitle("Login and Register");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Select Role:"));
        roleBox = new JComboBox<>(new String[]{"Admin", "Guest"});
        panel.add(roleBox);

        panel.add(new JLabel("Select Action:"));
        actionBox = new JComboBox<>(new String[]{"Login", "Register"});
        panel.add(actionBox);

        panel.add(new JLabel("Username:"));
        JTextField userField = new JTextField();
        panel.add(userField);

        panel.add(new JLabel("Password:"));
        JPasswordField passField = new JPasswordField();
        panel.add(passField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        JButton confirmBtn = new JButton("Confirm");
        JButton exitBtn = new JButton("Exit");

        confirmBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String action = (String) actionBox.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String email = emailField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                JOptionPane.showMessageDialog(this, "Email must be a Gmail address.");
                return;
            }

            String filePath = role.equalsIgnoreCase("Admin") ? ADMIN_FILE : USER_FILE;

            if (action.equalsIgnoreCase("Register")) {
                boolean success = registerUser(filePath, username, password, email);
                if (success) {
                    // after successful registration, switch to Login mode
                    actionBox.setSelectedItem("Login");
                    JOptionPane.showMessageDialog(this, "Please now login with your new credentials.");
                    // clear fields
                    userField.setText("");
                    passField.setText("");
                    emailField.setText("");
                }
            } else {
                loginUser(filePath, username, password, email, role);
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(confirmBtn);
        panel.add(exitBtn);

        add(panel);
        setVisible(true);
    }

    /**
     * Returns true if registration succeeded, false otherwise
     */
    private boolean registerUser(String filePath, String username, String password, String email) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equalsIgnoreCase(username)) {
                        JOptionPane.showMessageDialog(this, "Username already exists.");
                        return false;
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(username + "," + password + "," + email);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Registration successful!");
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error during registration.");
            return false;
        }
    }

    private void loginUser(String filePath, String username, String password, String email, String role) {
        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "User file not found.");
            return;
        }

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if (parts[0].equalsIgnoreCase(username)
                        && parts[1].equals(password)
                        && parts[2].equalsIgnoreCase(email)) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                CurrentUser.setUsername(username);
                JOptionPane.showMessageDialog(this, "Login successful.");
                dispose();
                if (role.equalsIgnoreCase("Admin")) {
                    new AdminMainPage();
                } else {
                    new EventCategoryGUI();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username, password, or email.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error during login.");
        }
    }
}
