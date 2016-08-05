/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialnetwork;
import java.sql.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.lang.String;
 /*
 * @author AmeeraK
 */
public class SocialNetwork {
/* File name : JDBCconnect.java */

  Connection conn;
   String hostname, database;

   public SocialNetwork(String sys, String data) {
      hostname = sys;
      database = data;
   }

   public boolean OpenConnection() throws SQLException, IOException {

      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
      }
      catch (ClassNotFoundException e) {
         System.out.println("Could not load the driver.");
         e.printStackTrace();
         return false;
      }

      try {
         if (hostname.equals("omega.uta.edu")) {
            if (database.equals("CSE1"))
               conn = DriverManager.getConnection
               // substitute with your username and password
               ("jdbc:oracle:thin:@omega.uta.edu:1521:axk2904", "axk2904@omega.uta.edu", "April123");
         }
         return true;
      }
      catch (SQLException sql) {
         sql.printStackTrace();
         return false;
      }
   }

   public void CloseConnection() throws SQLException {
      conn.close();
   }


   public ResultSet ListAll() {
      try {
         Statement st = conn.createStatement();
         ResultSet rset = st.executeQuery("select username from all_users order by username");
         return rset;
      }
      catch (SQLException sql) {
         sql.printStackTrace();
         return null;
      }
   }

   public static void main( String args[] )
   {
      ResultSet reset;
      try {
         SocialNetwork connect = new SocialNetwork("omega.uta.edu","CSE1");
         // JDBCconnect connect = new JDBCconnect(args[0], args[1]);
         /*if ( connect.OpenConnection() ) {
            reset = connect.ListAll( );

            while( reset.next() ) {
               System.out.println(reset.getString("username"));
            }
         }
         
          */
         connect.OpenConnection();
         connect.CloseConnection();
      }
      catch (SQLException exception) {
         System.out.println("\nSQLException" + exception.getMessage()+"\n");
      }
      catch ( IOException e) {
         e.printStackTrace();
      }
   }
}
        
           

