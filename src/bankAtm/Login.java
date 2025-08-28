package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    public JButton signup,clear,signin;
    JTextField card;
    JPasswordField pin;

    public Login() {
        // Title of the JFrame
        setTitle("AUTOMATED TELLER MACHINE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // for proper exit

        // Load and scale the SBI logo
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/sbi.jpg"));
        Image i2 = getScaledImage(i1.getImage(), 180, 130); // high-quality scaling
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(60, 30, 180, 130); // position and size of image

        // Welcome_label
        JLabel text1 = new JLabel("WELCOME TO SBI");
        text1.setFont(new Font("Arial", Font.BOLD, 28)); // font style
        text1.setBounds(290, 50, 400, 100); // position and size
        text1.setBorder(null);

        // "CARD NO" label
        JLabel cardtext = new JLabel("CARD NO:");
        cardtext.setFont(new Font("Arial", Font.BOLD, 20));
        cardtext.setBounds(180, 160, 100, 70);
        cardtext.setBorder(null);

        // Input field for card number
        card = new JTextField();
        card.setBounds(300, 183, 250, 30);

        // "PIN NO" label
        JLabel pintext = new JLabel("PIN NO:");
        pintext.setFont(new Font("Arial", Font.BOLD, 20));
        pintext.setBounds(180, 220, 100, 70);
        pintext.setBorder(null);

        // Input field for PIN (hidden characters)
        pin = new JPasswordField();
        pin.setBounds(300, 243, 250, 30);

        // "SIGN IN" button
        signin = new JButton("SIGN IN");
        signin.setFont(new Font("Arial", Font.BOLD, 20));
        signin.setBounds(300, 290, 120, 40);
        signin.setBorder(null);
        signin.setBackground(Color.darkGray);
        signin.setForeground(Color.white);
        signin.addActionListener(this);

        // "clear" button
        clear = new JButton("CLEAR");
        clear.setFont(new Font("Arial", Font.BOLD, 20));
        clear.setBounds(428, 290, 120, 40);
        clear.setBorder(null);
        clear.setBackground(Color.darkGray);
        clear.setForeground(Color.white);
        clear.addActionListener(this);

        // "SIGN UP" button
        signup = new JButton("SIGN UP");
        signup.setFont(new Font("Arial", Font.BOLD, 20));
        signup.setBounds(300, 340, 248, 40);
        signup.setBorder(null);
        signup.setBackground(Color.darkGray);
        signup.setForeground(Color.white);
        signup.addActionListener(this);

        // Add all components to JFrame
        add(imageLabel);
        add(text1);
        add(cardtext);
        add(pintext);
        add(card);
        add(pin);
        add(signin);
        add(clear);
        add(signup);


        // Frame settings
        setLayout(null); // absolute positioning
        setSize(800, 480); // frame size
        setLocation(375, 200); // window position on screen
        setVisible(true); // make visible
        getContentPane().setBackground(Color.white); // set background color
    }

    // Utility method: High-quality image scaling
    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        /* Enable high-quality rendering */
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw scaled image
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
//    action listener
public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signin ) {
            String cardno=card.getText();
            String pinno=pin.getText();

        String query ="select * from login where cardno= ? and pin=?";

        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)){
            pst.setString(1, cardno);
            pst.setString(2, pinno);
            ResultSet rs = pst.executeQuery();

        if(rs.next()){
            setVisible(false);
            new Transcation(pinno);
        }
        else{
            JOptionPane.showMessageDialog(null,"INVALID CARD NO OR PIN NO");
        }

        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        }
        else if (e.getSource() == clear) {
                card.setText("");
                pin.setText("");
        }
        else  if (e.getSource() == signup) {
            setVisible(false);
            new SignPhaseOne().setVisible(true);
        }
}
    public static void main(String[] ignoredArgs) {
        // Start the application
        new Login();
    }
}
