package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.PlatzCalculator;


public class Wettkampf implements PlatzCalculator, PropertyChangeListener {

   public static final String PLAETZE_CHANGE_PROPERTY = Wettkampf.class.getName() + " Plätze geändert!";

   private String name;
   private Pflichten filter;
   private List<Ergebnis> ergebnisse;
   private Map<Ergebnis, Integer> plaetze;
   private PropertyChangeSupport pcs;
   
   public Wettkampf(String name, Pflichten filter) {
      super();
      this.name = name;
      this.filter = filter;
      ergebnisse = new ArrayList<>();
      plaetze = new HashMap<>();
      this.pcs = new PropertyChangeSupport(this);
   }

   public String getName() {
      return name;
   }
   
   public Pflichten getFilter() {
      return filter;
   }
      
   public int size() {
      return ergebnisse.size();
   }

   
   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener) {
      pcs.addPropertyChangeListener(listener);
   }

   @Override
   public void removePropertyChangeListener(PropertyChangeListener listener) {
      pcs.removePropertyChangeListener(listener);
   }

   public boolean add(Ergebnis e) {
      e.addPropertyChangeListener(this);
      boolean add = ergebnisse.add(e);
      propertyChange(null);
      
      return add;
   }

   public boolean remove(Object o) {
      return ergebnisse.remove(o);
   }

   public Collection<Ergebnis> getErgebnisse() {
      return new ArrayList<>(ergebnisse);
   }

   @Override
   public int getPlatzFor(Ergebnis ergebnis) {
      return plaetze.get(ergebnis).intValue();
   }
   
   private ErgebnisComperator comp;
   private class ErgebnisComperator implements Comparator<Ergebnis> {

      @Override
      public int compare(Ergebnis o1, Ergebnis o2) {
         return Double.compare(o2.getErgebnis(), o1.getErgebnis());
      }
      
   }
   @Override
   public void propertyChange(PropertyChangeEvent evt) {

      if(comp == null)
         comp = new ErgebnisComperator();
      
      plaetze.clear();
      
      Collections.sort(ergebnisse, comp);
      int platz = 0;
      Ergebnis prev = null;
      for(Ergebnis e: ergebnisse){
         if(prev == null || prev.getErgebnis()!=e.getErgebnis())
            platz++;
         plaetze.put(e, Integer.valueOf(platz));
         prev = e;
      }
      pcs.firePropertyChange(PLAETZE_CHANGE_PROPERTY, true, false);
   }
}
