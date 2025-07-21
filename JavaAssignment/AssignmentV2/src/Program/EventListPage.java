// File: src/Program/EventListPage.java
package Program;

import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EventListPage extends JFrame {
    private static final String BASE_PATH = "C:/Degree y2/OO/AssignmentV2/Database/Event/";
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private final String category;

    public EventListPage(String category) {
        this.category = category;
        setTitle(category + " Events");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        File f = new File(BASE_PATH + category + ".txt");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(
                this,
                "Cannot find event file:\n" + f.getAbsolutePath(),
                "File Not Found",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        loadEvents();

        JScrollPane scroll = new JScrollPane(list);
        JButton viewBtn = new JButton("View / Book");
        JButton backBtn = new JButton("Back");

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showDetails(); }
        });
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EventCategoryGUI();
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(viewBtn);
        bottom.add(backBtn);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadEvents() {
        try (BufferedReader r = new BufferedReader(new FileReader(BASE_PATH + category + ".txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) model.addElement(parts[0]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to load events:\n" + e.getMessage(),
                "I/O Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showDetails() {
        int idx = list.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event.");
            return;
        }

        String name = model.get(idx);
        try (BufferedReader r = new BufferedReader(new FileReader(BASE_PATH + category + ".txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 6 && p[0].equals(name)) {
                    String info = String.format(
                        "Event Name: %s%nCategory:   %s%nDate:       %s%nTime:       %s%nSpeaker:    %s%nFee: RM%s",
                        p[0], p[1], p[2], p[3], p[4], p[5]
                    );
                    JOptionPane.showMessageDialog(
                        this, info, "Event Information", JOptionPane.INFORMATION_MESSAGE
                    );

                    int res = JOptionPane.showConfirmDialog(
                        this, "Book this event?", "Confirm Booking", JOptionPane.YES_NO_OPTION
                    );
                    if (res == JOptionPane.YES_OPTION) {
                        dispose();
                        new PaymentPage(category, p);
                    }
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to load details:\n" + e.getMessage(),
                "I/O Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
