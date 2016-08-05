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

public class SocialNetworkDisplay extends JFrame {

	private JPanel contentPane;
	private JList<String> friendsList;


	/**
	 * Create the frame.
	 */
	public SocialNetworkDisplay() {
		setBackground(Color.WHITE);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0,0));
                
		
		JPanel panelFriendsList = new JPanel();
		panelFriendsList.setBackground(Color.WHITE);
		getContentPane().add(panelFriendsList, BorderLayout.WEST);
                panelFriendsList.setSize(new Dimension(150,250));  
                
		panelFriendsList.setLayout(null);
		
		JLabel lblFriendsList = new JLabel("Friends List");
		lblFriendsList.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblFriendsList.setBounds(12, 13, 128, 27);
		panelFriendsList.add(lblFriendsList);
		
		DefaultListModel<String> friendListModel = new DefaultListModel<>();
		friendListModel.addElement("Edward F");
		friendListModel.addElement("Ameera K");		
		
		friendsList = new JList<>(friendListModel);
		
		friendsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					final List<String> selectedValuesList;
					selectedValuesList = friendsList.getSelectedValuesList();
					System.out.println(selectedValuesList);
				}
			}
		});
		
		friendsList.setBounds(12, 53, 178, 651);
		panelFriendsList.add(friendsList);
		
		JPanel panelLoginSearch = new JPanel();
		panelLoginSearch.setBackground(new Color(0, 0, 128));
		getContentPane().add(panelLoginSearch, BorderLayout.NORTH);
		
		JPanel panelStatus = new JPanel();
		panelStatus.setBackground(Color.WHITE);
		getContentPane().add(panelStatus, BorderLayout.CENTER);
		
		JPanel panelChats = new JPanel();
		panelChats.setBackground(Color.WHITE);
		getContentPane().add(panelChats, BorderLayout.EAST);
		
		
	}
        public static void main(String [] args)
        {
            SocialNetworkDisplay social = new SocialNetworkDisplay();
            social.setVisible(true);
        }
}
