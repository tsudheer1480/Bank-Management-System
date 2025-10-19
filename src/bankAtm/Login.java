package bankAtm;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class Login extends JFrame implements ActionListener {

    public JButton signup, clear, signin, forgotBtn;
    JTextField card;
    JPasswordField pin;

    public Login() {
        setTitle("AUTOMATED TELLER MACHINE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // SBI Logo
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/sbi.jpg"));
        Image i2 = getScaledImage(i1.getImage(), 180, 130);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(60, 30, 180, 130);

        // Welcome text
        JLabel text1 = new JLabel("WELCOME TO SBI");
        text1.setFont(new Font("Arial", Font.BOLD, 28));
        text1.setBounds(290, 50, 400, 100);

        // Card number
        JLabel cardtext = new JLabel("CARD NO:");
        cardtext.setFont(new Font("Arial", Font.BOLD, 20));
        cardtext.setBounds(180, 160, 100, 70);

        card = new JTextField();
        card.setBounds(300, 183, 250, 30);

        // PIN
        JLabel pintext = new JLabel("PIN NO:");
        pintext.setFont(new Font("Arial", Font.BOLD, 20));
        pintext.setBounds(180, 220, 100, 70);

        pin = new JPasswordField();
        pin.setBounds(300, 243, 250, 30);

        // Buttons
        signin = new JButton("SIGN IN");
        signin.setFont(new Font("Arial", Font.BOLD, 20));
        signin.setBounds(300, 290, 120, 40);
        signin.setBackground(Color.darkGray);
        signin.setForeground(Color.white);
        signin.setBorder(null);
        signin.addActionListener(this);

        clear = new JButton("CLEAR");
        clear.setFont(new Font("Arial", Font.BOLD, 20));
        clear.setBounds(428, 290, 120, 40);
        clear.setBackground(Color.darkGray);
        clear.setForeground(Color.white);
        clear.setBorder(null);
        clear.addActionListener(this);

        signup = new JButton("SIGN UP");
        signup.setFont(new Font("Arial", Font.BOLD, 20));
        signup.setBounds(300, 340, 248, 40);
        signup.setBackground(Color.darkGray);
        signup.setForeground(Color.white);
        signup.setBorder(null);
        signup.addActionListener(this);

        forgotBtn = new JButton("Forgot PIN / Card No?");
        forgotBtn.setBounds(300, 390, 248, 20);
        forgotBtn.setBackground(new Color(196, 238, 246));
        forgotBtn.setForeground(new Color(57, 64, 66));
        forgotBtn.setBorder(null);
        forgotBtn.setFont(new Font("Arial", Font.BOLD, 12));
        forgotBtn.addActionListener(this);

        // Add components
        add(imageLabel);
        add(text1);
        add(cardtext);
        add(card);
        add(pintext);
        add(pin);
        add(signin);
        add(clear);
        add(signup);
        add(forgotBtn);

        // Frame settings
        setLayout(null);
        setSize(800, 480);
        setLocation(375, 200);
        setVisible(true);
        getContentPane().setBackground(Color.white);
    }

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signin) {
            String cardno = card.getText().trim();
            char[] pinChars = pin.getPassword();
            String enteredPin = new String(pinChars);

            String query = "SELECT pin FROM login WHERE cardno = ?";
            try (Connection conn = Conn.getConnection();
                 PreparedStatement pst = conn.prepareStatement(query)) {

                pst.setString(1, cardno);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String storedHash = rs.getString("pin");
                    if (BCrypt.checkpw(enteredPin, storedHash)) {
                        setVisible(false);
                        new Transcation(cardno).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect PIN");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "CARD Number not Found");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                Arrays.fill(pinChars, '0'); // clear sensitive data
            }

        } else if (e.getSource() == clear) {
            card.setText("");
            pin.setText("");
        } else if (e.getSource() == signup) {
            setVisible(false);
            new SignPhaseOne().setVisible(true);
        } else if (e.getSource() == forgotBtn) {
            setVisible(false);
            new ForgotCredentials().setVisible(true);
        }
    }

    public static void main() {
        new Login();
    }
}
