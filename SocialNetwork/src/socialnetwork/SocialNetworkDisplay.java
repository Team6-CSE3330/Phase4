package socialnetwork;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocialNetworkDisplay extends JFrame {

	final  int myid = 5;
	SocialNetwork sn = new SocialNetwork("root", "edward05");
	ArrayList<Integer> myfriendsids = new ArrayList<Integer>();
	ArrayList<Integer> searchedids = new ArrayList<Integer>();
	JList<String> nameList;
	JList<String> searchList;
	JList<String> messageList;
        JList<String> publicMessageList; 
	DefaultListModel<String> nameListModel = new DefaultListModel<>(); 
	DefaultListModel<String> messageListModel = new DefaultListModel<>(); 
	DefaultListModel<String> searchListModel = new DefaultListModel<>(); 
        DefaultListModel<String> publicMessageListModel = new DefaultListModel<>();

	JFrame mainwindow = new JFrame();
	JFrame PopUpWindow = new JFrame();
	JPanel LeftMainWindow = new JPanel();
	JPanel CenterMainWindow = new JPanel();
	JPanel RightMainWindow = new JPanel();
	JPanel RightMessageArea = new JPanel();
	JPanel LeftFriendSearchArea = new JPanel();
	JPanel AddFriendPopUpWindow = new JPanel();
        JPanel CenterMessageArea = new JPanel();

	JButton sendbutton = new JButton("SEND");
	JButton searchbutton = new JButton("SEARCH");
	JButton addbutton = new JButton("ADD");

	JTextField textfield = new JTextField(25);
	JTextField searchtext = new JTextField(25);

	public static void main(String [] args) throws IOException, SQLException
	{
		SocialNetworkDisplay snd = new SocialNetworkDisplay();
	}
	public SocialNetworkDisplay() throws SQLException, IOException
	{            
		sn.OpenConnection();

		mainwindow.setBackground(Color.WHITE);
		mainwindow.setLayout(new BorderLayout());

		mainwindow.add(LeftMainWindow, BorderLayout.WEST);  
		mainwindow.add(CenterMainWindow, BorderLayout.CENTER);
		mainwindow.add(RightMainWindow, BorderLayout.EAST);
		mainwindow.setSize(1480, 800);

		LeftMainWindow.setLayout(new GridLayout(3,1));             
		LeftMainWindow.setBackground(Color.WHITE);            
		LeftMainWindow.setPreferredSize(new Dimension(450,450));
		         
		LeftFriendSearchArea.setBackground(Color.WHITE);
		LeftFriendSearchArea.setPreferredSize(new Dimension(100,300));
		LeftFriendSearchArea.add(searchtext);
		LeftFriendSearchArea.add(searchbutton);  

		CenterMainWindow.setBackground(Color.WHITE);
		CenterMainWindow.setPreferredSize(new Dimension(450,450));
            
                CenterMessageArea.setBackground(Color.WHITE);
                CenterMessageArea.setPreferredSize(new Dimension(450,450));

                publicMessageList = new JList<>(publicMessageListModel); 
                CenterMainWindow.add(CenterMessageArea);
                CenterMessageArea.add(publicMessageList);
                
		RightMainWindow.setLayout(new GridLayout(3,1));
		RightMainWindow.setBackground(Color.WHITE);
		RightMainWindow.setPreferredSize(new Dimension(450,450));

		RightMessageArea.setBackground(Color.WHITE);
		RightMessageArea.setPreferredSize(new Dimension(100,300));
		RightMessageArea.add(textfield);
		RightMessageArea.add(sendbutton);
                

		friendsAreaSetup();   
		
		//POP-UP WINDOW ITEMS

		sendbutton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				sn.sendPrivateMessage(myid, myfriendsids.get(nameList.getSelectedIndex()), textfield.getText());
				try {
					messageAreaSetup(nameList.getSelectedIndex());
					//updatePrivateMessages with Text 
				} catch (SQLException ex) {
					Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
				}
				textfield.setText("");

			} 
		});

		searchbutton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				try {            
					addFriendPopUpDisplay(searchtext.getText());
				} catch (SQLException ex) {
					Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
				}
				searchtext.setText("");

			} 
		});

		mainwindow.setVisible(true);
	}
        
        public void publicMessageAreaSetup(int friend) throws SQLException {
                CenterMainWindow.removeAll();
		CenterMainWindow.revalidate();
		CenterMainWindow.repaint();

		publicMessageListModel.clear();
		try {
			ResultSet r = sn.viewPrivateMessages(myid, myfriendsids.get(friend));
			while(r.next())	{
                            publicMessageListModel.addElement(r.getString("Message") + " @ " + r.getString("Timestamp_"));
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println("no friends in list currently");
			publicMessageListModel.addElement("");
		}

		publicMessageList = new JList<>(publicMessageListModel); 

		CenterMessageArea.add(publicMessageList);
		CenterMainWindow.add(CenterMessageArea, BorderLayout.SOUTH);
	}
	public void messageAreaSetup(int friend) throws SQLException {   
		RightMainWindow.removeAll();
		RightMainWindow.revalidate();
		RightMainWindow.repaint();

		messageListModel.clear();
		try {
			ResultSet r = sn.viewPrivateMessages(myid, myfriendsids.get(friend));
			while(r.next())	{
				messageListModel.addElement(r.getString("Message") + " @ " + r.getString("Timestamp_"));
			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println("no friends in list currently");
			messageListModel.addElement("");
		}

		messageList = new JList<>(messageListModel); 

		RightMainWindow.add(messageList);
		RightMainWindow.add(RightMessageArea, BorderLayout.SOUTH);
	}
	
	public void friendsAreaSetup() throws SQLException {
		LeftMainWindow.removeAll();
		LeftMainWindow.revalidate();
		LeftMainWindow.repaint(); 

		nameListModel.clear();
		myfriendsids.clear();

		ResultSet r = sn.getAllFriends(myid);
		while(r.next()) {
			nameListModel.addElement(r.getString("Name"));
			myfriendsids.add(r.getInt("Member_ID"));
		}
		
		nameList = new JList<>(nameListModel); 

		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.addListSelectionListener(new ListSelectionListener() { 
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					try {
						messageAreaSetup(nameList.getSelectedIndex());
                                                publicMessageAreaSetup(nameList.getSelectedIndex());
					} catch (SQLException ex) {
						Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		});
		
		LeftMainWindow.add(nameList);
		LeftMainWindow.add(LeftFriendSearchArea, BorderLayout.SOUTH);
	}
	public void addFriendPopUpDisplay(String friendsearched) throws SQLException
	{
		PopUpWindow.setBackground(Color.WHITE);
		PopUpWindow.setLayout(new BorderLayout());
		PopUpWindow.setSize(400, 300);
		
		AddFriendPopUpWindow.setBackground(Color.WHITE);            
		AddFriendPopUpWindow.setPreferredSize(new Dimension(450,450));
		
		
		searchListModel.clear();
		searchedids.clear();
		
		ResultSet r = sn.searchForMember(friendsearched);
		while(r.next())
		{
			System.out.println(1);
			searchListModel.addElement(r.getString("Name"));
			searchedids.add(r.getInt("Member_ID"));
		}

		searchList = new JList<>(searchListModel);
		
		AddFriendPopUpWindow.add(searchList);
		AddFriendPopUpWindow.add(addbutton);

		PopUpWindow.add(AddFriendPopUpWindow, BorderLayout.CENTER); 

		addbutton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sn.addFriend(myid, searchedids.get(searchList.getSelectedIndex()));
					sn.addFriend(searchedids.get(searchList.getSelectedIndex()), myid);
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					System.out.println("Add Failed");
				}
				try {
					friendsAreaSetup();
				} catch (SQLException ex) {
					Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
				}
				RightMainWindow.removeAll();
				PopUpWindow.setVisible(false);

			} 
		});      

		PopUpWindow.setVisible(true);

//
//		searchList.addListSelectionListener(new ListSelectionListener() { 
//			@Override
//			public void valueChanged(ListSelectionEvent e) 
//			{ 
//				if(!e.getValueIsAdjusting()) { 
//
//
//				} 
//			} 
//		});

	}



}
