package bankAtm;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class PinChange extends JFrame implements ActionListener {
    String cardno;
    JButton change, back;
    JPasswordField pin, repin;

    public PinChange(String cardno) {
        this.cardno = cardno;
        setSize(845, 850);
        setLocation(375, 5);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Load and scale background images
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.png"));
        Image i2 = Login.getScaledImage(i1.getImage(), 850, 850);
        ImageIcon i3 = new ImageIcon(i2);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/SBI-LOGO-HISTORY.jpg"));
        Image i5 = Login.getScaledImage(i4.getImage(), 435, 292);
        Image roundedTransparentLogo = Transcation.makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 850, 850);
        add(imageLabel);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));
        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        JLabel t1 = new JLabel("New Pin:");
        t1.setFont(new Font("Times New Roman", Font.BOLD, 16));
        t1.setForeground(Color.white);
        t1.setBounds(30, 60, 113, 30);
        bgImage.add(t1);

        Border border = BorderFactory.createLineBorder(new Color(12, 12, 12, 255), 2);

        pin = new TransparentPasswordField(0, 0.4f);
        pin.setBounds(165, 60, 200, 30);
        pin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pin.setForeground(Color.white);
        pin.setBackground(new Color(12, 12, 12));
        pin.setBorder(border);
        bgImage.add(pin);

        JLabel t2 = new JLabel("Re-Enter Pin:");
        t2.setFont(new Font("Times New Roman", Font.BOLD, 16));
        t2.setForeground(Color.white);
        t2.setBounds(30, 100, 130, 30);
        bgImage.add(t2);

        repin = new TransparentPasswordField(0, 0.4f);
        repin.setBounds(165, 100, 200, 30);
        repin.setFont(new Font("Times New Roman", Font.BOLD, 16));
        repin.setForeground(Color.white);
        repin.setBackground(new Color(12, 12, 12));
        repin.setBorder(border);
        bgImage.add(repin);

        change = new RoundedButton("Change Pin", 0.8f, new Color(100, 160, 210));
        change.setBounds(283, 149, 150, 35);
        change.setFont(new Font("Arial", Font.BOLD, 13));
        change.addActionListener(this);
        bgImage.add(change);

        back = new RoundedButton("Back", 0.8f, new Color(100, 160, 210));
        back.setBounds(343, 195, 90, 35);
        back.setFont(new Font("Arial", Font.BOLD, 13));
        back.addActionListener(this);
        bgImage.add(back);

        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == change) {
            String newPin = new String(pin.getPassword());
            String rePin = new String(repin.getPassword());

            if (newPin.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pin cannot be empty");
                return;
            }

            if (!newPin.equals(rePin)) {
                JOptionPane.showMessageDialog(null, "Pins do not match!");
                return;
            }

            if (newPin.length() != 6) {
                JOptionPane.showMessageDialog(null, "PIN must be exactly 6 digits");
                return;
            }

            String oldPin = JOptionPane.showInputDialog(null, "Enter your OLD PIN to confirm:");
            if (oldPin == null || oldPin.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Operation cancelled.");
                return;
            }

            try (Connection conn = Conn.getConnection()) {
                // Fetch stored hash
                PreparedStatement getPinStmt = conn.prepareStatement("SELECT pin FROM login WHERE cardno = ?");
                getPinStmt.setString(1, cardno);
                ResultSet rs = getPinStmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Card not found!");
                    return;
                }

                String storedHash = rs.getString("pin");
                if (!BCrypt.checkpw(oldPin, storedHash)) {
                    JOptionPane.showMessageDialog(null, "Invalid OLD PIN!");
                    return;
                }

                // Hash the new pin
                String hashedNewPin = BCrypt.hashpw(newPin, BCrypt.gensalt());

                // Update in all necessary tables
                PreparedStatement pst1 = conn.prepareStatement("UPDATE login SET pin=? WHERE cardno=?");
                pst1.setString(1, hashedNewPin);
                pst1.setString(2, cardno);

                PreparedStatement pst2 = conn.prepareStatement("UPDATE signupthree SET pin=? WHERE cardno=?");
                pst2.setString(1, hashedNewPin);
                pst2.setString(2, cardno);

                int rows1 = pst1.executeUpdate();
                int rows2 = pst2.executeUpdate();

                if (rows1 > 0 && rows2 > 0) {
                    JOptionPane.showMessageDialog(null, "PIN changed successfully!");
                    setVisible(false);
                    new Transcation(cardno).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "PIN update failed!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            new Transcation(cardno).setVisible(true);
        }
    }
}

// Transparent password field
class TransparentPasswordField extends JPasswordField {
    private final float opacity;

    public TransparentPasswordField(int columns, float opacity) {
        super(columns);
        this.opacity = opacity;
        setOpaque(false);
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
