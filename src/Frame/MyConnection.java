/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class MyConnection {
    public static Connection getConnectionn() {
        
    Connection con=null;
    try{
      Class.forName("com.mysql.jdbc.Driver");
      con=  DriverManager.getConnection("jdbc:mysql://localhost:3306/loginjeuxdenim","root","12345");
      
        System.out.println("connection bien defini"); 
       
    }
    catch(Exception e){
        System.out.println(e.getMessage());
    }
    return con ;
    }
    
}
