package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.beans.PropertyChangeListener;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;


public interface PlatzCalculator {
   int getPlatzFor(Ergebnis ergebnis);

   /**
    * Wird ausgelöst, wenn sich Plätze geändert haben (möglicherweise dieser)
    * @param listener
    */
   public void addPropertyChangeListener(PropertyChangeListener listener);

   public void removePropertyChangeListener(PropertyChangeListener listener);

}
