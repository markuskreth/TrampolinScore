package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;


public class WettkampfPanel extends JPanel {

   private static final long serialVersionUID = -257839817852907002L;
   private DefaultListModel<Ergebnis> model;
   private WettkampfBusiness business;

   /**
    * Create the panel.
    * @param wettkampfBusiness 
    */
   public WettkampfPanel(WettkampfBusiness wettkampfBusiness) {
      this.business = wettkampfBusiness;
      
      setLayout(new BorderLayout(0, 0));
      final ErgebnisListRenderer ergebnisListRenderer = new ErgebnisListRenderer(wettkampfBusiness);
      
      JPanel panel = new JPanel();
      add(panel, BorderLayout.NORTH);
      
      JButton btnNeuerStarter = new JButton("Neuer Starter");
      btnNeuerStarter.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            business.newStarter();
         }
      });
      panel.add(btnNeuerStarter);
      
      JLabel lblDurchgang = new JLabel("Durchgang");
      panel.add(lblDurchgang);
      
      final JComboBox<Durchgang> comboBox_1 = new JComboBox<>();
      comboBox_1.setModel(new DefaultComboBoxModel<Durchgang>(Durchgang.values()));
      comboBox_1.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            Durchgang durchgang = (Durchgang) comboBox_1.getSelectedItem();
            ergebnisListRenderer.setDurchgang(durchgang);
         }
      });
      panel.add(comboBox_1);

//      Durchgang durchgang = (Durchgang) comboBox_1.getSelectedItem();
//      ergebnisListRenderer.setDurchgang(durchgang);
//
//      ergebnisListRenderer.setDurchgang(Durchgang.KUER);
      
      JLabel lblSortierung = new JLabel("Sortierung");
      panel.add(lblSortierung);
      
      JComboBox<Sortierung> comboBox = new JComboBox<>();
      comboBox.setModel(new DefaultComboBoxModel<Sortierung>(Sortierung.values()));
      panel.add(comboBox);
      
      final JList<Ergebnis> list = new JList<>();
      list.setCellRenderer(ergebnisListRenderer);
      model = new DefaultListModel<>();
      list.setModel(model);
      list.addMouseListener(new MouseAdapter() {

         @Override
         public void mouseClicked(MouseEvent e) {
             int index = list.locationToIndex(e.getPoint());
             if (index > -1) {

                 Rectangle bounds = list.getCellBounds(index, index);
                 ErgebnisListRenderer cellRenderer = (ErgebnisListRenderer) list.getCellRenderer();
                 Component renderComp = cellRenderer.getListCellRendererComponent(list, list.getModel().getElementAt(index), index, false, false);
                 renderComp.setBounds(bounds);

                 Point local = new Point(e.getPoint());
                 local.x -= bounds.x;
                 local.y -= bounds.y;

                 cellRenderer.buttonClicked(renderComp);

             }
         }
      });
      add(list, BorderLayout.CENTER);
      
   }

   public void setWettkampf(Wettkampf wettkampf) {
      model.removeAllElements();
      for(Ergebnis e: wettkampf.getErgebnisse())
         model.addElement(e);
   }

}
