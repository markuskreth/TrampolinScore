package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.WertungCalculatorFactory;

public class Wertung implements Cloneable {

	public static final String ERGEBNIS_CHANGE_PROPERTY = Wertung.class.getName() + " ERgebnis geändert!";

	public static final String KARI1_CHANGE_PROPERTY = Wertung.class.getName() + " Kari1 geändert!";

	public static final String KARI2_CHANGE_PROPERTY = Wertung.class.getName() + " Kari2 geändert!";

	public static final String KARI3_CHANGE_PROPERTY = Wertung.class.getName() + " Kari3 geändert!";

	public static final String KARI4_CHANGE_PROPERTY = Wertung.class.getName() + " Kari4 geändert!";

	public static final String HD1_CHANGE_PROPERTY = Wertung.class.getName() + " HD1 geändert!";

	public static final String HD2_CHANGE_PROPERTY = Wertung.class.getName() + " HD2 geändert!";

	public static final String DIFF_CHANGE_PROPERTY = Wertung.class.getName() + " Schwierigkeit geändert!";

	private double kari1 = 0;

	private double kari2 = 0;

	private double kari3 = 0;

	private double kari4 = 0;

	private double hd1 = 0;

	private double hd2 = 0;

	private double schwierigkeit = 0;

	private double ergebnis = 0;

	private Durchgang durchgang;

	private PropertyChangeSupport pcs;

	private int id;

	@Override
	public String toString() {
		return durchgang + " kari1=" + kari1 + "; kari2=" + kari2 + "; kari3=" + kari3 + "; kari4=" + kari4 + "; hd1="
				+ hd1 + "; hd2="
				+ hd2 + "; diff=" + schwierigkeit + "=>" + ergebnis;
	}

	@Override
	public Wertung clone() {
		Wertung clone = new Wertung(id, durchgang);
		clone.kari1 = kari1;
		clone.kari2 = kari2;
		clone.kari3 = kari3;
		clone.kari4 = kari4;
		clone.hd1 = hd1;
		clone.hd2 = hd2;

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

	public double getHd1() {
		return hd1;
	}

	public void setHd1(double hd1) {

		Double OldValue = Double.valueOf(this.hd1);
		this.hd1 = hd1;
		calculate();

		pcs.firePropertyChange(HD1_CHANGE_PROPERTY, OldValue, Double.valueOf(this.hd1));
	}

	public double getHd2() {
		return hd2;
	}

	public void setHd2(double hd2) {

		Double OldValue = Double.valueOf(this.hd2);
		this.hd2 = hd2;
		calculate();

		pcs.firePropertyChange(HD2_CHANGE_PROPERTY, OldValue, Double.valueOf(this.hd2));
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

}
