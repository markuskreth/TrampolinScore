package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;


public class WettkampfPanel extends JPanel {

   private static final long serialVersionUID = -257839817852907002L;
   private SortableListModlel model;
   private WettkampfBusiness business;

   /**
    * Create the panel.
    * @param wettkampfBusiness 
    */
   public WettkampfPanel(final WettkampfBusiness wettkampfBusiness) {
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
      
      final JComboBox<Sortierung> comboBox = new JComboBox<>();
      comboBox.setModel(new DefaultComboBoxModel<Sortierung>(Sortierung.values()));
      comboBox.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            Sortierung sort = (Sortierung) comboBox.getSelectedItem();
            model.setSortierung(sort );
         }
      });
      panel.add(comboBox);
      
      final JList<Ergebnis> list = new JList<>();
      list.setCellRenderer(ergebnisListRenderer);
      model = new SortableListModlel();
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

   private class SortableListModlel extends AbstractListModel<Ergebnis> {

      private static final long serialVersionUID = 2910124315519583475L;
      private List<Ergebnis> data = new ArrayList<>();
      private SortierungComperator comperator = new SortierungComperator();
      
      @Override
      public int getSize() {
         return data.size();
      }

      public void addElement(Ergebnis e) {
         int startIndex = data.size()-1;
         
         if(startIndex<0)
            startIndex = 0;
         
         data.add(e);
         Collections.sort(data, comperator);
         fireIntervalAdded(this, startIndex, startIndex+1);
      }

      public void removeAllElements() {
         
         if( ! data.isEmpty()) {
            int index1 = data.size()-1;
            data.clear();
            fireIntervalRemoved(this, 0, index1);
         }
         
      }

      @Override
      public Ergebnis getElementAt(int index) {
         return data.get(index);
      }
      
      public void setSortierung(Sortierung sort) {
         comperator.setSortierung(sort);
         Collections.sort(data, comperator);            
         fireContentsChanged(this, 0, data.size() - 1);
      }
      
      private class SortierungComperator implements Comparator<Ergebnis> {

         private Sortierung sort = Sortierung.Nach_Startreihenfolge;

         @Override
         public int compare(Ergebnis o1, Ergebnis o2) {
            switch (sort) {
               case Nach_Ergebnis:
                  int compare = Double.compare(o1.getPlatz(), o2.getPlatz());
                  return compare == 0?Integer.compare(o1.getId(), o2.getId()):compare;
               case Nach_Startreihenfolge:
                  return Integer.compare(o1.getId(), o2.getId());
               default:
                  break;
            }
            return 0;
         }

         public void setSortierung(Sortierung sort) {
            this.sort = sort;
         }
         
      }
   }
}
