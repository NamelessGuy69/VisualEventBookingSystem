// File: Program/SalesReportSelectionPage.java
package Program;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SalesReportSelectionPage extends JFrame {
    public SalesReportSelectionPage() {
        setTitle("Select Sales Category");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Select Category", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(2,2,10,10));
        JButton tech = new JButton("Technology");
        JButton fin  = new JButton("Finance");
        JButton hlth = new JButton("Health");
        JButton back = new JButton("Back");

        panel.add(tech); panel.add(fin);
        panel.add(hlth); panel.add(back);
        add(panel, BorderLayout.CENTER);

        tech.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openReport("Technology"); }
        });
        fin .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openReport("Finance"); }
        });
        hlth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openReport("Health"); }
        });
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminMainPage();
            }
        });

        setVisible(true);
    }

    private void openReport(String cat) {
        try {
            new SalesReportPage(cat);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Failed to open report for “" + cat + "”:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
