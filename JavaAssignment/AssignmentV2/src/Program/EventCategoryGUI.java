// File: Program/EventCategoryGUI.java
package Program;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EventCategoryGUI extends JFrame {

    public EventCategoryGUI() {
        setTitle("Select Event Category");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Select Event Category", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton techBtn    = new JButton("Technology");
        JButton financeBtn = new JButton("Finance");
        JButton healthBtn  = new JButton("Health");

        for (JButton b : new JButton[]{techBtn, financeBtn, healthBtn}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(200, 40));
            center.add(b);
            center.add(Box.createVerticalStrut(10));
        }
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton logoutBtn = new JButton("Logout");
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);

        techBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openList("Technology"); }
        });
        financeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openList("Finance"); }
        });
        healthBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openList("Health"); }
        });
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginAndRegister();
            }
        });

        setVisible(true);
    }

    private void openList(String category) {
        try {
            new EventListPage(category);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error loading “" + category + "” events:\n" + ex.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
