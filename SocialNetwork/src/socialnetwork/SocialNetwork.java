/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialnetwork;
import java.sql.*;
import java.io.*;
import javax.swing.*;
/**
 *
 * @author AmeeraK
 */
public class SocialNetwork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
         final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
         
         Connection conn = DriverManager.getConnection("jdbc:mysql://address=(protocol=tcp)(host=omega.uta.edu) (port=3306) (user=axk2904) (password=April123)/axk2904"); 
         System.out.println(conn.getSchema());
         
        
        
    }
    
}
