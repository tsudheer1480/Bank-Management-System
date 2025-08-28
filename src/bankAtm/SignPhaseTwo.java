package bankAtm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;


class SignPhaseTwo extends JFrame implements ActionListener {

    String formno;
    JTextField panNo,addharNo;
    JComboBox<String>  religioncombobox;
    JComboBox<String>  categorycombobox;
    JComboBox<String> incomecombobox;
    JComboBox<String>  educationcombobox;
    JComboBox<String>  occupationcombobox;
    JRadioButton senioryes, seniorno;
    JRadioButton oldyes,oldno;
    JButton next;

    SignPhaseTwo(String formno) {
        this.formno = formno;
        setTitle("Sign Phase 2");

        JLabel text1 = new JLabel("Page 2 : Additional Details");
        text1.setFont(new Font("Times New Roman", Font.BOLD,22));
        text1.setBounds(275, 50, 300, 40);

        JLabel religion = new JLabel("Religion:");
        religion.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        religion.setBounds(100, 150, 200, 40);

        String[] reli = {"select","Hindu", "Muslim", "Christian", "Sikh", "Buddhist", "Jain", "Other"};

        religioncombobox = new JComboBox<>(reli);
        religioncombobox.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        religioncombobox.setBounds(275, 150, 400, 40);
        religioncombobox.setBackground(Color.white);

        JLabel category = new JLabel("Category:");
        category.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        category.setBounds(100, 200, 200, 40);

        String[] categories = {"select","General", "OBC", "SC", "ST", "Other"};
        categorycombobox = new JComboBox<>(categories);
        categorycombobox.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        categorycombobox.setBounds(275, 200, 400, 40);
        categorycombobox.setBackground(Color.white);

        JLabel income = new JLabel("Income:");
        income.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        income.setBounds(100, 250, 200, 40);

        String[] incomes = {
                "select",
                "Null",
                "Less than 1,50,000",
                "1,50,000 - 2,50,000",
                "2,50,000 - 5,00,000",
                "Above 5,00,000"
        };
        incomecombobox = new JComboBox<>(incomes);
        incomecombobox.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        incomecombobox.setBounds(275, 250, 400, 40);
        incomecombobox.setBackground(Color.white);



        JLabel education = new JLabel("Education" +
                "Details:");
        education.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        education.setBounds(100, 300, 200, 40);

        String[] education1 = {
                "select",
                "Non-Graduate",
                "Graduate",
                "Post-Graduate",
                "Doctrate",
                "Others"
        };
        educationcombobox = new JComboBox<>(education1);
        educationcombobox.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        educationcombobox.setBounds(275, 300, 400, 40);
        educationcombobox.setBackground(Color.white);

        JLabel occupation = new JLabel("Occupation:");
        occupation.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        occupation.setBounds(100, 350, 200, 40);

        String[] occupation1 = {
                "select",
                "Salaried",
                "Self-Employed",
                "Business",
                "Student",
                "Retired",
                "Others"
        };
        occupationcombobox = new JComboBox<>(occupation1);
        occupationcombobox.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        occupationcombobox.setBounds(275, 350, 400, 40);
        occupationcombobox.setBackground(Color.white);



        JLabel panNumber = new JLabel("PAN Number:");
        panNumber.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        panNumber.setBounds(100, 400, 200, 40);

        panNo = new JTextField();
        panNo.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        panNo.setBounds(275, 400, 400, 40);

        JLabel addhar = new JLabel("Addhar No:");
        addhar.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        addhar.setBounds(100, 450, 200, 40);

        addharNo = new JTextField();
        addharNo.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        addharNo.setBounds(275, 450, 400, 40);

        JLabel senoir = new JLabel("Senoir Citizen:");
        senoir.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        senoir.setBounds(100, 500, 200, 40);

        senioryes = new JRadioButton("Yes");
        senioryes.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        senioryes.setBounds(275, 505, 100, 30);
        senioryes.setBackground(Color.white);

        seniorno = new JRadioButton("No");
        seniorno.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        seniorno.setBounds(430, 505, 100, 30);
        seniorno.setBackground(Color.white);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(seniorno);
        buttonGroup.add(senioryes);

        JLabel old = new JLabel("Existing Account:");
        old.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        old.setBounds(100, 550, 200, 40);
        oldyes = new JRadioButton("Yes");
        oldyes.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        oldyes.setBounds(275, 555, 100, 30);
        oldyes.setBackground(Color.white);

        oldno = new JRadioButton("No");
        oldno.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        oldno.setBounds(430, 555, 100, 30);
        oldno.setBackground(Color.white);

        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(oldyes);
        buttonGroup1.add(oldno);






        next = new JButton("Next");
        next.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        next.setBackground(Color.black);
        next.setForeground(Color.white);
        next.setBounds(545, 610, 125, 50);
        next.addActionListener(this );

        setLayout(null);
        setSize(800,850);
        setVisible(true);
        setLocation(400,10);
        setLayout(null);
        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // for proper exit


        add(religion);
        add(text1);
        add(religion);
        add(category);
        add(income);
        add(education);
        add(occupation);
        add(panNumber);
        add(addhar);
        add(senoir);
        add(old);

       add(religioncombobox);
       add(categorycombobox);
       add(incomecombobox);
       add(educationcombobox);
       add(occupationcombobox);
       add(panNo);
       add(addharNo);
       add(senioryes);
       add(seniorno);
       add(oldyes);
       add(oldno);


        add(next);


    }
    public void actionPerformed(ActionEvent e)
    {

        String religion= religioncombobox.getSelectedItem().toString();
        String category= categorycombobox.getSelectedItem().toString();
        String income= incomecombobox.getSelectedItem().toString();
        String education= educationcombobox.getSelectedItem().toString();
        String occupation= occupationcombobox.getSelectedItem().toString();
        String pannumber = panNo.getText().trim();
        String addhar = addharNo.getText().trim();
        String senior = null;
        if(senioryes.isSelected()){
            senior = "Yes";
        }
        else if(seniorno.isSelected()){
            senior = "No";
        }
        String oldUser =null;
        if(oldyes.isSelected()){
            oldUser = "Yes";
        }
        else if(oldno.isSelected()){
            oldUser = "No";
        }

        try {
            if (religion.equals("select")) {
                JOptionPane.showMessageDialog(null, "Please select a religion");
            } else if (category.equals("select")) {
                JOptionPane.showMessageDialog(null, "Please select a category");
            } else if (income.equals("select")) {
                JOptionPane.showMessageDialog(null, "Please select a income");
            } else if (education.equals("select")) {
                JOptionPane.showMessageDialog(null, "Please select a education");
            } else if (occupation.equals("select")) {
                JOptionPane.showMessageDialog(null, "Please select a occupation");
            } else if (addhar.length() != 12 || !(addhar.matches("\\d+"))) {
                JOptionPane.showMessageDialog(null, "Please enter a vaild Addhar number");
            } else if (oldUser == (null)) {
                JOptionPane.showMessageDialog(null, "Please select a  user type");
            } else {
                String query = "Insert into signuptwo values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = Conn.getConnection();
                     PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, formno);
                    ps.setString(2, religion);
                    ps.setString(3, category);
                    ps.setString(4, income);
                    ps.setString(5, education);
                    ps.setString(6, occupation);
                    ps.setString(7, pannumber);
                    ps.setString(8, addhar);
                    ps.setString(9, senior);
                    ps.setString(10, oldUser);
                    ps.executeUpdate();
                    setVisible(false);
                    new SignupPhaseThree(formno);
                }

            }
        }

        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        }
    }





