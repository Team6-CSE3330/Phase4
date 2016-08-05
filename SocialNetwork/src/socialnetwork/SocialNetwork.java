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
	
	private int My_ID;

	private Connection conn;
	private String hostname, database;
    private final static String omega = "omega.uta.edu:3306/";
    //database is "axk2904", user name is "axk2904" for omega

    public static void main( String args[] ) {
 	   ResultSet reset;
 	   try {
 		   SocialNetwork connect = new SocialNetwork(omega, "axk2904");
          
 		   if ( connect.OpenConnection("axk2904", "April123") ) {
 			   reset = connect.ListAll( );

            while( reset.next() ) {
                System.out.println(reset.getString("email"));
            }
        }
          
          connect.CloseConnection();
       }
       catch (SQLException exception) {
          System.out.println("\nSQLException" + exception.getMessage()+"\n");
       }
       catch ( IOException e) {
          e.printStackTrace();
       }
    }

    public SocialNetwork(String hostname, String database) {
    	this.hostname = hostname;
    	this.database = database;
    }

	public boolean OpenConnection(String userName, String password) throws SQLException, IOException {
		   //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		try {	  
			conn = DriverManager.getConnection("jdbc:mysql://" + this.hostname + this.database + "?user=" + userName + "&password=" + password);
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
			ResultSet rset = st.executeQuery("select email from dummy_login_table order by email");
			return rset;
		} catch (SQLException sql) {
			sql.printStackTrace();
			return null;
		}
   }
   
   public ResultSet getAllFriends(int My_ID) {
	    try {
	    	Statement st = conn.createStatement();
	        ResultSet rset = st.executeQuery("  ");
	        return rset;
	    } catch (SQLException sql) {
	        sql.printStackTrace();
	        return null;
	    }
   }
   
   public ResultSet findFriend(String firstName, String lastName) {
	    try {
	    	Statement st = conn.createStatement();
	        ResultSet rset = st.executeQuery("  ");
	        return rset;
	    } catch (SQLException sql) {
	        sql.printStackTrace();
	        return null;
	    }
   }
   
   public ResultSet viewMessages(int My_ID, int Friends_ID) {
	    try {
	    	Statement st = conn.createStatement();
	        ResultSet rset = st.executeQuery("  ");
	        return rset;
	    } catch (SQLException sql) {
	        sql.printStackTrace();
	        return null;
	    }
   }
   
   public ResultSet getAllOfMyGroups(int My_ID) {
	    try {
	    	Statement st = conn.createStatement();
	        ResultSet rset = st.executeQuery("  ");
	        return rset;
	    } catch (SQLException sql) {
	        sql.printStackTrace();
	        return null;
	    }
   }
}
        
           

