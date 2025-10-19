package bankAtm;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.*;

class SignupPhaseThree extends JFrame implements ActionListener {
        JRadioButton r1,r2,r3,r4;
        JCheckBox ch1,ch2,ch3,ch4,ch5,ch6,ch7;
        JButton submit,cancel;
        Random rand =new Random();
        String formno;

        // generate 16-digit card number
        long cardno = Math.abs(rand.nextLong() % 900000000000L) + 5348875000000000L;
        int pin = rand.nextInt(900000) + 100000;
        String pinStr = String.valueOf(pin);

    // hash the pin using bcrypt
        String hashedPin = BCrypt.hashpw(pinStr, BCrypt.gensalt(12)); // cost 12 is reasonable
        String cardStr = String.valueOf(cardno);

        // group into 4-4-4-4 format
        String formatted = cardStr.replaceAll("(.{4})(?=.{4})", "$1-");

    SignupPhaseThree(String formno) {
        this.formno = formno;
        setTitle("Signup Phase Three");
        setSize(850 ,800);
        setLayout(null);
        setVisible(true);
        setLocation(400,30);

        JLabel text = new JLabel("Page 3: Account Details");
        text.setBounds(300,50,400,40);
        text.setFont(new Font("Times New Roman",Font.BOLD,28));
        add(text);

        JLabel t1 = new JLabel("Account Number");
        t1.setBounds(150,100,200,25);
        t1.setFont(new Font("Times New Roman",Font.BOLD,25));
        add(t1);

        r1 = new JRadioButton("Saving Account");
        r1.setBounds(150,140,200,20);
        r1.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(r1);

        r2 = new JRadioButton("Fixed Deposit Account");
        r2.setBounds(150,160,200,20);
        r2.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(r2);

        r3 = new JRadioButton("Current Account");
        r3.setBounds(400,140,200,20);
        r3.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(r3);

        r4 = new JRadioButton("Deposit Account");
        r4.setBounds(400,160,200,20);
        r4.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(r4);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);
        buttonGroup.add(r3);
        buttonGroup.add(r4);

        JLabel t2 = new JLabel("Card Number:");
        t2.setBounds(150,250,200,25);
        t2.setFont(new Font("Times New Roman",Font.BOLD,25));
        add(t2);



        // mask first 10 digits, keep next 2 + last 4
        String masked = STR."XXXX-XXXX-XX\{formatted.substring(12)}";

        JLabel t3 = new JLabel(String.format(masked));

        t3.setBounds(410,250,300,20);
        t3.setFont(new Font("Times New Roman",Font.BOLD,18));
        add(t3);

        JLabel t4 = new JLabel(" Your 16 Digit Card Number");
        t4.setBounds(150,275,200,15);
        t4.setFont(new Font("Times New Roman",Font.PLAIN,12));
        add(t4);

        JLabel t5 = new JLabel("PIN: ");
        t5.setBounds(150,350,200,25);
        t5.setFont(new Font("Times New Roman",Font.BOLD,25));
        add(t5);

        JLabel t6 = new JLabel("XXXX");
        t6.setBounds(410,350,200,20);
        t6.setFont(new Font("Times New Roman",Font.BOLD,18));
        add(t6);

        JLabel t7 = new JLabel("Your 4 Digit Password");
        t7.setBounds(150,375,200,15);
        t7.setFont(new Font("Times New Roman",Font.PLAIN,12));
        add(t7);

        JLabel t8 = new JLabel("Service Required : ");
        t8.setBounds(150,475,200,25);
        t8.setFont(new Font("Times New Roman",Font.BOLD,25));
        add(t8);

        ch1 = new JCheckBox("ATM Card ");
        ch1.setBounds(150,500,200,20);
//        ch1.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(ch1);

        ch2 = new JCheckBox("Mobile Banking ");
        ch2.setBounds(150,530,200,20);
//        ch2.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(ch2);

        ch3 = new JCheckBox("Cheque Book");
        ch3.setBounds(150,560,200,20);
//        ch3.setFont(new Font("Times New Roman",Font.PLAIN,18));
        add(ch3);

        ch4 = new JCheckBox("Internet Banking");
        ch4.setBounds(420,500,200,20);
        add(ch4);

        ch5 = new JCheckBox("EMAIL & SMS Alerts");
        ch5.setBounds(420,530,200,20);
        add(ch5);

        ch6 = new JCheckBox("E-Statements");
        ch6.setBounds(420,560,200,20);
        add(ch6);

        ch7 = new JCheckBox("I herby declares thet the above entered details are correct to the best knowledge.");
        ch7.setBounds(150,600,800,20);
        ch7.setFont(new Font("Times New Roman",Font.PLAIN,14));
        ch7.setBorder(null);
        add(ch7);



        submit = new JButton("Submit");
        submit.setBounds(250,650,150,40);
        submit.setFont(new Font("Times New Roman",Font.PLAIN,25));
        submit.setForeground(Color.white);
        submit.setBackground(Color.BLACK);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(425,650,150,40);
        cancel.setFont(new Font("Times New Roman",Font.PLAIN,25));
        cancel.setForeground(Color.white);
        cancel.setBackground(Color.red );
        cancel.addActionListener(this);
        add(cancel);



        revalidate();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // for proper exit

        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==submit) {
            String acc="";
            if(r1.isSelected()){
                acc+="Saving Account";
            }
            else if (r2.isSelected()){
                acc+="Fixed Deposit Account";
                }
            else if (r3.isSelected()) {
                    acc+="Current Account";
                }
            else if (r4.isSelected()) {
                acc+="Deposit Account";
            }

            String service = getString();
            try {
                if (service.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Select services");
                } else if (!ch7.isSelected()) {
                    JOptionPane.showMessageDialog(null, "check terms and conditions");
                } else if (acc.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Select Account");
                } else {
                    String query1 = "INSERT INTO signupthree(formno, cardno, pin, account_type, service) VALUES (?, ?, ?, ?, ?)";
                    String query2 = "INSERT INTO login(formno,cardno,pin) VALUES (?,?,?)";

                    try (Connection conn = Conn.getConnection();
                         PreparedStatement ps = conn.prepareStatement(query1)) {
                        ps.setString(1, formno);
                        ps.setString(2, cardStr);
                        ps.setString(3, hashedPin);
                        ps.setString(4, acc);
                        ps.setString(5, service);
                        ps.executeUpdate();

                        PreparedStatement ps2 = conn.prepareStatement(query2);
                        ps2.setString(1, formno);
                        ps2.setString(2, cardStr);
                        ps2.setString(3, hashedPin);
                        ps2.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Account Created");
                        JOptionPane.showMessageDialog(null,
                                "Your CARD NO is:"+cardStr+
                                "\nYour temporary PIN is: " + pinStr + "\nPlease change it immediately after login.",
                                "PIN Generated",
                                JOptionPane.INFORMATION_MESSAGE);


                        setVisible(false);
                        new Login().setVisible(true);
                    }
                }
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        if(e.getSource()==cancel){
            setVisible(false);
            new Login().setVisible(true);
        }

    }

    private String getString() {
        String service= "";
        if(ch1.isSelected()){
            service+=" ATM Card";
        }
        if(ch2.isSelected()){
            service+=" Mobile Banking";
        }
        if(ch3.isSelected()){
            service+=" Cheque Book";
        }
        if(ch4.isSelected()){
            service+=" Internet Banking";
        }
        if(ch5.isSelected()){
            service+=" EMAIL & SMS Alerts";
        }
        if(ch6.isSelected()){
            service+=" E-Statements";
        }
        return service;
    }
}