package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.WertungCalculatorFactory;


public class Wertung implements Cloneable {

   public static final String ERGEBNIS_CHANGE_PROPERTY = Wertung.class.getName() + " ERgebnis geändert!";
   public static final String KARI1_CHANGE_PROPERTY =    Wertung.class.getName() + " Kari1 geändert!";
   public static final String KARI2_CHANGE_PROPERTY =    Wertung.class.getName() + " Kari2 geändert!";
   public static final String KARI3_CHANGE_PROPERTY =    Wertung.class.getName() + " Kari3 geändert!";
   public static final String KARI4_CHANGE_PROPERTY =    Wertung.class.getName() + " Kari4 geändert!";
   public static final String KARI5_CHANGE_PROPERTY =    Wertung.class.getName() + " Kari5 geändert!";
   public static final String DIFF_CHANGE_PROPERTY =     Wertung.class.getName() + " Schwierigkeit geändert!";
   
   private double kari1 = 0;
   private double kari2 = 0;
   private double kari3 = 0;
   private double kari4 = 0;
   private double kari5 = 0;
   private double schwierigkeit = 0;
   private double ergebnis = 0;
   private Durchgang durchgang;
   private PropertyChangeSupport pcs;
   private int id;
   
   @Override
   public Wertung clone() {
      Wertung clone = new Wertung(id, durchgang);
      clone.kari1 = kari1;
      clone.kari2 = kari2;
      clone.kari3 = kari3;
      clone.kari4 = kari4;
      clone.kari5 = kari5;

      clone.schwierigkeit = schwierigkeit;
      clone.ergebnis = ergebnis;
      
      return clone;
   }
   
   public Wertung(int id, Durchgang durchgang) {
      this.id = id;
      this.durchgang = durchgang;
      this.pcs = new PropertyChangeSupport(this);
   }
   
   public int getId() {
      return id;
   }
   
   public double getKari1() {
      return kari1;
   }
   
   public void setKari1(double kari1) {
      Double OldValue = Double.valueOf(this.kari1);
      this.kari1 = kari1;
      
      calculate();

      pcs.firePropertyChange(KARI1_CHANGE_PROPERTY, OldValue, Double.valueOf(this.kari1));
   }
   
   public double getKari2() {
      return kari2;
   }
   
   public void setKari2(double kari2) {
      Double OldValue = Double.valueOf(this.kari2);
      this.kari2 = kari2;
      calculate();

      pcs.firePropertyChange(KARI2_CHANGE_PROPERTY, OldValue, Double.valueOf(this.kari2));
   }
   
   public double getKari3() {
      return kari3;
   }
   
   public void setKari3(double kari3) {
      Double OldValue = Double.valueOf(this.kari3);
      this.kari3 = kari3;
      calculate();

      pcs.firePropertyChange(KARI3_CHANGE_PROPERTY, OldValue, Double.valueOf(this.kari3));
   }
   
   public double getKari4() {
      return kari4;
   }
   
   public void setKari4(double kari4) {
      Double OldValue = Double.valueOf(this.kari4);
      this.kari4 = kari4;
      calculate();

      pcs.firePropertyChange(KARI4_CHANGE_PROPERTY, OldValue, Double.valueOf(this.kari4));
   }
   
   public double getKari5() {
      return kari5;
   }
   
   public void setKari5(double kari5) {
      Double OldValue = Double.valueOf(this.kari5);
      this.kari5 = kari5;
      calculate();

      pcs.firePropertyChange(KARI5_CHANGE_PROPERTY, OldValue, Double.valueOf(this.kari5));
   }
   
   public double getSchwierigkeit() {
      return schwierigkeit;
   }
   
   public void setSchwierigkeit(double schwierigkeit) {
      Double OldValue = Double.valueOf(this.schwierigkeit);
      this.schwierigkeit = schwierigkeit;
      calculate();

      pcs.firePropertyChange(DIFF_CHANGE_PROPERTY, OldValue, Double.valueOf(this.schwierigkeit));
   }
   

   private void calculate() {
      Double oldErgebnis = Double.valueOf(ergebnis);
      ergebnis = WertungCalculatorFactory.calculate(this);
      pcs.firePropertyChange(ERGEBNIS_CHANGE_PROPERTY, oldErgebnis, Double.valueOf(ergebnis));
   }

   public double getErgebnis() {
      return ergebnis;
   }
   
   public Durchgang getDurchgang() {
      return durchgang;
   }

   
   /**
    * PropertyName ist der Wert von {@link #getDurchgang()}
    * @param listener
    */
   public void addPropertyChangeListener(PropertyChangeListener listener) {
      pcs.addPropertyChangeListener(listener);
   }
   

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      pcs.removePropertyChangeListener(listener);
   }
   
}
