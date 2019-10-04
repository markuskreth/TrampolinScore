package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.PlatzCalculator;

public class Wettkampf implements PlatzCalculator, PropertyChangeListener {

	public static final String PLAETZE_CHANGE_PROPERTY = Wettkampf.class.getName() + " Plätze geändert!";

	private String name;

	private Gruppe gruppe;

	private List<Ergebnis> ergebnisse;

	private Map<Ergebnis, Integer> plaetze;

	private PropertyChangeSupport pcs;

	public Wettkampf(String name, Gruppe gruppe) {
		super();
		this.name = name;
		this.gruppe = gruppe;
		ergebnisse = new ArrayList<>();
		plaetze = new HashMap<>();
		this.pcs = new PropertyChangeSupport(this);
	}

	public String getName() {
		return name;
	}

	public Gruppe getGruppe() {
		return gruppe;
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
			return o2.getErgebnis().compareTo(o1.getErgebnis());
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (comp == null)
			comp = new ErgebnisComperator();

		plaetze.clear();

		Collections.sort(ergebnisse, comp);
		int platz = 0;
		Ergebnis prev = null;
		for (Ergebnis e : ergebnisse) {
			if (prev == null || prev.getErgebnis() != e.getErgebnis())
				platz++;
			plaetze.put(e, Integer.valueOf(platz));
			prev = e;
		}
		pcs.firePropertyChange(PLAETZE_CHANGE_PROPERTY, true, false);
	}
}
