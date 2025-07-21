package Program;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AdminEventManagerPage extends JFrame {
    private JTextField nameField, categoryField, dateField, timeField, speakerField, feeField;
    private static final String BASE_PATH = "C:\\Degree y2\\OO\\AssignmentV2\\Database\\Event\\";

    public AdminEventManagerPage() {
        setTitle("Admin - Manage Events");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2));

        nameField = new JTextField();
        categoryField = new JTextField();
        dateField = new JTextField();
        timeField = new JTextField();
        speakerField = new JTextField();
        feeField = new JTextField();

        panel.add(new JLabel("Event Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category (Technology/Finance/Health):"));
        panel.add(categoryField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (e.g. 10:00AM):"));
        panel.add(timeField);
        panel.add(new JLabel("Speaker:"));
        panel.add(speakerField);
        panel.add(new JLabel("Fee :"));
        panel.add(feeField);

        JButton addBtn = new JButton("Add Event");
        JButton deleteBtn = new JButton("Delete Event");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> addEvent());
        deleteBtn.addActionListener(e -> deleteEvent());
        backBtn.addActionListener(e -> {
            dispose();
            new AdminMainPage();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addEvent() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String speaker = speakerField.getText().trim();
        String fee = feeField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || date.isEmpty() || time.isEmpty() || speaker.isEmpty() || fee.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        if (!category.equalsIgnoreCase("Technology") && !category.equalsIgnoreCase("Finance") && !category.equalsIgnoreCase("Health")) {
            JOptionPane.showMessageDialog(this, "Invalid category. Must be Technology, Finance, or Health.");
            return;
        }

        String filename = BASE_PATH + category + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(name + "," + category + "," + date + "," + time + "," + speaker + "," + fee);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Event added successfully into " + category + ".txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to add event.");
        }
    }

    private void deleteEvent() {
        String category = JOptionPane.showInputDialog(this, "Enter the Category of Event (Technology/Finance/Health):");
        String eventName = JOptionPane.showInputDialog(this, "Enter the Event Name to Delete:");

        if (category == null || eventName == null || category.trim().isEmpty() || eventName.trim().isEmpty()) {
            return;
        }

        if (!category.equalsIgnoreCase("Technology") && !category.equalsIgnoreCase("Finance") && !category.equalsIgnoreCase("Health")) {
            JOptionPane.showMessageDialog(this, "Invalid category. Must be Technology, Finance, or Health.");
            return;
        }

        String filename = BASE_PATH + category + ".txt";
        File inputFile = new File(filename);
        File tempFile = new File(BASE_PATH + "temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(eventName + ",")) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(this, "Event deleted successfully from " + category + ".txt");
            } else {
                JOptionPane.showMessageDialog(this, "Event not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete event.");
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
}
