package Program;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

public class PaymentPage extends JFrame {
    private static final String PAYMENT_PATH = "C:\\Degree y2\\OO\\AssignmentV2\\Database\\Payment\\";
    private String category;
    private String[] eventDetails;

    public PaymentPage(String category, String[] eventDetails) {
        this.category = category;
        this.eventDetails = eventDetails;

        setTitle("Payment for " + eventDetails[0]);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Select Payment Method:");
        JRadioButton ewallet = new JRadioButton("E-Wallet");
        JRadioButton onlineBanking = new JRadioButton("Online Banking");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(ewallet);
        paymentGroup.add(onlineBanking);

        JButton payBtn = new JButton("Pay");
        JButton cancelBtn = new JButton("Cancel");

        payBtn.addActionListener(e -> {
            if (!ewallet.isSelected() && !onlineBanking.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please select a payment method.");
                return;
            }
            saveBooking();
            JOptionPane.showMessageDialog(this, "Payment Successful!");
            dispose();
            new EventCategoryGUI();
        });

        cancelBtn.addActionListener(e -> {
            dispose();
            new EventCategoryGUI();
        });

        setLayout(new GridLayout(6, 1));
        add(label);
        add(ewallet);
        add(onlineBanking);
        add(payBtn);
        add(cancelBtn);

        setVisible(true);
    }

    private void saveBooking() {
        String username = CurrentUser.getUsername();
        String filePath = PAYMENT_PATH + category + ".txt";

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(username + "," + String.join(",", eventDetails));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save booking.");
        }
    }
}
