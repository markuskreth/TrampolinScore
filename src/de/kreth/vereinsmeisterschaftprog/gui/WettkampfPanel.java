package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.*;

public class WettkampfPanel extends JPanel {

   private static final long serialVersionUID = -257839817852907002L;

   private WettkampfBusiness business;
   private JTable table;
   private MyTableModel tableModel;
   final JComboBox<Durchgang> comboBox_Durchgang = new JComboBox<>();

   /**
    * Create the panel.
    * 
    * @param wettkampfBusiness
    */
   public WettkampfPanel(final WettkampfBusiness wettkampfBusiness) {
      this.business = wettkampfBusiness;
      
      setLayout(new BorderLayout(0, 0));
      
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
      
      comboBox_Durchgang.setModel(new DefaultComboBoxModel<Durchgang>(Durchgang.values()));
      panel.add(comboBox_Durchgang);

      JLabel lblSortierung = new JLabel("Sortierung");
      panel.add(lblSortierung);
      
      final JComboBox<Sortierung> comboBox = new JComboBox<>();
      comboBox.setModel(new DefaultComboBoxModel<Sortierung>(Sortierung.values()));
      comboBox.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            Sortierung sort = (Sortierung) comboBox.getSelectedItem();
            tableModel.setSortierung(sort );
         }
      });
      
      panel.add(comboBox);
      tableModel = new MyTableModel();
      table = new JTable();
      table.setModel(tableModel);
      
      TableColumnModel columnModel = table.getColumnModel();
      TableColumn btnColumn = columnModel.getColumn(5);
      int width = 50;
      for(int i=0; i<columnModel.getColumnCount(); i++) {
         if(i==0)
            columnModel.getColumn(i).setPreferredWidth(width*3);
         else if(i==5)
            columnModel.getColumn(i).setPreferredWidth((int) (width*1.5));
         else
            columnModel.getColumn(i).setPreferredWidth(width);
      }
      btnColumn.setCellRenderer(new ButtonRenderer());
      btnColumn.setCellEditor(new ButtonEditor());
      JScrollPane scroller = new JScrollPane(table);
      add(scroller, BorderLayout.CENTER);
      
      
   }

   public void setWettkampf(Wettkampf wettkampf) {

      tableModel.removeAllElements();
      for (Ergebnis e : wettkampf.getErgebnisse())
         tableModel.addElement(e);
   }

   private class ButtonRenderer implements TableCellRenderer {

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         JButton btn = (JButton) value;
         if (isSelected) {
            btn.setForeground(table.getSelectionForeground());
            btn.setBackground(table.getSelectionBackground());
         } else {
            btn.setForeground(table.getForeground());
            btn.setBackground(UIManager.getColor("Button.background"));
         }
         return btn;
      }

   }

   private class ButtonEditor extends AbstractCellEditor implements ActionListener, TableCellEditor {
      
      private static final long serialVersionUID = -3269232034263059218L;
      private JButton button;

      public ButtonEditor() {

         button = new JButton();
         button.addActionListener(this);
         button.setBorderPainted(false);
      }
      
      @Override
      public Object getCellEditorValue() {
         return null;
      }

      @Override
      public void actionPerformed(ActionEvent ev) {
         fireEditingStopped();
      }

      @Override
      public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
         return (JButton) value;
      }
      
   }
   
   private class MyTableModel extends AbstractTableModel {

      private static final long serialVersionUID = 2910124315519583475L;
      private String[] columnNames = { "Starter", "Pflicht", "Kür", "Gesamt", "Platz", "" };
      private List<Ergebnis> data = new ArrayList<>();
      private List<JButton> editButtons = new ArrayList<>();

      DecimalFormat df = new DecimalFormat("0.0##");

      private SortierungComperator comperator = new SortierungComperator();

      public void setSortierung(Sortierung sort) {
         comperator.setSortierung(sort);
         Collections.sort(data, comperator);
         fireTableDataChanged();
      }

      public void addElement(final Ergebnis e) {
         data.add(e);
         e.addPropertyChangeListener(new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
               Collections.sort(data, comperator);
               fireTableDataChanged();
            }
         });
         JButton b = new JButton("Werten");
         b.addActionListener(new ActionListener() {
            int index = data.size()-1;
            @Override
            public void actionPerformed(ActionEvent ev) {
               business.werteErgebnis(data.get(index), (Durchgang) comboBox_Durchgang.getSelectedItem());
            }
         });
         editButtons.add(b);
         fireTableDataChanged();
      }

      public void removeAllElements() {
         data.clear();
         editButtons.clear();
         fireTableDataChanged();
      }

      @Override
      public String getColumnName(int column) {
         return columnNames[column];
      }

      @Override
      public int getRowCount() {
         return data.size();
      }

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
         if(columnIndex==5)
            return true;
         return false;
      }

      @Override
      public int getColumnCount() {
         return columnNames.length;
      }

      @Override
      public Class<?> getColumnClass(int columnIndex) {
         if (columnIndex == 5)
            return JComponent.class;

         return String.class;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
         Ergebnis ergebnis = data.get(rowIndex);
         Object value;
         switch (columnIndex) {
            case 0:
               value = ergebnis.getStarterName();
               break;

            case 1:
               value = df.format(ergebnis.getPflicht().getErgebnis());
               break;

            case 2:
               value = df.format(ergebnis.getKuer().getErgebnis());
               break;

            case 3:
               value = df.format(ergebnis.getErgebnis());
               break;
            case 4:
               value = ergebnis.getPlatz() + ".";
               break;
            case 5:
               value = editButtons.get(rowIndex);
               break;
            default:
               value = "";
               break;
         }

         return value;
      }
   }

   private class SortierungComperator implements Comparator<Ergebnis> {

      private Sortierung sort = Sortierung.Nach_Startreihenfolge;

      @Override
      public int compare(Ergebnis o1, Ergebnis o2) {
         switch (sort) {
            case Nach_Ergebnis:
               int compare = Double.compare(o1.getPlatz(), o2.getPlatz());
               return compare == 0 ? Integer.compare(o1.getId(), o2.getId()) : compare;
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
