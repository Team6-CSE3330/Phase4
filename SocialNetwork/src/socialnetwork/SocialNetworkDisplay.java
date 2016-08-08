package socialnetwork;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.JInternalFrame;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;


public class SocialNetworkDisplay extends JFrame {

        final  int myid = 5;
        SocialNetwork sn = new SocialNetwork("root", "edward05");
        ArrayList<Integer> myfriendsids = new ArrayList<Integer>();
        ArrayList<Integer> searchedids = new ArrayList<Integer>();
        JList<String> nameList;
        JList<String> searchList;
        JList<String> messageList;
        DefaultListModel<String> nameListModel = new DefaultListModel<>(); 
        DefaultListModel<String> searchListModel = new DefaultListModel<>(); 
        DefaultListModel<String> messageListModel = new DefaultListModel<>(); 
        
        JFrame mainwindow = new JFrame();
        JFrame PopUpWindow = new JFrame();
        JPanel LeftMainWindow = new JPanel();
        JPanel CenterMainWindow = new JPanel();
        JPanel RightMainWindow = new JPanel();
        JPanel RightMessageArea = new JPanel();
        JPanel LeftFriendSearchArea = new JPanel();
        JPanel AddFriendPopUpWindow = new JPanel();
        
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
//            
//            r = socialnetwork.viewPrivateMessages(3, 4);

            mainwindow.setBackground(Color.WHITE);
            mainwindow.setLayout(new BorderLayout());
            RightMainWindow.setLayout(new GridLayout(3,1));

            mainwindow.add(CenterMainWindow, BorderLayout.CENTER);
            mainwindow.add(RightMainWindow, BorderLayout.EAST);

            CenterMainWindow.setBackground(Color.GRAY);
            CenterMainWindow.setPreferredSize(new Dimension(450,450));
            
            RightMainWindow.setBackground(Color.WHITE);
            RightMainWindow.setPreferredSize(new Dimension(450,450));
           
            RightMessageArea.setBackground(Color.WHITE);
            RightMessageArea.setPreferredSize(new Dimension(100,300));
            RightMessageArea.add(textfield);
            RightMessageArea.add(sendbutton);
           
            socialNetworkPanel1SetUp();            

            LeftFriendSearchArea.setBackground(Color.WHITE);
            LeftFriendSearchArea.setPreferredSize(new Dimension(100,300));
            LeftFriendSearchArea.add(searchtext);
            LeftFriendSearchArea.add(searchbutton);  
            
           
            
              sendbutton.addActionListener(new ActionListener() { 
              @Override
              public void actionPerformed(ActionEvent e) { 
                  sn.sendPrivateMessage(myid, myfriendsids.get(nameList.getSelectedIndex()), textfield.getText());
                  try {
                      createMessageList(nameList.getSelectedIndex());
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
                      SocialNetworkPopUpDisplay(searchtext.getText());
                  } catch (SQLException ex) {
                      Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  searchtext.setText("");

              } 
            });           
         
                        
            mainwindow.setVisible(true);
            
        }
        public void createMessageList(int friend) throws SQLException
        {   
            RightMainWindow.removeAll();
            RightMainWindow.revalidate();
            RightMainWindow.repaint();
            searchListModel.clear();
            ResultSet r = sn.viewPrivateMessages(myid, myfriendsids.get(friend));
            while(r.next())
            {
                searchListModel.addElement(r.getString("Message") + " @ " + r.getString("Timestamp_"));

            }
            

            messageList = new JList<>(searchListModel); 
            RightMainWindow.add(messageList);
            RightMainWindow.add(RightMessageArea, BorderLayout.SOUTH);
   
            
            
            System.out.println(friend + ", " + myfriendsids.get(friend));
        }
        public void socialNetworkPanel1SetUp() throws SQLException
        {
            LeftMainWindow.removeAll();
            LeftMainWindow.revalidate();
            LeftMainWindow.repaint();       
            mainwindow.add(LeftMainWindow, BorderLayout.WEST);  
            nameList = new JList<>(nameListModel); 
            nameListModel.clear();
            LeftMainWindow.add(nameList);
            nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            ResultSet r = sn.getAllFriends(myid);
            while(r.next())
            {
                nameListModel.addElement(r.getString("Name"));
                myfriendsids.add(r.getInt("Member_ID"));
                
            }        
            
            LeftMainWindow.add(LeftFriendSearchArea, BorderLayout.SOUTH);
            nameList.addListSelectionListener(new ListSelectionListener() { 
            @Override
            public void valueChanged(ListSelectionEvent e) 
            { 
                if(!e.getValueIsAdjusting()) { 
                    try {
                        createMessageList(nameList.getSelectedIndex());
                    } catch (SQLException ex) {
                        Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    
                } 
            } 
            });
            
            LeftMainWindow.setLayout(new GridLayout(3,1));             
            LeftMainWindow.setBackground(Color.WHITE);            
            LeftMainWindow.setPreferredSize(new Dimension(450,450));
        }
        public void SocialNetworkPopUpDisplay(String friendsearched) throws SQLException
        {
            ResultSet r = sn.searchForMember(friendsearched);
            while(r.next())
            {
                System.out.println(1);
                messageListModel.addElement(r.getString("Name"));
                searchedids.add(r.getInt("Member_ID"));
            }
            
            PopUpWindow.setVisible(true);
            PopUpWindow.setBackground(Color.WHITE);
            PopUpWindow.setLayout(new BorderLayout());
            PopUpWindow.add(AddFriendPopUpWindow, BorderLayout.CENTER);
            
            searchList = new JList<>(messageListModel); 
            AddFriendPopUpWindow.add(searchList);

            AddFriendPopUpWindow.setBackground(Color.WHITE);            
            AddFriendPopUpWindow.setPreferredSize(new Dimension(450,450));
            AddFriendPopUpWindow.add(addbutton);
            
            addbutton.addActionListener(new ActionListener() { 
              @Override
              public void actionPerformed(ActionEvent e) { 
                  sn.addFriend(myid, searchedids.get(searchList.getSelectedIndex()));
                  sn.addFriend(searchedids.get(searchList.getSelectedIndex()), myid);
                  try {
                      socialNetworkPanel1SetUp();
                  } catch (SQLException ex) {
                      Logger.getLogger(SocialNetworkDisplay.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  PopUpWindow.setVisible(false);

              } 
            });      
             
             
            searchList.addListSelectionListener(new ListSelectionListener() { 
            @Override
            public void valueChanged(ListSelectionEvent e) 
            { 
                if(!e.getValueIsAdjusting()) { 
                    
                    
                } 
            } 
            });
            
        }
        

     
}
