package Program;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AdminMainPage extends JFrame {
    public AdminMainPage() {
        setTitle("Admin Dashboard - " + CurrentUser.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton manageEventsBtn = new JButton("Manage Events");
        JButton salesReportBtn = new JButton("Check Sales Report");
        JButton logoutBtn = new JButton("Logout");

        manageEventsBtn.addActionListener(e -> {
            dispose();
            new AdminEventManagerPage();
        });

        salesReportBtn.addActionListener(e -> {
            dispose();
            new SalesReportSelectionPage();
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginAndRegister();
        });

        panel.add(manageEventsBtn);
        panel.add(salesReportBtn);
        panel.add(logoutBtn);

        add(panel);
        setVisible(true);
    }
}
