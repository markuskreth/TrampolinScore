package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import de.kreth.vereinsmeisterschaftprog.business.InputConverter;
import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertenDialog extends JDialog implements PropertyChangeListener {

   private static final long serialVersionUID = -878644536065558566L;
   private final JPanel contentPanel = new JPanel();

   private InputConverter converter = new InputConverter();
   
   private JFormattedTextField txtKari1;
   private JFormattedTextField txtKari2;
   private JFormattedTextField txtKari3;
   private JFormattedTextField txtKari4;
   private JFormattedTextField txtKari5;
   private JFormattedTextField txtDiff;
   private JLabel lblStarter;
   private String starterName;
   private Wertung wertung;
   private Wertung dummy;
   private JLabel lblErgebnis;

   /**
    * Create the dialog.
    */
   public WertenDialog(final WettkampfBusiness business) {

      setDefaultCloseOperation(HIDE_ON_CLOSE);

      DecimalFormat df = new DecimalFormat("0.0#");

      setBounds(100, 100, 655, 214);
      getContentPane().setLayout(new BorderLayout());
      contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(new BorderLayout(0, 0));
      {
         JPanel panel = new JPanel();
         contentPanel.add(panel, BorderLayout.NORTH);
         panel.setLayout(new BorderLayout(0, 0));
         {
            lblStarter = new JLabel();
            lblStarter.setPreferredSize(new Dimension(50, 10));
            panel.add(lblStarter);
            lblStarter.setFont(new Font("Dialog", Font.BOLD, 17));
         }
         {
            JButton btnNamendern = new JButton("Name Ändern");
            btnNamendern.addActionListener(new ActionListener() {
               
               @Override
               public void actionPerformed(ActionEvent e) {
                  business.changePersonName(starterName);
                  
               }
            });
            panel.add(btnNamendern, BorderLayout.EAST);
         }
      }
      {
         JPanel panel = new JPanel();
         contentPanel.add(panel, BorderLayout.CENTER);
         GridBagLayout gbl_panel = new GridBagLayout();
         gbl_panel.columnWidths = new int[]{91, 91, 91, 91, 91, 91, 91, 0};
         gbl_panel.rowHeights = new int[]{70, 70, 0, 0};
         gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
         gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
         panel.setLayout(gbl_panel);
         {
            JLabel lblNewLabel = new JLabel("Kari1");
            GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
            gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
            gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
            gbc_lblNewLabel.gridx = 0;
            gbc_lblNewLabel.gridy = 0;
            panel.add(lblNewLabel, gbc_lblNewLabel);
         }
         {
            JLabel lblKari = new JLabel("Kari2");
            GridBagConstraints gbc_lblKari = new GridBagConstraints();
            gbc_lblKari.fill = GridBagConstraints.BOTH;
            gbc_lblKari.insets = new Insets(0, 0, 5, 5);
            gbc_lblKari.gridx = 1;
            gbc_lblKari.gridy = 0;
            panel.add(lblKari, gbc_lblKari);
         }
         {
            JLabel lblKari_1 = new JLabel("Kari3");
            GridBagConstraints gbc_lblKari_1 = new GridBagConstraints();
            gbc_lblKari_1.fill = GridBagConstraints.BOTH;
            gbc_lblKari_1.insets = new Insets(0, 0, 5, 5);
            gbc_lblKari_1.gridx = 2;
            gbc_lblKari_1.gridy = 0;
            panel.add(lblKari_1, gbc_lblKari_1);
         }
         {
            JLabel lblKari_2 = new JLabel("Kari4");
            GridBagConstraints gbc_lblKari_2 = new GridBagConstraints();
            gbc_lblKari_2.fill = GridBagConstraints.BOTH;
            gbc_lblKari_2.insets = new Insets(0, 0, 5, 5);
            gbc_lblKari_2.gridx = 3;
            gbc_lblKari_2.gridy = 0;
            panel.add(lblKari_2, gbc_lblKari_2);
         }
         {
            JLabel lblKari_3 = new JLabel("Kari5");
            GridBagConstraints gbc_lblKari_3 = new GridBagConstraints();
            gbc_lblKari_3.fill = GridBagConstraints.BOTH;
            gbc_lblKari_3.insets = new Insets(0, 0, 5, 5);
            gbc_lblKari_3.gridx = 4;
            gbc_lblKari_3.gridy = 0;
            panel.add(lblKari_3, gbc_lblKari_3);
         }
         {
            JLabel lblSchwierigkeit = new JLabel("Schwierigkeit");
            GridBagConstraints gbc_lblSchwierigkeit = new GridBagConstraints();
            gbc_lblSchwierigkeit.fill = GridBagConstraints.BOTH;
            gbc_lblSchwierigkeit.insets = new Insets(0, 0, 5, 5);
            gbc_lblSchwierigkeit.gridx = 5;
            gbc_lblSchwierigkeit.gridy = 0;
            panel.add(lblSchwierigkeit, gbc_lblSchwierigkeit);
         }
         {
            JLabel lblErgebnis_2 = new JLabel("Ergebnis");
            GridBagConstraints gbc_lblErgebnis_2 = new GridBagConstraints();
            gbc_lblErgebnis_2.fill = GridBagConstraints.BOTH;
            gbc_lblErgebnis_2.insets = new Insets(0, 0, 5, 0);
            gbc_lblErgebnis_2.gridx = 6;
            gbc_lblErgebnis_2.gridy = 0;
            panel.add(lblErgebnis_2, gbc_lblErgebnis_2);
         }
         {
            txtKari1 = new JFormattedTextField(df);
            GridBagConstraints gbc_txtKari1 = new GridBagConstraints();
            gbc_txtKari1.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtKari1.insets = new Insets(0, 0, 5, 5);
            gbc_txtKari1.gridx = 0;
            gbc_txtKari1.gridy = 1;
            panel.add(txtKari1, gbc_txtKari1);
            txtKari1.setText("1");
            txtKari1.addFocusListener(new DecimalFocusListener(txtKari1, 1));
            txtKari1.setColumns(10);
         }
         {
            txtKari2 = new JFormattedTextField(df);
            txtKari2.setText("11");
            txtKari2.addFocusListener(new DecimalFocusListener(txtKari2,2));
            GridBagConstraints gbc_txtKari2 = new GridBagConstraints();
            gbc_txtKari2.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtKari2.insets = new Insets(0, 0, 5, 5);
            gbc_txtKari2.gridx = 1;
            gbc_txtKari2.gridy = 1;
            panel.add(txtKari2, gbc_txtKari2);
            txtKari2.setColumns(10);
         }
         {
            txtKari3 = new JFormattedTextField(df);
            txtKari3.addFocusListener(new DecimalFocusListener(txtKari3,3));
            GridBagConstraints gbc_txtKari3 = new GridBagConstraints();
            gbc_txtKari3.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtKari3.insets = new Insets(0, 0, 5, 5);
            gbc_txtKari3.gridx = 2;
            gbc_txtKari3.gridy = 1;
            panel.add(txtKari3, gbc_txtKari3);
            txtKari3.setColumns(10);
         }
         {
            txtKari4 = new JFormattedTextField(df);
            txtKari4.addFocusListener(new DecimalFocusListener(txtKari4,4));
            GridBagConstraints gbc_txtKari4 = new GridBagConstraints();
            gbc_txtKari4.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtKari4.insets = new Insets(0, 0, 5, 5);
            gbc_txtKari4.gridx = 3;
            gbc_txtKari4.gridy = 1;
            panel.add(txtKari4, gbc_txtKari4);
            txtKari4.setColumns(10);
         }
         {
            txtKari5 = new JFormattedTextField(df);
            txtKari5.addFocusListener(new DecimalFocusListener(txtKari5,5));
            GridBagConstraints gbc_txtKari5 = new GridBagConstraints();
            gbc_txtKari5.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtKari5.insets = new Insets(0, 0, 5, 5);
            gbc_txtKari5.gridx = 4;
            gbc_txtKari5.gridy = 1;
            panel.add(txtKari5, gbc_txtKari5);
            txtKari5.setColumns(10);
         }
         {
            txtDiff = new JFormattedTextField(df);
            txtDiff.addFocusListener(new DecimalFocusListener(txtDiff,7));
            GridBagConstraints gbc_txtDiff = new GridBagConstraints();
            gbc_txtDiff.fill = GridBagConstraints.HORIZONTAL;
            gbc_txtDiff.insets = new Insets(0, 0, 5, 5);
            gbc_txtDiff.gridx = 5;
            gbc_txtDiff.gridy = 1;
            panel.add(txtDiff, gbc_txtDiff);

         }
         {
            lblErgebnis = new JLabel("90,3");
            lblErgebnis.setHorizontalAlignment(SwingConstants.CENTER);
            lblErgebnis.setHorizontalTextPosition(SwingConstants.CENTER);
            GridBagConstraints gbc_lblErgebnis = new GridBagConstraints();
            gbc_lblErgebnis.insets = new Insets(0, 0, 5, 0);
            gbc_lblErgebnis.fill = GridBagConstraints.HORIZONTAL;
            gbc_lblErgebnis.gridx = 6;
            gbc_lblErgebnis.gridy = 1;
            panel.add(lblErgebnis, gbc_lblErgebnis);
         }
      }
      {
         JPanel buttonPane = new JPanel();
         buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
         getContentPane().add(buttonPane, BorderLayout.SOUTH);
         {
            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent e) {
                  setVisible(false);
               }
            });
            buttonPane.add(okButton);
            getRootPane().setDefaultButton(okButton);
         }
         {
            JButton cancelButton = new JButton("Abbrechen");
            cancelButton.addActionListener(new ActionListener() {

               @Override
               public void actionPerformed(ActionEvent e) {
                  wertung.setKari1(dummy.getKari1());
                  wertung.setKari2(dummy.getKari2());
                  wertung.setKari3(dummy.getKari3());
                  wertung.setKari4(dummy.getKari4());
                  wertung.setKari5(dummy.getKari5());
                  wertung.setSchwierigkeit(dummy.getSchwierigkeit());
                  setVisible(false);
               }
            });
            buttonPane.add(cancelButton);
         }
      }
      
      txtKari1.requestFocusInWindow();
   }

   private void markAsError(JTextField field) {
      field.setSelectionStart(0);
      field.setBackground(Color.ORANGE);
      field.setSelectionEnd(field.getText().length());
      field.requestFocus();
   }

   public void setWertung(String starterName, Wertung wertung) {
      this.starterName = starterName;
      
      setTitle(wertung.getDurchgang() + " " + starterName);
      
      this.wertung = wertung;
      updateView();
   }

   private void updateView() {
      
      this.dummy = wertung.clone();
      
      if (wertung.getDurchgang() == Durchgang.PFLICHT) {
         txtDiff.setEnabled(false);
      } else {
         txtDiff.setEnabled(true);
      }

      lblStarter.setText(starterName);

      if(wertung.getKari1()<=0)
         txtKari1.setText("");
      else
         txtKari1.setText(converter.format(wertung.getKari1()));

      if(wertung.getKari2()<=0)
         txtKari2.setText("");
      else
         txtKari2.setText(converter.format(wertung.getKari2()));

      if(wertung.getKari3()<=0)
         txtKari3.setText("");
      else
         txtKari3.setText(converter.format(wertung.getKari3()));
      
      if(wertung.getKari4()<=0)
         txtKari4.setText("");
      else
         txtKari4.setText(converter.format(wertung.getKari4()));
      if(wertung.getKari5()<=0)
         txtKari5.setText("");
      else
         txtKari5.setText(converter.format(wertung.getKari5()));

      if(wertung.getSchwierigkeit()<=0)
         txtDiff.setText("");
      else
         txtDiff.setText(converter.format(wertung.getSchwierigkeit()));

      if(wertung.getErgebnis()<=0)
         lblErgebnis.setText(converter.format(0));
      else
         lblErgebnis.setText(converter.format(wertung.getErgebnis()));
      
      txtKari1.requestFocus();
   }
   
   private class DecimalFocusListener extends FocusAdapter {
      
      private JTextField field;
      private int kari;

      public DecimalFocusListener(JTextField field, int kari) {
         
         this.field = field;
         this.kari = kari;
      }
      
      @Override
      public void focusLost(FocusEvent e) {
         
            try {
               
               double number = converter.convert(field.getText());
               field.setText(converter.format(number));
               
               switch (kari) {
                  case 1:
                     wertung.setKari1(number);
                     break;
                  case 2:
                     wertung.setKari2(number);
                     break;
                  case 3:
                     wertung.setKari3(number);
                     break;
                  case 4:
                     wertung.setKari4(number);
                     break;
                  case 5:
                     wertung.setKari5(number);
                     break;
                  case 6:
                  case 7:
                     wertung.setSchwierigkeit(number);
                     break;
                  default:
                     break;
               }
               
            } catch (ParseException e1) {
               markAsError(field);
               e1.printStackTrace();
            }
         
         
         lblErgebnis.setText(converter.format(wertung.getErgebnis()));
      }
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      if(evt.getPropertyName().matches(Ergebnis.STARTERNAME_CHANGE_PROPERTY)) {
         starterName = evt.getNewValue().toString();               
         lblStarter.setText(starterName);
      }
   }
}
