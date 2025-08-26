package bankAtm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Date;

public class Withdrawal extends JFrame implements ActionListener {
    String pinno ;
    JButton witdrawal,back;
    JTextField amount;

    public Withdrawal(String pinno){
        this.pinno=pinno;
        setSize(845, 850);
        setLocation(375, 5);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // for proper exit
        setLayout(null);

        // Load and scale image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.png"));
        Image i2 = Login.getScaledImage(i1.getImage(), 850, 850);
        ImageIcon i3 = new ImageIcon(i2);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/SBI-LOGO-HISTORY.jpg"));
        Image i5 = Login.getScaledImage(i4.getImage(), 435, 292);
        Image roundedTransparentLogo = Transcation.makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0,0 , 850, 850);

        add(imageLabel);

        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        JLabel t1 = new JLabel("Enter Amount to Witdraw");
        t1.setFont(new Font("Times New Roman", Font.BOLD, 18));
        t1.setForeground(Color.white);
        t1.setBounds(110, 30, 250, 30);
        bgImage.add(t1);

        Border border = BorderFactory.createLineBorder(new Color(12, 12, 12), 2);


        amount  = new JTextField();
        amount.setBounds(110, 60, 200, 40);
        amount.setBackground(new Color(139, 147, 151));
        amount.setFont(new Font("Times New Roman", Font.BOLD, 18));
        amount.setForeground(Color.white);
        amount.setOpaque(false);
        amount.setBorder(border);
        amount.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        bgImage.add(amount);

        witdrawal = new RoundedButton("Withdraw", 0.8f, new Color(100, 160, 210)  );
        witdrawal.setBounds(283, 149, 150, 35);
        witdrawal.setFont(new Font("Arial", Font.BOLD, 13));
        witdrawal.setForeground(Color.black);
        witdrawal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        witdrawal.addActionListener(this);
        bgImage.add(witdrawal);

        back = new RoundedButton("Back", 0.8f, new Color(100, 160, 210)  );
        back.setBounds(343, 195, 90, 35);
        back.setFont(new Font("Arial", Font.BOLD, 13));
        back.setForeground(Color.black);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        bgImage.add(back);



        setUndecorated(true);
        setVisible(true);
        setLayout(null);

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == witdrawal) {
            Conn c = new Conn();
            String cash = amount.getText();
            Date date = new Date();
            if(cash.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter an amount before proceeding.");
                return;
            }
            int cashi = Integer.parseInt(cash);
            if (cashi <= 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Amount to withdraw");
            } else if ((cashi % 100) != 0) {
                JOptionPane.showMessageDialog(null, "Please Enter Amount in in multiples of 100! to witdraw");
            } else {
                // Ask for PIN confirmation
                String enteredPin = JOptionPane.showInputDialog(null, "Enter PIN to confirm:");
                if (enteredPin == null || !enteredPin.equals(pinno)) {
                    JOptionPane.showMessageDialog(null, "Invalid PIN! Operation cancelled.");
                    return;
                }
                try {

                    String query = "INSERT INTO transcations (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = c.c.prepareStatement(query);
                    ps.setString(1, pinno);
                    ps.setString(2, date.toString());  // or use SQL Date if your column is DATE type
                    ps.setString(3, "Withdrawal");
                    ps.setString(4, cash);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "witdrawal Successfully");

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
        else if (e.getSource() == back) {
            setVisible(false);
            new Transcation(pinno).setVisible(true);
        }

    }

    static void main (String [] ignoredArgs){
        new Withdrawal("123");
    }
}

