package bankAtm;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Withdrawal extends JFrame implements ActionListener {
    String cardno;
    JButton withdrawalBtn, backBtn;
    JTextField amountField;

    public Withdrawal(String cardno) {
        this.cardno = cardno;
        setSize(845, 850);
        setLocation(375, 5);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Load and scale ATM image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.png"));
        Image i2 = Login.getScaledImage(i1.getImage(), 850, 850);
        ImageIcon i3 = new ImageIcon(i2);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/SBI-LOGO-HISTORY.jpg"));
        Image i5 = Login.getScaledImage(i4.getImage(), 435, 292);
        Image roundedTransparentLogo = Transcation.makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 850, 850);
        add(imageLabel);

        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        JLabel t1 = new JLabel("Enter Amount to Withdraw");
        t1.setFont(new Font("Times New Roman", Font.BOLD, 18));
        t1.setForeground(Color.white);
        t1.setBounds(110, 30, 250, 30);
        bgImage.add(t1);

        Border border = BorderFactory.createLineBorder(new Color(12, 12, 12), 2);

        amountField = new JTextField();
        amountField.setBounds(110, 60, 200, 40);
        amountField.setBackground(new Color(139, 147, 151));
        amountField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        amountField.setForeground(Color.white);
        amountField.setOpaque(false);
        amountField.setBorder(border);
        amountField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        bgImage.add(amountField);

        withdrawalBtn = new RoundedButton("Withdraw", 0.8f, new Color(100, 160, 210));
        withdrawalBtn.setBounds(283, 149, 150, 35);
        withdrawalBtn.setFont(new Font("Arial", Font.BOLD, 13));
        withdrawalBtn.setForeground(Color.black);
        withdrawalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        withdrawalBtn.addActionListener(this);
        bgImage.add(withdrawalBtn);

        backBtn = new RoundedButton("Back", 0.8f, new Color(100, 160, 210));
        backBtn.setBounds(343, 195, 90, 35);
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setForeground(Color.black);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(this);
        bgImage.add(backBtn);

        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == withdrawalBtn) {
            handleWithdrawal();
        } else if (e.getSource() == backBtn) {
            setVisible(false);
            new Transcation(cardno).setVisible(true);
        }
    }

    private void handleWithdrawal() {
        String cashInput = amountField.getText().trim();
        if (cashInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an amount before proceeding.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(cashInput);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount! Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter a positive amount.");
            return;
        }
        if (amount % 100 != 0) {
            JOptionPane.showMessageDialog(null, "Please enter amount in multiples of 100!");
            return;
        }

        // PIN confirmation
        String enteredPin = JOptionPane.showInputDialog(null, "Enter your PIN to confirm:");
        if (enteredPin == null || enteredPin.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "PIN cannot be empty.");
            return;
        }

        try (Connection conn = Conn.getConnection()) {
            // ✅ Verify PIN using BCrypt
            String pinQuery = "SELECT pin FROM login WHERE cardno = ?";
            try (PreparedStatement pst = conn.prepareStatement(pinQuery)) {
                pst.setString(1, cardno);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    String storedHash = rs.getString("pin");
                    if (!BCrypt.checkpw(enteredPin, storedHash)) {
                        JOptionPane.showMessageDialog(null, "Incorrect PIN.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Card number not found.");
                    return;
                }
            }

            // ✅ Check balance
            int balance = Deposit.getBalance(0, conn.prepareStatement(STR."SELECT * FROM transactions WHERE cardno = '\{cardno}'"));
            if (amount > balance) {
                JOptionPane.showMessageDialog(null, STR."""
Insufficient Funds!
Available Balance: ₹\{balance}""");
                return;
            }

            // ✅ Perform withdrawal
            int newBalance = balance - amount;
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd E yyyy HH:mm:ss");
            String date = sdf.format(date1);

            String query2 = "INSERT INTO transactions (cardno, Date, type, amount, balance) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst2 = conn.prepareStatement(query2)) {
                pst2.setString(1, cardno);
                pst2.setString(2, date);
                pst2.setString(3, "Withdrawal");
                pst2.setInt(4, amount);
                pst2.setInt(5, newBalance);
                pst2.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Withdrawal Successful!");

            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to check your balance?",
                    "Balance Check",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, STR."Your Balance is: ₹\{newBalance}");
            }

            setVisible(false);
            new Transcation(cardno).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, STR."Error: \{ex.getMessage()}");
        }
    }
}
