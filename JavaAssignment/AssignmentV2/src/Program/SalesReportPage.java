// File: Program/SalesReportPage.java
package Program;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

public class SalesReportPage extends JFrame {
    private static final String PATH = "C:/Degree y2/OO/AssignmentV2/Database/Payment/";

    public SalesReportPage(String category) {
        setTitle(category + " Sales Report");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SalesReportSelectionPage();
            }
        });
        add(back, BorderLayout.SOUTH);

        load(category, area);
        setVisible(true);
    }

    private void load(String c, JTextArea a) {
        File f = new File(PATH + c + ".txt");
        a.append("===== " + c + " Sales =====\n\n");
        if (!f.exists()) {
            a.append("No sales record found.\n");
            return;
        }
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 7) {
                    a.append(
                        "User:    " + p[0] + "\n" +
                        "Event:   " + p[1] + "\n" +
                        "Date:    " + p[3] + "\n" +
                        "Time:    " + p[4] + "\n" +
                        "Speaker: " + p[5] + "\n" +
                        "Fee:     " + p[6] + "\n" +
                        "-----------------------\n"
                    );
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading records:\n" + e.getMessage(),
                "I/O Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
