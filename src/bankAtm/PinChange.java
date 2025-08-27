package bankAtm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class PinChange extends JFrame implements ActionListener {
    String oldpinno;
    JButton change,back;
    JPasswordField pin;
    JPasswordField repin;

    public PinChange(String oldpinno){
        this.oldpinno = oldpinno;
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

        JLabel t1 = new JLabel("Pin:");
        t1.setFont(new Font("Times New Roman", Font.BOLD, 16));
        t1.setForeground(Color.white);
        t1.setBounds(30, 60, 113, 30);
        bgImage.add(t1);

        Border border = BorderFactory.createLineBorder(new Color(12, 12, 12, 255), 2);

        pin = new TransparentPasswordField(0,0.4f);
        pin.setBounds(165, 60, 200, 30);
        pin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pin.setForeground(Color.white);
        pin.setBackground(new Color(12, 12, 12));
        pin.setBorder(border);
        pin.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        pin.setOpaque(false);
        bgImage.add(pin);



        JLabel t2 = new JLabel("Re-Enter New Pin:");
        t2.setFont(new Font("Times New Roman", Font.BOLD, 16));
        t2.setForeground(Color.white);
        t2.setBounds(30, 100, 130, 30);
        bgImage.add(t2);

        repin = new TransparentPasswordField(0,0.4f);
        repin.setBounds(165, 100, 200, 30);
        repin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        repin.setForeground(Color.white);
        repin.setBackground(new Color(12, 12, 12));
        repin.setBorder(border);
        repin.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        repin.setOpaque(false);
        bgImage.add(repin);

        change = new RoundedButton("Change Pin", 0.8f, new Color(100, 160, 210)  );
        change.setBounds(283, 149, 150, 35);
        change.setFont(new Font("Arial", Font.BOLD, 13));
        change.setForeground(Color.black);
        change.setCursor(new Cursor(Cursor.HAND_CURSOR));
        change.addActionListener(this);
        bgImage.add(change);

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
       if (e.getSource() == change){
             String pin1 =pin.getText();
             String newpin = repin.getText();
             if(!pin1.equals(newpin)){
                 JOptionPane.showMessageDialog(null,"Pin Does Not Match");
                 return;
             }
             if(pin1.isEmpty()){
                 JOptionPane.showMessageDialog(null,"Pin is Empty");
             }
             else if(! (newpin.length()== 6)){
                 JOptionPane.showMessageDialog(null,"Pins Length Should be 6");
             }
             else {
                 String enteredPin = JOptionPane.showInputDialog(null, "Enter OLD PIN to confirm:");
                 if (enteredPin == null || !enteredPin.equals(oldpinno)) {
                     JOptionPane.showMessageDialog(null, "Invalid OLD PIN! Operation cancelled.");
                     return;
                 }
                 try{
                     Conn  c = new Conn();
                     String query1 = "UPDATE signupthree SET pin = ? WHERE pin = ?";
                     PreparedStatement pst = c.c.prepareStatement(query1);
                     pst.setString(1, newpin);
                     pst.setString(2, oldpinno);
                     int rows1 = pst.executeUpdate();

                     String query2 = "UPDATE login SET pin = ? WHERE pin = ?";
                     PreparedStatement pst2 = c.c.prepareStatement(query2);
                     pst2.setString(1, newpin);
                     pst2.setString(2, oldpinno);
                     int rows2 = pst2.executeUpdate();

                     String query3= "UPDATE transactions SET pin = ? WHERE pin = ?";
                     PreparedStatement pst3 =c.c.prepareStatement(query3);
                     pst3.setString(1, newpin);
                     pst3.setString(2, oldpinno);
                     int rows3 = pst3.executeUpdate();

                     if(rows1>0 && rows2>0 && rows3>0){
                         JOptionPane.showMessageDialog(null,"Pin Change  Successfully");
                         oldpinno=newpin;
                         setVisible(false);
                         new Transcation(oldpinno).setVisible(true);

                     }
                     else{
                         JOptionPane.showMessageDialog(null,"Pin Change Failed");

                     }
                 } catch (Exception ex) {
                     throw new RuntimeException(ex);                 }
             }

         }
       else if (e.getSource() == back) {
            setVisible(false);
            new Transcation(oldpinno).setVisible(true);
        }


    }

}


class TransparentPasswordField extends JPasswordField {
    private final float opacity; // 0.0 = fully transparent, 1.0 = fully opaque

    public TransparentPasswordField(int columns, float opacity) {
        super(columns);
        this.opacity = opacity;
        setOpaque(false); // Important, so we control painting
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
