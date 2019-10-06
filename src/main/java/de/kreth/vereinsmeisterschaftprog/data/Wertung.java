package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.WertungCalculatorFactory;

public class Wertung implements Cloneable, PropertyChangeListener {

	public static final String ERGEBNIS_CHANGE_PROPERTY = Wertung.class.getName() + " Ergebnis geändert!";

	public static final String HALTUNG_CHANGE_PROPERTY = Wertung.class.getName() + " Kari geändert!";

	public static final String HD_CHANGE_PROPERTY = Wertung.class.getName() + " HD geändert!";

	public static final String DIFF_CHANGE_PROPERTY = Wertung.class.getName() + " Schwierigkeit geändert!";

	private final List<Value> werte = new ArrayList<>();

	private BigDecimal ergebnis = BigDecimal.ZERO;

	private final Durchgang durchgang;

	private PropertyChangeSupport pcs;

	private int id;

	public Wertung(int id, Durchgang durchgang) {
		this.id = id;
		this.durchgang = durchgang;
		this.pcs = new PropertyChangeSupport(this);
	}

	public int getId() {
		return id;
	}

	/**
	 * Unmodifiable!
	 * @return
	 */
	public List<Value> allValues() {
		return Collections.unmodifiableList(werte);
	}

	public List<Value> getByType(ValueType type) {
		return werte.stream().filter(v -> v.getType() == type).sorted(this::compare).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		List<Value> sorted = new ArrayList<>(werte);
		sorted.sort(this::compare);
		StringBuilder text = new StringBuilder();
		text.append(durchgang).append(" [");
		for (Value v : sorted) {
			if (!sorted.get(0).equals(v)) {
				text.append(",");
			}
			text.append(v);
		}
		text.append("] =>").append(ergebnis);
		return text.toString();
	}

	private int compare(Value v1, Value v2) {
		int compare = Integer.compare(v1.getType().ordinal(), v2.getType().ordinal());
		if (compare == 0) {
			compare = Integer.compare(v1.getIndex(), v2.getIndex());
		}
		return compare;
	}

	@Override
	public Wertung clone() {
		Wertung clone = new Wertung(id, durchgang);
		return clone(clone);
	}

	protected Wertung clone(Wertung clone) {
		werte.forEach(e -> clone.werte.add(e.clone()));
		clone.ergebnis = ergebnis;

		clone.werte.forEach(wert -> wert.addPropertyChangeListener(ev -> clone.calculate()));
		return clone;
	}

	private void calculate() {
		BigDecimal oldErgebnis = ergebnis;
		ergebnis = WertungCalculatorFactory.calculate(this);
		pcs.firePropertyChange(new PropertyChangeEvent(this, ERGEBNIS_CHANGE_PROPERTY, oldErgebnis, ergebnis));
	}

	public BigDecimal getErgebnis() {
		return ergebnis;
	}

	public Durchgang getDurchgang() {
		return durchgang;
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
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

	public Value get(ValueType type, int index) {
		for (Value v : this.werte) {
			if (type.equals(v.getType()) && index == v.getIndex()) {
				return v;
			}
		}
		throw new IllegalArgumentException("There is no Kari of type " + type + " and index " + index);
	}

	void setValues(List<Value> valueList) {
		werte.forEach(w -> w.removePropertyChangeListener(this));
		this.werte.clear();
		if (valueList.isEmpty()) {
			return;
		}
		List<String> identifiers = valueList.stream().map(v -> v.identifier()).collect(Collectors.toList());
		int size = new HashSet<>(identifiers).size();
		if (size < valueList.size()) {
			Collections.sort(identifiers);
			throw new IllegalStateException("Doublicate Values in " + toString() + ": " + identifiers);
		}
		this.werte.addAll(valueList);
		this.werte.forEach(w -> w.addPropertyChangeListener(this));
		if (Durchgang.KUER.equals(durchgang)) {
			boolean hasDiff = false;
			for (Value v : werte) {
				if (ValueType.SCHWIERIGKEIT.equals(v.getType())) {
					hasDiff = true;
				}
			}
			if (hasDiff == false) {
				throw new IllegalStateException("Kuer must have SCHWIERIGKEIT");
			}
		}
		calculate();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(evt);
		calculate();
	}
}
