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
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;

public class SocialNetworkDisplay extends JFrame {




        public static void main(String [] args)
        {
            JList<String> nameList;
            DefaultListModel<String> listModel = new DefaultListModel<>(); 
            JFrame frame = new JFrame();
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            JPanel panel3 = new JPanel();
            JPanel panel4 = new JPanel();
            JLabel label1 = new JLabel("FRIENDS LIST");

            panel4.add(label1);
            listModel.addElement("Ameera");
            listModel.addElement("Edward");
            nameList = new JList<>(listModel); 
            panel1.add(nameList);
            nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            frame.setBackground(Color.WHITE);
            frame.setLayout(new BorderLayout());
            frame.add(panel1, BorderLayout.WEST);  
            frame.add(panel2, BorderLayout.EAST);
            frame.add(panel3, BorderLayout.CENTER);
            panel1.add(panel4, BorderLayout.NORTH);
            
            panel1.setBackground(Color.WHITE);            
            panel1.setPreferredSize(new Dimension(450,450));
            
            panel2.setBackground(Color.WHITE);
            panel2.setPreferredSize(new Dimension(450,450));
            
            panel3.setBackground(Color.WHITE);
            panel3.setPreferredSize(new Dimension(450,450));
            
            panel4.setBackground(Color.WHITE);
            panel4.setPreferredSize(new Dimension(100,50));
            
            nameList.addListSelectionListener(new ListSelectionListener() { 
            @Override
            public void valueChanged(ListSelectionEvent e) 
            { 
                if(!e.getValueIsAdjusting()) { 
                    final List<String> selectedValuesList = nameList.getSelectedValuesList(); 
                    System.out.println(selectedValuesList); 
                } 
            } 
            });
                        
            frame.setVisible(true);
            
        }
}
