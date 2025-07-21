// File: src/Program/BookingHistoryPage.java
package Program;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BookingHistoryPage extends JFrame {
    private static final String PAYMENT_PATH = "C:/Degree y2/OO/AssignmentV2/Database/Payment/";  // same folder you save to :contentReference[oaicite:0]{index=0}&#8203;:contentReference[oaicite:1]{index=1}
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list = new JList<>(model);
    private List<String> records = new ArrayList<>();  // store raw lines keyed by category

    public BookingHistoryPage() {
        setTitle("My Bookings - " + CurrentUser.getUsername());  // getUsername() from CurrentUser.java :contentReference[oaicite:2]{index=2}&#8203;:contentReference[oaicite:3]{index=3}
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadBookings();

        JScrollPane scroll = new JScrollPane(list);
        JButton cancelBtn = new JButton("Cancel Booking");
        JButton backBtn   = new JButton("Back");

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cancelBooking(); }
        });
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EventCategoryGUI();  // back to category chooser :contentReference[oaicite:4]{index=4}&#8203;:contentReference[oaicite:5]{index=5}
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(cancelBtn);
        bottom.add(backBtn);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadBookings() {
        model.clear();
        records.clear();
        String user = CurrentUser.getUsername();
        String[] cats = {"Technology","Finance","Health"};
        for (String cat : cats) {
            File f = new File(PAYMENT_PATH + cat + ".txt");
            if (!f.exists()) continue;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] p = line.split(",");
                    // p[0]=username, p[1]=eventName, p[2]=category, p[3]=date, p[4]=time, p[5]=speaker, p[6]=fee
                    if (p.length >= 7 && p[0].equals(user)) {
                        records.add(cat + "|" + line);
                        // show "EventName (Category) on Date at Time"
                        model.addElement(
                            p[1] + " (" + cat + ") on " + p[3] + " at " + p[4]
                        );
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to load bookings:\n" + ex.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void cancelBooking() {
        int idx = list.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.",
                                          "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Cancel this booking?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        String rec = records.get(idx);
        String[] parts = rec.split("\\|", 2);
        String cat = parts[0], fullLine = parts[1];

        File f = new File(PAYMENT_PATH + cat + ".txt");
        File temp = new File(PAYMENT_PATH + "temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(f));
             BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(fullLine)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to cancel booking:\n" + ex.getMessage(),
                "I/O Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        f.delete();
        temp.renameTo(f);
        JOptionPane.showMessageDialog(this, "Booking canceled.");
        loadBookings();
    }
}
