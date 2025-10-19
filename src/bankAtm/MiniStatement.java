package bankAtm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MiniStatement extends JFrame {

    public MiniStatement(String cardno) {
        setTitle("Mini Statement");
        setSize(600, 600);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Table model setup
        String[] columnNames = {"Date", "Type", "Amount", "Balance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        // Improved query (consistent case)
        String query = "SELECT date, type, amount, balance FROM transactions WHERE cardno = ? ORDER BY date DESC LIMIT 15";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, cardno);
            try (ResultSet rs = pst.executeQuery()) {

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    Object[] row = {
                            rs.getString("date"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getInt("balance")
                    };
                    model.addRow(row);
                }

                if (!hasData) {
                    JOptionPane.showMessageDialog(this, "No transactions found for this account.",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // For debugging — remove in production
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Improve table visuals
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        // Row coloring based on transaction type
        Font rowFont = new Font("Raleway", Font.PLAIN, 14);
        Color depositColor = new Color(189, 250, 189);
        Color withdrawColor = new Color(241, 177, 177);
        Color defaultColor = Color.WHITE;

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String type = getValueAt(row, 1).toString();

                if ("Deposit".equalsIgnoreCase(type)) {
                    c.setBackground(depositColor);
                } else if ("Withdrawal".equalsIgnoreCase(type)) {
                    c.setBackground(withdrawColor);
                } else {
                    c.setBackground(defaultColor);
                }

                c.setFont(rowFont);
                c.setForeground(Color.BLACK);
                return c;
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Add Close button at bottom
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
