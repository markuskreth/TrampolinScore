package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.kreth.vereinsmeisterschaftprog.business.MainFrameBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Pflichten;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame {

   private static final long serialVersionUID = 5118057573440157488L;
   private JPanel contentPane;
   private MainFrameBusiness business;
   private DefaultListModel<Pflichten> model;
   /**
    * Create the frame.
    */
   public MainFrame() {
      setTitle("Vereinsmeisterschaften 2014");
      business = new MainFrameBusiness();
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 736, 300);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.setLayout(new BorderLayout(0, 0));
      setContentPane(contentPane);
      
      model = new DefaultListModel<>();
      model.addElement(new Pflichten(-1, "Hinzufügen", "Kein Eintrag! Neu erstellen, wenn gewählt!"));
      for(Pflichten p: business.getPflichten())
         model.addElement(p);
      
      contentPane.add(business.getPanel(), BorderLayout.CENTER);
      
      JPanel panel = new JPanel();
      contentPane.add(panel, BorderLayout.WEST);
      panel.setLayout(new BorderLayout(0, 0));
      
      final JList<Pflichten> pflichtenView = new JList<Pflichten>();
      panel.add(pflichtenView, BorderLayout.CENTER);
      pflichtenView.setFont(new Font("Dialog", Font.BOLD, 14));
      pflichtenView.setPreferredSize(new Dimension(100, 0));
      pflichtenView.setMinimumSize(new Dimension(150, 150));
      pflichtenView.setSelectedIndex(1);
      pflichtenView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      
            pflichtenView.setModel(model);
            
            pflichtenView.addListSelectionListener(new ListSelectionListener() {
               
               @Override
               public void valueChanged(ListSelectionEvent e) {
                  
                  if (! e.getValueIsAdjusting()) {
                     
                     Pflichten selection = pflichtenView.getSelectedValue();
                     business.pflichtChange(selection);
                  }
                  
               }
            });
            
            pflichtenView.setSelectedIndex(1);  // wirft Exception, wenn noch keine Pflichten eingeeben sind! Hier ändern
      
      JButton btnExport = new JButton("Exportieren");
      btnExport.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            business.doExport();
         }
      });
      panel.add(btnExport, BorderLayout.SOUTH);
   }

}
