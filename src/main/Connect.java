
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Connect {
	
	static String databaseName="pharmacy";
	static String url="jdbc:mysql://localhost:3306/"+databaseName;
	static String username="root";
	static String password = "Rohith";
	
  @SuppressWarnings("deprecation")
public static Connection connect(){
       try{
    Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
    Connection con = DriverManager.getConnection(url,username,password);
    if(con!=null)return con ;
   }catch(Exception e){
    JOptionPane.showMessageDialog(null, e.getMessage(),"Error",2);
   }
   return null ;
}    
  }
   