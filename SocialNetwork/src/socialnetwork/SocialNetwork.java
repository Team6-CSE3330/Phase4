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
import java.util.Date;
import java.lang.String;
 /*
 * @author AmeeraK
 */
public class SocialNetwork {
	private Connection conn;
	private String userName, password;
    //private final static String omega = "omega.uta.edu:3306/";
    //private final static String omega_schema = "axk2904";
    private final static String local_host = "127.0.0.1:3306/";
    private final static String local_host_schema = "phase4";

    public static void main( String args[] ) {
 	   ResultSet r;
 	   SocialNetwork socialnetwork = new SocialNetwork("root", "edward05");
 	   try {
 		   if ( socialnetwork.OpenConnection() ) { //local_host, "phase4"
 			  r = socialnetwork.getMembersInMyGroup(2, 4);
 			  System.out.println("List members of my group");
 			  while( r.next() ) 
 				   System.out.println("person: " + r.getString("name"));// + " is " + r.getString("name"));
 		   }
 		  socialnetwork.CloseConnection();
 	   } catch (SQLException exception) {
           System.out.println("\nSQLException" + exception.getMessage()+"\n");
        } catch ( IOException e) {
           e.printStackTrace();
        }
    }

    public SocialNetwork(String userName, String password) {
    	this.userName = userName;
    	this.password = password;
    }

    //String hostname, String database
	public boolean OpenConnection() throws SQLException, IOException {
		   //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		try {	  
			conn = DriverManager.getConnection("jdbc:mysql://" + local_host + 
					local_host_schema + "?useSSL=false&user=" + this.userName + "&password=" + this.password);
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

	public boolean createNewMember(String name) {
		int new_ID = 1;
		ResultSet s = executeQuery("select Member_ID from member");
		try {
			if(s.last())
				new_ID += s.getInt("Member_ID");
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
			
       String sql 
	       = "insert into member "
	       + "values (" + new_ID + ", " + name
	       + ");";
       return 0 < executeUpdate(sql);
	}

	public ResultSet getMemberInformation(int myID) {
		  String sql 
		    = "SELECT MEMBER.* "
		    + "FROM MEMBER "
		    + "WHERE MEMBER.Member_ID =" + myID
		    + ";";
		 return executeQuery(sql);
	}

   public ResultSet getAllFriends(int myID) {
        String sql 
        	   = "SELECT MEMBER.Member_ID, MEMBER.Name "
        	   + "FROM MEMBER "
        	   + "WHERE MEMBER.Member_ID IN ("
        	   +     "SELECT FRIEND_OF.Member_ID "
        	   +     "FROM FRIEND_OF "
        	   +     "WHERE FRIEND_OF.Owner_ID =" + myID
        	   + ") order by Member.Name asc;";
        return executeQuery(sql);
   }

   public ResultSet searchForMember(String name) {
        String sql 
           = "SELECT MEMBER.Member_ID, MEMBER.Name "
           + "FROM MEMBER "
           + "WHERE MEMBER.Name =\"" + name 
           + "\";";
        return executeQuery(sql);
   }
	
	public ResultSet getMemberAddress(int myID) {
        String sql 
	        = "SELECT ADDRESS.* "
	        + "FROM ADDRESS "
	        + "WHERE ADDRESS.Member_ID =" + myID 
	        + ";";
        return executeQuery(sql);
	}
	
   public boolean addFriend(int myID, int newFriendID) {
       String sql 
	       = "insert into friend_of "
	       + "values (" + myID + ", " + newFriendID
	       + ");";
       return 0 < executeUpdate(sql);
   }
   
   public boolean removeFriend(int myID, int friendsID) {
       String sql 
	       = "delete from friend_of "
	       + "where Owner_ID =" + myID + " and Member_ID =" + friendsID
	       + ";";
       return 0 < executeUpdate(sql);
   }
   
   public ResultSet viewPrivateMessages(int myID, int friendsID) {
       String sql 
		   = "select Timestamp_, Message "
		   + "from public_message "
		   + "where Timestamp_ in ("
		   +    "select Timestamp_ "
		   +    "from private_message "
		   +    "where (from_member =" + myID + " and to_member =" + friendsID + ") or "
		   +          "(from_member =" + friendsID + " and to_member =" + myID + ")"
		   + ") order by Timestamp_ asc;";
	   return executeQuery(sql);
   }
   
   public boolean addFriendToGroup(int friendID, int groupID) {
       String sql 
	       = "insert into group_member_id "
	       + "values (" + groupID + ", " + friendID
	       + ");";
       return 0 < executeUpdate(sql);
   }
   
   public ResultSet getAllOfMyGroups(int myID) {
       String sql 
	       = "select owns_group.Group_ID, g.Name "
	       + "from owns_group, group_ as g "
	       + "where owns_group.Owner_ID =" + myID 
	       + " and g.Group_ID =owns_group.Group_ID;";
	   return executeQuery(sql);
   }
   
   public ResultSet getMembersInMyGroup(int myID, int groupID) {
       String sql 
	       = "select distinct member.Name "
	       + "from group_member_id, owns_group, member "
	       + "where owns_group.Owner_ID =" + myID 
	       + " and owns_group.Group_ID =" + groupID 
	       + " and group_member_id.Group_ID =" + groupID
	       + " and group_member_id.Member_ID = member.Member_ID;";
	   return executeQuery(sql);
   }
   
   public boolean sendPrivateMessage(int myID, int friendID, String msg) {
	   String timestamp = new Date().toString();
       String sql;
       sql = "insert into public_message "
    	       + "values (" + myID + ", \"" + timestamp + "\", \"" + msg
    	       + "\");";
       
       //Note, not calling createPublicMessage because we want timestamp to be the same between both
       //a difference of even a microsecond means they are different. Lastly, passing timestamp
       //means we would need to pass timestamp into that function from outside this class, meaning
       //we would need to generate timestamp outside.
       if(executeUpdate(sql) <= 0) return false;
       
       sql = "insert into private_message "
	       + "values (" + myID + ", \"" + timestamp + "\", " + friendID
	       + ");";
	   return executeUpdate(sql) > 0;
   }
   
   public boolean createPublicMessage(int myID, String msg) {
	   String timestamp = new Date().toString();
       String sql;
       sql = "insert into public_message "
    	       + "values (" + myID + ", \"" + timestamp + "\", \"" + msg
    	       + "\");";
       return 0 < executeUpdate(sql);
   }
   
   /*
   public ResultSet createGroup(int myID, String groupName) {
   }
   
   public ResultSet messageMembersOfGroup(int myID, String msg, int groupID) {
   }
   */
   private ResultSet executeQuery(String sql) {
	    try {
	        return conn.createStatement().executeQuery(sql);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
   }
   
    private int executeUpdate(String sql) {
	    try {
	        return conn.createStatement().executeUpdate(sql);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
   }
}
        
           

