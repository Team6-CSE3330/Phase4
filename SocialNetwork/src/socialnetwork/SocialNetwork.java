package socialnetwork;
import java.sql.*;
import java.io.*;
import java.util.Date;
import java.lang.String;

/*
 * @author AmeeraK, EdwardF
 */
public class SocialNetwork {
	private Connection conn;
	private String userName, password;
	private final static String local_host = "127.0.0.1:3306/";
	private final static String local_host_schema = "phase4";

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
		int new_ID = 0;
		try {
			new_ID = executeQuery("select count(*) from member").getInt("count(*)");
		} catch (SQLException e) {  }
		if(new_ID <= 0) return false;

		String phone_num = "817";
		String password = "123456seven";
		String email = name.toLowerCase() + "@mavs.uta.edu";

		String sql 
		= "insert into member "
				+ "values ('" + name + "', '" + phone_num + "', '" 
				+ password + "', '" + email + "', " + new_ID
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
	
	public int logIn(String email, String password) throws SQLException {
		String sql = "";
		sql = "select Member_ID from member where "
				+ "Email ='" + email + "' and password_ ='" + password + "';";
		ResultSet r = executeQuery(sql);
		r.next();
		return r.getInt("Member_ID");
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

	public ResultSet viewPublicMessages(int myID) {
		ResultSet r = getAllFriends(myID);
		String sql = "";
		try {
			while(r.next()) {
				sql 
				= "select pub.Timestamp_, pub.Message, member.Name "
						+ "from public_message as pub, member "
						+ "where pub.Owner_ID =" + r.getString("Member_ID") + " or pub.Owner_ID =" + myID
						+ " and " + " member.Member_ID =" + r.getString("Member_ID") + " "
						+ "order by Timestamp_ desc;";
			}
		} catch (SQLException e) {
			
		}
		sql = "select public.Timestamp_, public.Message, member.Name "
				+ "from member, public_message as public "
				+ "where public.Owner_ID = member.Member_ID "
				+ "and public.Owner_ID IN ("
				+	"SELECT MEMBER.Member_ID "
				+	"FROM MEMBER "
				+	"WHERE MEMBER.Member_ID IN ("
				+		"SELECT FRIEND_OF.Member_ID "
				+		"FROM FRIEND_OF "
				+		"WHERE FRIEND_OF.Owner_ID = " + myID + ") "
				+	"or Member.Member_ID = " + myID + ") "
				+ "and public.Timestamp_ not in ( "
				+	"select private_message.Timestamp_ "
				+    "from private_message"
				+") order by Timestamp_ desc;";
		return executeQuery(sql);
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
		= "select distinct member.Member_ID, member.Name "
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
				+ "values (" + myID + ", '" + timestamp + "', '" + msg
				+ "');";
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
				+ "values (" + myID + ", '" + timestamp + "', '" + msg
				+ "');";
		return 0 < executeUpdate(sql);
	}

	public boolean createGroup(int myID, String groupName) {
		String sql; int groupSize = 0;

		try {
			groupSize = executeQuery("select count(*) from group_").getInt("count(*)");
		} catch (SQLException e) {
			// e.printStackTrace();
		} // last number

		sql = "insert into group_ "
				+ "values (" + groupSize + ", \"" + groupName + "\", true"
				+ ");";
		if (0 >= executeUpdate(sql)) return false;

		sql = "insert into owns_group "
				+ "values (" + myID + ", " + groupSize 
				+ ");";
		return 0 < executeUpdate(sql);
	}

	public boolean messageMembersOfGroup(int myID, String msg, int groupID) {
		ResultSet r = getMembersInMyGroup(myID, groupID);
		try {
			while(r.next()) {
				if(false == sendPrivateMessage(myID, r.getInt("Member_ID"), msg))
					return false;
			}
		} catch(SQLException e) { return false; }
		return true;
	}

	private ResultSet executeQuery(String sql) {
		try {
			return conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Error here");
			e.printStackTrace();
			return null;
		}
	}

	private int executeUpdate(String sql) {
		try {
			return conn.createStatement().executeUpdate(sql);
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException exc) {
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}



