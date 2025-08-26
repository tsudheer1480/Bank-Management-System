package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

class Transcation extends JFrame implements ActionListener {
    JButton checkBalance,withdrawal,ministatement,fastcash,pinchange,deposit,exit;
    String pinno;
    Transcation(String pinno) {
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
        Image roundedTransparentLogo = makeRoundedTransparentImage(i5, 435, 292, 20, 0.53f);

        JLabel bgImage = new JLabel(new ImageIcon(roundedTransparentLogo));






        JLabel imageLabel = new JLabel(i3);
        imageLabel.setBounds(0,0 , 850, 850);
        add(imageLabel);


        bgImage.setBounds(143, 174, 435, 292);
        imageLabel.add(bgImage);

        deposit = new RoundedButton("Deposit", 0.8f, new Color(100, 160, 210));
        deposit.setBounds(2, 99, 150, 35);
        deposit.setFont(new Font("Arial", Font.BOLD, 13));
        deposit.setBackground(Color.lightGray);
        deposit.setForeground(Color.black);
        deposit.addActionListener(this);
        deposit.setCursor(new Cursor(Cursor.HAND_CURSOR ));
        bgImage.add(deposit);

        fastcash = new RoundedButton("Fast Cash", 0.8f, new Color(100, 160, 210)  );
        fastcash.setBounds(2, 149, 150, 35);
        fastcash.setFont(new Font("Arial", Font.BOLD, 13));
        fastcash.setForeground(Color.black);
        fastcash.addActionListener(this);
        fastcash.setCursor(new Cursor(Cursor.HAND_CURSOR ));
        bgImage.add(fastcash);



        pinchange = new RoundedButton("Pin Change", 0.8f, new Color(100, 160, 210)  );
        pinchange.setBounds(2, 199, 150, 35);
        pinchange.setFont(new Font("Arial", Font.BOLD, 13));
        pinchange.setForeground(Color.black);
        pinchange.addActionListener(this);
        pinchange.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(pinchange);

        withdrawal = new RoundedButton("Withdrawal", 0.8f, new Color(100, 160, 210));
        withdrawal.setBounds(283, 99, 150, 35);
        withdrawal.setFont(new Font("Arial", Font.BOLD, 13));
        withdrawal.setForeground(Color.black);
        withdrawal.addActionListener(this);
        withdrawal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(withdrawal);

        ministatement = new RoundedButton("Mini Statement", 0.8f, new Color(100, 160, 210)  );
        ministatement.setBounds(283, 149, 150, 35);
        ministatement.setFont(new Font("Arial", Font.BOLD, 13));
        ministatement.setForeground(Color.black);
        ministatement.addActionListener(this);
        ministatement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(ministatement);

        checkBalance = new RoundedButton("Check Balance", 0.8f, new Color(100, 160, 210)  );
        checkBalance.setBounds(283, 199, 150, 35);
        checkBalance.setFont(new Font("Arial", Font.BOLD, 13));
        checkBalance.setForeground(Color.black);
        checkBalance.addActionListener(this);
        checkBalance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bgImage.add(checkBalance);

        exit = new RoundedButton("Exit", 0.8f, new Color(100, 160, 210)  );
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
        System.out.println(pinno);
        if(e.getSource() == exit){
            System.exit(0);
        }
        else if(e.getSource() == deposit){
            System.out.println(pinno);
                setVisible(false);
                new Deposit(pinno).setVisible(true);
        }
        else if(e.getSource() == withdrawal){
            System.out.println(pinno);
                setVisible(false);
                new Withdrawal(pinno).setVisible(true);
        }
        else if(e.getSource() == ministatement){

        }
        else if(e.getSource() == fastcash){
            System.out.println(pinno);
                setVisible(false);
                new FastCash(pinno).setVisible(true);
        }
        else if(e.getSource() == checkBalance){

        }
        else if(e.getSource() == pinchange){

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
    static void main(String[] ignoredArgs) {
        new Transcation("1245");
    }


}
class RoundedButton extends JButton {
    private final float alpha;   // transparency
    private final Color baseColor;
    private boolean hovered = false;
    private boolean pressed = false;

    public RoundedButton(String text, float alpha, Color baseColor) {
        super(text);
        this.alpha = alpha;
        this.baseColor = baseColor;

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);

        // Detect hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background with transparency
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));

        Color finalColor = baseColor;

        if (pressed) {
            // Darker when pressed
            finalColor = baseColor.darker();
        } else if (hovered) {
            // Slightly brighter on hover
            finalColor = baseColor.brighter();
        }

        g2.setColor(finalColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        g2.dispose();

        // Draw the button text
        super.paintComponent(g);
    }

}