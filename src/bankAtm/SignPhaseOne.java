package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;
import com.toedter.calendar.JDateChooser;


class SignPhaseOne extends JFrame implements ActionListener {
    long ran;
    JTextField nametextfield,phoneNumber,fathernametextfield,emailtextfiels,addresstextfield,citytextfield,statetextfield,pincodetextfield;
    JDateChooser datebirth;
    JRadioButton male,female;
    JRadioButton married,unmarried,other;
    JButton next;
    SignPhaseOne() {
        setTitle("Sign Phase 1 ");

        Random rand = new Random();
        ran = Math.abs((rand.nextLong()%9000L )+1000L);
        JLabel formno = new JLabel(STR."APPLICATION NO . \{ran}");
        formno.setFont(new Font("Times New Roman", Font.BOLD, 28));
        formno.setBounds(220, 20, 500, 40);

        JLabel text1 = new JLabel("Page 1: personal details");
        text1.setFont(new Font("Times New Roman", Font.BOLD,22));
        text1.setBounds(275, 50, 300, 40);

        JLabel name = new JLabel("Name:");
        name.setFont(new Font("Times New Roman", Font.BOLD, 22));
        name.setBounds(100, 150, 200, 40);

         nametextfield = new JTextField();
        nametextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        nametextfield.setBounds(275, 150, 400, 40);

        JLabel fathername = new JLabel("Father's Name:");
        fathername.setFont(new Font("Times New Roman", Font.BOLD, 22));
        fathername.setBounds(100, 200, 200, 40);

        fathernametextfield = new JTextField();
        fathernametextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        fathernametextfield.setBounds(275, 200, 400, 40);

        JLabel dateofbirth = new JLabel("Date of Birth:");
        dateofbirth.setFont(new Font("Times New Roman", Font.BOLD, 22));
        dateofbirth.setBounds(100, 250, 200, 40);

        datebirth = new JDateChooser();
        datebirth.setForeground(Color.RED);
        datebirth.setBounds(275, 250, 400, 40);

        JLabel gender = new JLabel("Gender:");
        gender.setFont(new Font("Times New Roman", Font.BOLD, 22));
        gender.setBounds(100, 300, 200, 40);

        male = new JRadioButton("Male");
        male.setFont(new Font("Times New Roman", Font.BOLD, 18));
        male.setBounds(275, 300, 100, 30);
        male.setBackground(Color.white);

        female = new JRadioButton("Female");
        female.setFont(new Font("Times New Roman", Font.BOLD, 18));
        female.setBounds(430, 300, 100, 30);
        female.setBackground(Color.white);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(male);
        buttonGroup.add(female);


        JLabel email = new JLabel("Email:");
        email.setFont(new Font("Times New Roman", Font.BOLD, 22));
        email.setBounds(100, 350, 200, 40);

        emailtextfiels = new JTextField();
        emailtextfiels.setFont(new Font("Times New Roman", Font.BOLD, 22));
        emailtextfiels.setBounds(275, 350, 400, 40);

        JLabel maritalStatus = new JLabel("Marital Status:");
        maritalStatus.setFont(new Font("Times New Roman", Font.BOLD, 22));
        maritalStatus.setBounds(100, 400, 200, 40);

        married = new JRadioButton("Married");
        married.setFont(new Font("Times New Roman", Font.BOLD, 18));
        married.setBounds(275, 400, 100, 30);
        married.setBackground(Color.white);

        unmarried = new JRadioButton("UnMarried");
        unmarried.setFont(new Font("Times New Roman", Font.BOLD, 18));
        unmarried.setBounds(430, 400, 130, 30);
        unmarried.setBackground(Color.white);

        other = new JRadioButton("Other");
        other.setFont(new Font("Times New Roman", Font.BOLD, 18));
        other.setBounds(580, 400, 100, 30);
        other.setBackground(Color.white);




        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(married);
        buttonGroup1.add(unmarried);
        buttonGroup1.add(other);


        JLabel address = new JLabel("Address:");
        address.setFont(new Font("Times New Roman", Font.BOLD, 22));
        address.setBounds(100, 450, 200, 40);

        addresstextfield = new JTextField();
        addresstextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        addresstextfield.setBounds(275, 450, 400, 40);

        JLabel city = new JLabel("City:");
        city.setFont(new Font("Times New Roman", Font.BOLD, 22));
        city.setBounds(100, 500, 200, 40);

        citytextfield = new JTextField();
        citytextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        citytextfield.setBounds(275, 500, 400, 40);

        JLabel state = new JLabel("State:");
        state.setFont(new Font("Times New Roman", Font.BOLD, 22));
        state.setBounds(100, 550, 200, 40);

        statetextfield = new JTextField();
        statetextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        statetextfield.setBounds(275, 550, 400, 40);

        JLabel pincode = new JLabel("Pin Code:");
        pincode.setFont(new Font("Times New Roman", Font.BOLD, 22));
        pincode.setBounds(100, 600, 200, 40);

        pincodetextfield = new JTextField();
        pincodetextfield.setFont(new Font("Times New Roman", Font.BOLD, 22));
        pincodetextfield.setBounds(275, 600, 400, 40);

        JLabel text12 = new JLabel("Phone Number:");
        text12.setFont(new Font("Times New Roman", Font.BOLD, 22));
        text12.setBounds(100, 650, 200, 40);
        add(text12);

        phoneNumber = new JTextField();
        phoneNumber.setBounds(275, 650, 400, 40);
        phoneNumber.setFont(new Font("Times New Roman", Font.BOLD, 22));
        add(phoneNumber);


        next = new JButton("Next");
        next.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        next.setBackground(Color.black);
        next.setForeground(Color.white);
        next.setBounds(545, 700, 125, 50);
        next.addActionListener(this );

        setLayout(null);
        setSize(800,850);
        setVisible(true);
        setLocation(400,10);
        setLayout(null);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // for proper exit


        add(formno);
        add(name);
        add(text1);
        add(fathername);
        add(dateofbirth);
        add(gender);
        add(email);
        add(maritalStatus);
        add(address);
        add(city);
        add(state);
        add(pincode);
        add(nametextfield);
        add(fathernametextfield);
        add(datebirth);
        add(emailtextfiels);
        add(male);
        add(female);
        add(married);
        add(unmarried);
        add(other);
        add(addresstextfield);
        add(citytextfield);
        add(statetextfield);
        add(pincodetextfield);
        add(next);


    }
    public void actionPerformed(ActionEvent e)
    {

        String formno = STR."\{ran}";
        String name = nametextfield.getText();
        String fname= fathernametextfield.getText();
        String dob;
        dob = ((JTextField)datebirth.getDateEditor().getUiComponent()).getText();
        String address = addresstextfield.getText();
        String gender=null;
        if(male.isSelected()){
            gender="male";
        }
        else if(female.isSelected()){
            gender="female";
        }
        String email = emailtextfiels.getText();
        String marital =null;
        if(married.isSelected()){
            marital ="married";
        }
        else if(unmarried.isSelected()){
            marital ="unmarried";
        }
        else if(other.isSelected()){
            marital ="other";
        }
        String addr = addresstextfield.getText();
        String city = citytextfield.getText();
        String state = statetextfield.getText();
        String pincode = pincodetextfield.getText();
        String phone =phoneNumber.getText();

        try {
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your name");
            } else if (fname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your father's name");
            } else if (dob.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your Date of Birth");
            } else if (address == null) {
                JOptionPane.showMessageDialog(null, "Please enter your Address");
            } else if (gender == (null)) {
                JOptionPane.showMessageDialog(null, "Please select your gender");
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your Email");
            } else if (marital == null) {
                JOptionPane.showMessageDialog(null, "Please select your marital");
            } else if (phone.length() != 10) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number");
            } else {
                String query = "INSERT INTO signup (formno, name, father_name, dob, gender, email, phonenumber, marital, address, city, state, pincode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = Conn.getConnection();
                     PreparedStatement pst = conn.prepareStatement(query)) {
                    pst.setString(1, formno);
                    pst.setString(2, name);
                    pst.setString(3, fname);
                    pst.setString(4, dob);
                    pst.setString(5, gender);
                    pst.setString(6, email);
                    pst.setString(7, phone);
                    pst.setString(8, marital);
                    pst.setString(9, addr);
                    pst.setString(10, city);
                    pst.setString(11, state);
                    pst.setString(12, pincode);

                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Signup Successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Signup Failed");
                    }


                    setVisible(false);
                    new SignPhaseTwo(formno).setVisible(true);
                }

            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }


    }
}