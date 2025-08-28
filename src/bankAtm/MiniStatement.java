package bankAtm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MiniStatement extends JFrame {
    public MiniStatement(String pinno) {
        setTitle("Mini Statement");
        setSize(600, 600);// window size
        getContentPane().setBackground(new Color(255, 255, 255));
        setLocationRelativeTo(null);    // ✅ center on screen
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // ✅ closes only this window, not entire app
        String query = "SELECT Date, type, Amount, balance FROM transactions WHERE pin = ? ORDER BY date DESC LIMIT 15";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, pinno);
            ResultSet rs = pst.executeQuery();

            String[] columnNames = {"Date", "Type", "Amount", "Balance"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] row = {
                        rs.getString("date"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getInt("balance")
                };
                model.addRow(row);
            }

            JTable table = new JTable(model) {


                @Override
                public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);

                    String type = getValueAt(row, 1).toString(); // "Deposit" or "Withdrawal"

                    if (type.equalsIgnoreCase("Deposit")) {
                        c.setBackground(new Color(230, 255, 230));
                        c.setFont(new Font("raleway", Font.PLAIN, 14));// light green for entire row
                        c.setForeground(Color.BLACK);
                    } else if (type.equalsIgnoreCase("Withdrawal")) {
                        c.setBackground(new Color(255, 230, 230)); // light red for entire row
                        c.setFont(new Font("raleway", Font.PLAIN, 14));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE); // default background
                        c.setFont(new Font("raleway", Font.PLAIN, 14));
                        c.setForeground(Color.BLACK);
                    }


                    return c;
                }
            };
            table.setFont(new Font("Arial", Font.PLAIN, 16));
            table.setRowHeight(30); // each row 30px tall
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));


            add(new JScrollPane(table)); // ✅ Add table to JFrame

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}