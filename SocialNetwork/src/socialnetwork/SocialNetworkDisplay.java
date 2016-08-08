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
        DefaultListModel<String> listModel = new DefaultListModel<>(); 
        DefaultListModel<String> listModel2 = new DefaultListModel<>(); 
        DefaultListModel<String> listModel3 = new DefaultListModel<>(); 
        
        JFrame frame = new JFrame();
        JFrame frame2 = new JFrame();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        
        JButton button = new JButton("SEND");
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

            frame.setBackground(Color.WHITE);
            frame.setLayout(new BorderLayout());
            panel3.setLayout(new GridLayout(3,1));

            frame.add(panel2, BorderLayout.CENTER);
            frame.add(panel3, BorderLayout.EAST);

            panel2.setBackground(Color.GRAY);
            panel2.setPreferredSize(new Dimension(450,450));
            
            panel3.setBackground(Color.WHITE);
            panel3.setPreferredSize(new Dimension(450,450));
           
            panel4.setBackground(Color.WHITE);
            panel4.setPreferredSize(new Dimension(100,300));
            panel4.add(textfield);
            panel4.add(button);
           
            socialNetworkPanel1SetUp();            

            panel5.setBackground(Color.WHITE);
            panel5.setPreferredSize(new Dimension(100,300));
            panel5.add(searchtext);
            panel5.add(searchbutton);  
            
           
            
              button.addActionListener(new ActionListener() { 
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
         
                        
            frame.setVisible(true);
            
        }
        public void createMessageList(int friend) throws SQLException
        {   
            panel3.removeAll();
            panel3.revalidate();
            panel3.repaint();
            listModel2.clear();
            ResultSet r = sn.viewPrivateMessages(myid, myfriendsids.get(friend));
            while(r.next())
            {
                listModel2.addElement(r.getString("Message") + " @ " + r.getString("Timestamp_"));

            }
            

            messageList = new JList<>(listModel2); 
            panel3.add(messageList);
            panel3.add(panel4, BorderLayout.SOUTH);
   
            
            
            System.out.println(friend + ", " + myfriendsids.get(friend));
        }
        public void socialNetworkPanel1SetUp() throws SQLException
        {
            panel1.removeAll();
            panel1.revalidate();
            panel1.repaint();       
            frame.add(panel1, BorderLayout.WEST);  
            nameList = new JList<>(listModel); 
            listModel.clear();
            panel1.add(nameList);
            nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            ResultSet r = sn.getAllFriends(myid);
            while(r.next())
            {
                listModel.addElement(r.getString("Name"));
                myfriendsids.add(r.getInt("Member_ID"));
                
            }        
            
            panel1.add(panel5, BorderLayout.SOUTH);
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
            
            panel1.setLayout(new GridLayout(3,1));             
            panel1.setBackground(Color.WHITE);            
            panel1.setPreferredSize(new Dimension(450,450));
        }
        public void SocialNetworkPopUpDisplay(String friendsearched) throws SQLException
        {
            ResultSet r = sn.searchForMember(friendsearched);
            while(r.next())
            {
                System.out.println(1);
                listModel3.addElement(r.getString("Name"));
                searchedids.add(r.getInt("Member_ID"));
            }
            
            frame2.setVisible(true);
            frame2.setBackground(Color.WHITE);
            frame2.setLayout(new BorderLayout());
            frame2.add(panel6, BorderLayout.CENTER);
            
            searchList = new JList<>(listModel3); 
            panel6.add(searchList);

            panel6.setBackground(Color.WHITE);            
            panel6.setPreferredSize(new Dimension(450,450));
            panel6.add(addbutton);
            
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
                  frame2.setVisible(false);

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
