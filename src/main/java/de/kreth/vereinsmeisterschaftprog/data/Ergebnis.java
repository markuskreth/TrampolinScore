package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.PlatzCalculator;

public class Ergebnis {

	public static final String ERGEBNIS_CHANGE_PROPERTY = Ergebnis.class.getName() + " ERGEBNIS geändert!";

	public static final String PLATZ_CHANGE_PROPERTY = Ergebnis.class.getName() + " Platz geändert!";

	public static final String STARTERNAME_CHANGE_PROPERTY = Ergebnis.class.getName() + " Starter Name geändert!";

	private int id;

	private String starterName;

	private Wertung pflicht;

	private Wertung kuer;

	private BigDecimal ergebnis;

	private int platz;

	private PlatzCalculator calc;

	PropertyChangeSupport pcs;

	public Ergebnis(int id, String starterName, PlatzCalculator calc, Wertung pflicht, Wertung kuer) {

		this.calc = calc;
		this.id = id;
		this.starterName = starterName;
		pcs = new PropertyChangeSupport(this);

		ergebnis = BigDecimal.ZERO;
		Wertung tmp;
		if (Durchgang.PFLICHT.equals(pflicht.getDurchgang())) {
			tmp = pflicht;
		}
		else if (Durchgang.PFLICHT.equals(kuer.getDurchgang())) {
			tmp = kuer;
		}
		else {
			throw new IllegalArgumentException("No Pflichtuebung given!");
		}
		this.pflicht = tmp;

		if (Durchgang.KUER.equals(pflicht.getDurchgang())) {
			tmp = pflicht;
		}
		else if (Durchgang.KUER.equals(kuer.getDurchgang())) {
			tmp = kuer;
		}
		else {
			throw new IllegalArgumentException("No Kueruebung given!");
		}
		this.kuer = tmp;

		PflichtORKuerErgebnisChangeListener l = new PflichtORKuerErgebnisChangeListener();
		pflicht.addPropertyChangeListener(Wertung.ERGEBNIS_CHANGE_PROPERTY, l);
		kuer.addPropertyChangeListener(Wertung.ERGEBNIS_CHANGE_PROPERTY, l);
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

	public BigDecimal getErgebnis() {
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
			BigDecimal oldValue = ergebnis;
			ergebnis = pflicht.getErgebnis().add(kuer.getErgebnis());
			pcs.firePropertyChange(ERGEBNIS_CHANGE_PROPERTY, oldValue, ergebnis);
		}
	}

	public void setStarterName(String starterName2) {
		String oldValue = this.starterName;
		this.starterName = starterName2;
		pcs.firePropertyChange(STARTERNAME_CHANGE_PROPERTY, oldValue, starterName2);
	}

}
