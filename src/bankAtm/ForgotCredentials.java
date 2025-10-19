package bankAtm;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ForgotCredentials extends JFrame implements ActionListener {

    JButton findCardBtn, forgotpin, back, submit;
    JLabel bgImage, t1;
    JTextField emailorphone, cardField, phoneField;
    String mode = ""; // to track current action

    public ForgotCredentials() {
        setSize(845, 850);
        setLocation(375, 5);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Load and scale image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.png"));
        Image i2 = Login.getScaledImage(i1.getImage(), 850, 850);
        ImageIcon i3 = new ImageIcon(i2);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/SBI-LOGO-HISTORY.jpg"));
        Image i5 = Login.getScaledImage(i4.getImage(), 435, 292);
        Image roundedTransparentLogo = Transcation.makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 850, 850);

        add(imageLabel);
        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        findCardBtn = new RoundedButton("Find Card No", 0.8f, new Color(100, 160, 210));
        findCardBtn.setBounds(2, 99, 150, 35);
        findCardBtn.setFont(new Font("Arial", Font.BOLD, 13));
        findCardBtn.setForeground(Color.black);
        findCardBtn.addActionListener(this);
        findCardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(findCardBtn);

        forgotpin = new RoundedButton("Forgot PIN", 0.8f, new Color(100, 160, 210));
        forgotpin.setBounds(283, 99, 150, 35);
        forgotpin.setFont(new Font("Arial", Font.BOLD, 13));
        forgotpin.setForeground(Color.black);
        forgotpin.addActionListener(this);
        forgotpin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(forgotpin);

        back = new RoundedButton("Back", 0.8f, new Color(100, 160, 210));
        back.setBounds(343, 249, 90, 35);
        back.setFont(new Font("Arial", Font.BOLD, 13));
        back.setForeground(Color.black);
        back.addActionListener(this);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(back);

        // Common border style
        Border border = BorderFactory.createLineBorder(new Color(12, 12, 12), 2);

        // Common fields
        t1 = new JLabel();
        t1.setFont(new Font("Arial", Font.BOLD, 13));
        t1.setForeground(new Color(121, 125, 129));
        t1.setBounds(20, 40, 200, 35);

        emailorphone = new JTextField();
        emailorphone.setBounds(200, 90, 200, 35);
        emailorphone.setFont(new Font("Times New Roman", Font.BOLD, 18));
        emailorphone.setForeground(new Color(196, 196, 211));
        emailorphone.setBackground(new Color(67, 65, 65));
        emailorphone.setBorder(border);

        cardField = new JTextField();
        cardField.setBounds(200, 40, 200, 35);
        cardField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        cardField.setForeground(new Color(196, 196, 211));
        cardField.setBackground(new Color(67, 65, 65));
        cardField.setBorder(border);

        phoneField = new JTextField();
        phoneField.setBounds(200, 140, 200, 35);
        phoneField.setFont(new Font("Times New Roman", Font.BOLD, 18));
        phoneField.setForeground(new Color(196, 196, 211));
        phoneField.setBackground(new Color(67, 65, 65));
        phoneField.setBorder(border);

        submit = new RoundedButton("Submit", 0.8f, new Color(100, 160, 210));
        submit.setBounds(160, 200, 150, 35);
        submit.addActionListener(this);
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submit.setFont(new Font("Arial", Font.BOLD, 13));
        submit.setForeground(Color.black);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findCardBtn) {
            mode = "findCard";
            showFindCardUI();
        } else if (e.getSource() == forgotpin) {
            mode = "forgotPin";
            showForgotPinUI();
        } else if (e.getSource() == submit) {
            if (mode.equals("findCard")) findCard();
            else if (mode.equals("forgotPin")) resetPin();
        } else if (e.getSource() == back) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }

    // ===== UI for Find Card =====
    private void showFindCardUI() {
        bgImage.removeAll();

        bgImage.add(back);

        t1.setText("Email or Phone:");
        bgImage.add(t1);
        bgImage.add(emailorphone);
        bgImage.add(submit);

        bgImage.revalidate();
        bgImage.repaint();
    }

    // ===== UI for Forgot PIN =====
    private void showForgotPinUI() {
        bgImage.removeAll();

        bgImage.add(back);

        JLabel lblCard = new JLabel("Card Number:");
        lblCard.setBounds(20, 40, 150, 35);
        lblCard.setFont(new Font("Arial", Font.BOLD, 13));
        lblCard.setForeground(new Color(121, 125, 129));
        bgImage.add(lblCard);
        bgImage.add(cardField);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 90, 150, 35);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 13));
        lblEmail.setForeground(new Color(121, 125, 129));
        bgImage.add(lblEmail);
        bgImage.add(emailorphone);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(20, 140, 150, 35);
        lblPhone.setFont(new Font("Arial", Font.BOLD, 13));
        lblPhone.setForeground(new Color(121, 125, 129));
        bgImage.add(lblPhone);
        bgImage.add(phoneField);

        bgImage.add(submit);
        bgImage.revalidate();
        bgImage.repaint();
    }

    // ===== Function to Find Card No =====
    private void findCard() {
        String input = emailorphone.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Email or Phone!");
            return;
        }

        try (Connection con = Conn.getConnection()){
            String sql = "SELECT s3.cardno FROM signup s " +
                    "JOIN signupthree s3 ON s.formno = s3.formno " +
                    "WHERE s.email = ? OR s.phonenumber = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, input);
            pst.setString(2, input);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String cardNo = rs.getString("cardno");
                JOptionPane.showMessageDialog(null, STR."✅ Your Card Number is: \{cardNo}");
            } else {
                JOptionPane.showMessageDialog(null, "❌ No account found for this email/phone number.");
            }

            rs.close();
            pst.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while fetching card!");
        }
    }


    // ===== Function to Reset PIN =====
    private void resetPin() {
        String card = cardField.getText().trim();
        String email = emailorphone.getText().trim();
        String phone = phoneField.getText().trim();

        System.out.println(STR."\{card} \{email} \{phone}");

        if (card.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = Conn.getConnection()) {
            String sql = """
                SELECT s3.cardno FROM signup s
                JOIN signupthree s3 ON s.formno = s3.formno
                WHERE s3.cardno = ? AND s.email = ? AND s.phonenumber = ?
                """;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, card);
            pst.setString(2, email);
            pst.setString(3, phone);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String tempPin = JOptionPane.showInputDialog(null, "Enter your new pin");
                String hashedPin = BCrypt.hashpw(tempPin, BCrypt.gensalt());

                try (PreparedStatement update = conn.prepareStatement("UPDATE login SET pin=? WHERE cardno=?")) {
                    update.setString(1, hashedPin);
                    update.setString(2, card);
                    update.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "PIN generation successful 🎉");

            } else {
                JOptionPane.showMessageDialog(this, "No matching account found!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while resetting PIN!");
        }
    }

}
