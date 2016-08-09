package socialnetwork;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SocialNetworkLogin extends JFrame {

	private	JFrame login_window = new JFrame();
	private JPanel center_panel_of_login_window = new JPanel();
	private JTextField email_text_field = new JTextField(20);
	private JTextField password_text_field = new JTextField(16);
	private JButton log_in_button = new JButton("LOG IN");
	private String email = "";
	private String password = "";
	private SocialNetwork sn = new SocialNetwork("root", "edward05");

	public static void main(String [] args) throws IOException, SQLException
	{
		new SocialNetworkLogin();
	}

	public SocialNetworkLogin() {
		
		
		login_window.setBackground(Color.WHITE);
		login_window.setLayout(new BorderLayout());
		login_window.setSize(300, 300);
		center_panel_of_login_window.setBackground(Color.WHITE);
		login_window.add(center_panel_of_login_window);
		
		center_panel_of_login_window.add(new JTextArea("          Welcome to The Social Network!           "));
		center_panel_of_login_window.add(new JTextArea("Email: "));
		center_panel_of_login_window.add(email_text_field);
		center_panel_of_login_window.add(new JTextArea("Password:"));
		center_panel_of_login_window.add(password_text_field);
		center_panel_of_login_window.add(log_in_button);
		
		login_window.setVisible(true);
		try {
			sn.OpenConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		log_in_button.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				try {
					int id = sn.logIn(email_text_field.getText(), password_text_field.getText());
					System.out.println(id);
					new SocialNetworkDisplay(id);
					login_window.setVisible(false);
					sn.CloseConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} 
		});
	}
	
}