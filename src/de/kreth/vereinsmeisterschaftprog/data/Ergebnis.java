package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.PlatzCalculator;


public class Ergebnis {

   public static final String ERGEBNIS_CHANGE_PROPERTY = Ergebnis.class.getName() + " ERGEBNIS geändert!";
   public static final String PLATZ_CHANGE_PROPERTY = Ergebnis.class.getName() + " Platz geändert!";
   public static final String STARTERNAME_CHANGE_PROPERTY = Ergebnis.class.getName() + " Starter Name geändert!";
   
   private int id;
   private String starterName;
   private Wertung pflicht;
   private Wertung kuer;
   private double ergebnis;
   private int platz;
   private PlatzCalculator calc;
   PropertyChangeSupport pcs;
   
   public Ergebnis(int id, String starterName, PlatzCalculator calc, Wertung kuer, Wertung pflicht) {
      
      this.calc = calc;
      this.id = id;
      this.starterName = starterName;
      pcs = new PropertyChangeSupport(this);
      
      ergebnis = 0;
      this.pflicht = pflicht;
      this.kuer = kuer;
      
      PflichtORKuerErgebnisChangeListener l = new PflichtORKuerErgebnisChangeListener();
      pflicht.addPropertyChangeListener(l);
      kuer.addPropertyChangeListener(l);
      calc.addPropertyChangeListener(new PropertyChangeListener() {
         
         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            refreshPlatz();
         }
      });
      l.propertyChange(null);
   }
   
   
   public int getId() {
      return id;
   }
   
   public String getStarterName() {
      return starterName;
   }
   
   public Wertung getPflicht() {
      return pflicht;
   }
   
   public Wertung getKuer() {
      return kuer;
   }
   
   public PlatzCalculator getCalc() {
      return calc;
   }
   
   public double getErgebnis() {
      return ergebnis;
   }
   
   public int getPlatz() {
      return platz;
   }
   
   public void addPropertyChangeListener(PropertyChangeListener listener) {
      pcs.addPropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      pcs.removePropertyChangeListener(listener);
   }

   @Override
   public String toString() {
      return starterName + "=" + ergebnis;
   }
   
   private void refreshPlatz() {
      Integer oldValue = Integer.valueOf(platz);      
      platz = calc.getPlatzFor(Ergebnis.this);      
      pcs.firePropertyChange(PLATZ_CHANGE_PROPERTY, oldValue, Integer.valueOf(platz));
   }
   
   private class PflichtORKuerErgebnisChangeListener implements PropertyChangeListener {
   
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
         Double oldValue = Double.valueOf(ergebnis);
         ergebnis = pflicht.getErgebnis() + kuer.getErgebnis();
         pcs.firePropertyChange(ERGEBNIS_CHANGE_PROPERTY, oldValue, Double.valueOf(ergebnis));
      }
   }

   public void setStarterName(String starterName2) {
      String oldValue = this.starterName;
      this.starterName = starterName2;
      pcs.firePropertyChange(STARTERNAME_CHANGE_PROPERTY, oldValue, starterName2);
   }
   
}
