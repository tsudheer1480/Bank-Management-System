package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

class FastCash extends JFrame implements ActionListener {
    JButton _10000, _500, _2000, _1000, _5000, _100, exit;
    String pinno;

    FastCash(String pinno) {
        this.pinno = pinno;
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
        Image roundedTransparentLogo = makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));


        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0, 0, 850, 850);
        add(imageLabel);


        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        _100 = new RoundedButton("Rs. 100", 0.8f, new Color(100, 160, 210));
        _100.setBounds(2, 99, 150, 35);
        _100.setFont(new Font("Arial", Font.BOLD, 13));
        _100.setBackground(Color.lightGray);
        _100.setForeground(Color.black);
        _100.addActionListener(this);
        _100.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_100);

        _1000 = new RoundedButton("Rs. 1,000", 0.8f, new Color(100, 160, 210));
        _1000.setBounds(2, 149, 150, 35);
        _1000.setFont(new Font("Arial", Font.BOLD, 13));
        _1000.setForeground(Color.black);
        _1000.addActionListener(this);
        _1000.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_1000);


        _5000 = new RoundedButton("Rs. 5,000", 0.8f, new Color(100, 160, 210));
        _5000.setBounds(2, 199, 150, 35);
        _5000.setFont(new Font("Arial", Font.BOLD, 13));
        _5000.setForeground(Color.black);
        _5000.addActionListener(this);
        _5000.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_5000);

        _500 = new RoundedButton("Rs. 500", 0.8f, new Color(100, 160, 210));
        _500.setBounds(283, 99, 150, 35);
        _500.setFont(new Font("Arial", Font.BOLD, 13));
        _500.setForeground(Color.black);
        _500.addActionListener(this);
        _500.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_500);

        _2000 = new RoundedButton("Rs. 2,000", 0.8f, new Color(100, 160, 210));
        _2000.setBounds(283, 149, 150, 35);
        _2000.setFont(new Font("Arial", Font.BOLD, 13));
        _2000.setForeground(Color.black);
        _2000.addActionListener(this);
        _2000.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_2000);

        _10000 = new RoundedButton("Rs. 10,000", 0.8f, new Color(100, 160, 210));
        _10000.setBounds(283, 199, 150, 35);
        _10000.setFont(new Font("Arial", Font.BOLD, 13));
        _10000.setForeground(Color.black);
        _10000.addActionListener(this);
        _10000.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(_10000);

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

    public void actionPerformed(ActionEvent e) {
        int amount = 0;
        if (e.getSource() == exit) {
            setVisible(false);
            new Transcation(pinno).setVisible(true);
            return;
        } else if (e.getSource() == _100) {
            amount = 100;
        } else if (e.getSource() == _500) {
            amount = 500;
        } else if (e.getSource() == _2000) {
            amount = 2000;
        } else if (e.getSource() == _1000) {
            amount = 1000;
        } else if (e.getSource() == _10000) {
            amount = 10000;
        } else if (e.getSource() == _5000) {
            amount = 5000;
        }

            Date date = new Date();
            String enteredPin = JOptionPane.showInputDialog(null, "Enter PIN to confirm:");
            if (enteredPin == null || !enteredPin.equals(pinno)) {
                JOptionPane.showMessageDialog(null, "Invalid PIN! Operation cancelled.");
                return;
            }
            try {
                Conn c = new Conn();
                int balance=0;
                String Query1 ="SELECT * From transcations where pin='"+pinno+"'";
                c.st.executeQuery(Query1);

                ResultSet rs = c.st.getResultSet();
                while (rs.next()) {

                        if(rs.getString("type").equals("Deposit")){
                            balance+=rs.getInt("amount");
                        }
                        else if(rs.getString("type").equals("Withdrawal")){
                            balance-=rs.getInt("amount");
                        }
                    }
                if(balance <= 0){
                    JOptionPane.showMessageDialog(null, "Insufficient Funds!\n"+"Balance: "+balance);

                }
                else {

                    String query = "INSERT INTO transcations (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = c.c.prepareStatement(query);
                    ps.setString(1, pinno);
                    ps.setString(2, date.toString());  // or use SQL Date if your column is DATE type
                    ps.setString(3, "Withdrawal");
                    ps.setString(4, String.valueOf(amount));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "withdrawal Successfully");
                    JOptionPane.showMessageDialog(null, "Insufficient Funds!\n"+"Balance: "+balance);
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

    }


    public static Image makeRoundedTransparentImage(Image image, int width, int height, int cornerRadius, float alpha) {
        // alpha = transparency (1.0 = fully visible, 0.0 = fully transparent)
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha)); // 👈 transparency applied
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Clip with rounded rectangle
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();

        return output;
    }
    static void main (String [] args) {
        new FastCash("1245");
    }
}





