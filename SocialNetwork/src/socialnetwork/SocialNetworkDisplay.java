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

        final  int myid = 3;
        SocialNetwork sn = new SocialNetwork("root", "edward05");
        ArrayList<Integer> myfriendsids = new ArrayList<Integer>();
        JList<String> nameList;
        JList<String> messageList;
        DefaultListModel<String> listModel = new DefaultListModel<>(); 
        DefaultListModel<String> listModel2 = new DefaultListModel<>(); 
        JFrame frame = new JFrame();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JButton button = new JButton("SEND");
        JTextField textfield = new JTextField(25);
        
        public static void main(String [] args) throws IOException, SQLException
        {
            SocialNetworkDisplay snd = new SocialNetworkDisplay();
        }
        public SocialNetworkDisplay() throws SQLException, IOException
        {            
            sn.OpenConnection();
            ResultSet r = sn.getAllFriends(2);
            while(r.next())
            {
                listModel.addElement(r.getString("Name"));
                myfriendsids.add(r.getInt("Member_ID"));
                
            }
            nameList = new JList<>(listModel); 
            panel1.add(nameList);
//            
//            r = socialnetwork.viewPrivateMessages(3, 4);

            
            
         
         
            nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            frame.setBackground(Color.WHITE);
            frame.setLayout(new BorderLayout());
            panel3.setLayout(new GridLayout(3,1));
            frame.add(panel1, BorderLayout.WEST);  
            frame.add(panel2, BorderLayout.CENTER);
            frame.add(panel3, BorderLayout.EAST);
            
            panel1.setBackground(Color.WHITE);            
            panel1.setPreferredSize(new Dimension(450,450));
            
            panel2.setBackground(Color.WHITE);
            panel2.setPreferredSize(new Dimension(450,450));
            
            panel3.setBackground(Color.WHITE);
            panel3.setPreferredSize(new Dimension(450,450));
           
            panel4.setBackground(Color.WHITE);
            panel4.setPreferredSize(new Dimension(100,300));
            panel4.add(textfield);
            panel4.add(button);
 

            
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
            
              button.addActionListener(new ActionListener() { 
              @Override
              public void actionPerformed(ActionEvent e) { 
                  textfield.getText();
                  //updatePrivateMessages with Text 
                
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
        

     
}
