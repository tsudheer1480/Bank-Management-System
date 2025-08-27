package bankAtm;

import java.sql.*;

class Conn {
    Connection c;
    Statement st;
    public Conn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c= DriverManager.getConnection("jdbc:mysql:///bankmanagementsystem","root","sudheer@1480$");
            st=c.createStatement();

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}