package bankAtm;

import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bankAtm.Transcation.makeRoundedTransparentImage;

class FastCash extends JFrame implements ActionListener {
    JButton _10000, _500, _2000, _1000, _5000, _100, exit;
    String cardno;

    FastCash(String cardno) {
        this.cardno = cardno;
        setSize(845, 850);
        setLocation(375, 5);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background ATM setup
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.png"));
        Image i2 = Login.getScaledImage(i1.getImage(), 850, 850);
        ImageIcon i3 = new ImageIcon(i2);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/SBI-LOGO-HISTORY.jpg"));
        Image i5 = Login.getScaledImage(i4.getImage(), 435, 292);
        Image roundedTransparentLogo = makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 850, 850);
        add(imageLabel);
        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        // Buttons setup
        _100 = createButton("Rs. 100", 2, 99, bgImage);
        _500 = createButton("Rs. 500", 283, 99, bgImage);
        _1000 = createButton("Rs. 1,000", 2, 149, bgImage);
        _2000 = createButton("Rs. 2,000", 283, 149, bgImage);
        _5000 = createButton("Rs. 5,000", 2, 199, bgImage);
        _10000 = createButton("Rs. 10,000", 283, 199, bgImage);

        exit = new RoundedButton("Back", 0.8f, new Color(100, 160, 210));
        exit.setBounds(343, 249, 90, 35);
        exit.setFont(new Font("Arial", Font.BOLD, 13));
        exit.setForeground(Color.black);
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.addActionListener(this);
        bgImage.add(exit);

        setUndecorated(true);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, JLabel bg) {
        JButton btn = new RoundedButton(text, 0.8f, new Color(100, 160, 210));
        btn.setBounds(x, y, 150, 35);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.black);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        bg.add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int amount = 0;

        if (e.getSource() == exit) {
            setVisible(false);
            new Transcation(cardno).setVisible(true);
            return;
        } else if (e.getSource() == _100) amount = 100;
        else if (e.getSource() == _500) amount = 500;
        else if (e.getSource() == _1000) amount = 1000;
        else if (e.getSource() == _2000) amount = 2000;
        else if (e.getSource() == _5000) amount = 5000;
        else if (e.getSource() == _10000) amount = 10000;

        handleFastCash(amount);
    }

    private void handleFastCash(int amount) {
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Please select a valid amount.");
            return;
        }

        String enteredPin = JOptionPane.showInputDialog(null, "Enter PIN to confirm:");
        if (enteredPin == null || enteredPin.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "PIN cannot be empty!");
            return;
        }

        try (Connection conn = Conn.getConnection()) {
            // ✅ Verify PIN using BCrypt
            String queryPin = "SELECT pin FROM login WHERE cardno = ?";
            try (PreparedStatement pst = conn.prepareStatement(queryPin)) {
                pst.setString(1, cardno);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String storedHash = rs.getString("pin");
                    if (!BCrypt.checkpw(enteredPin, storedHash)) {
                        JOptionPane.showMessageDialog(null, "Incorrect PIN! Transaction cancelled.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Card not found.");
                    return;
                }
            }

            // ✅ Balance check
            String query1 = "SELECT * FROM transactions WHERE cardno = ?";
            int balance = 0;
            try (PreparedStatement pst = conn.prepareStatement(query1)) {
                pst.setString(1, cardno);
                balance = Deposit.getBalance(balance, pst);
            }

            if (balance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient Funds!\nBalance: ₹" + balance);
                return;
            }

            // ✅ Record transaction
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd E yyyy HH:mm:ss");
            String date = sdf.format(date1);

            String query2 = "INSERT INTO transactions (cardno, Date, type, Amount, balance) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query2)) {
                ps.setString(1, cardno);
                ps.setString(2, date);
                ps.setString(3, "Withdrawal");
                ps.setInt(4, amount);
                ps.setInt(5, balance - amount);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Withdrawal Successful!");
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to check your balance?",
                    "Balance Check",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Your Balance is: ₹" + (balance - amount));
            }

            setVisible(false);
            new Transcation(cardno).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
