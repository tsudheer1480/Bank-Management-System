package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BalanceEnquiry extends JFrame implements ActionListener {
    String cardno;
    JButton back;
    JLabel t1;

    BalanceEnquiry(String cardno){
        this.cardno = cardno;
        setSize(845, 850);
        setLocation(375, 5);
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

        t1 = new JLabel();
        t1.setFont(new Font("Times New Roman", Font.BOLD, 18));
        t1.setForeground(Color.white);
        t1.setBounds(110, 80, 250, 30);
        bgImage.add(t1);

        back = new RoundedButton("Back", 0.8f, new Color(100, 160, 210)  );
        back.setBounds(343, 195, 90, 35);
        back.setFont(new Font("Arial", Font.BOLD, 13));
        back.setForeground(Color.black);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        bgImage.add(back);

        String query = "SELECT * From transactions where cardno= ? ";
        int balance = 0;
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1,cardno);
               balance=Deposit.getBalance(balance, pst);
               t1.setText(STR."Your Balance is Rs.\{balance}");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }




        setUndecorated(true);
        setVisible(true);
        setLayout(null);

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            setVisible(false);
            new Transcation(cardno).setVisible(true);
        }

    }
}

