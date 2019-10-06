package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.kreth.vereinsmeisterschaftprog.data.calculatoren.PlatzCalculator;

public class Ergebnis {

	public static final String ERGEBNIS_CHANGE_PROPERTY = Ergebnis.class.getName() + " ERGEBNIS geändert!";

	public static final String PLATZ_CHANGE_PROPERTY = Ergebnis.class.getName() + " Platz geändert!";

	public static final String STARTERNAME_CHANGE_PROPERTY = Ergebnis.class.getName() + " Starter Name geändert!";

	private final PropertyChangeSupport pcs;

	private final List<Wertung> wertungen = new ArrayList<>();

	private final int id;

	private final int random;

	private String starterName;

	private BigDecimal ergebnis;

	private int platz;

	private PlatzCalculator calc;

	public Ergebnis(int id, String starterName, PlatzCalculator calc, int random, List<Wertung> wertungen) {

		this.calc = calc;
		this.random = random;
		this.id = id;
		this.starterName = starterName;
		pcs = new PropertyChangeSupport(this);

		ergebnis = BigDecimal.ZERO;
		boolean existsPflicht = false;
		boolean existsKuer = false;
		for (Wertung w : wertungen) {
			if (Durchgang.PFLICHT.equals(w.getDurchgang())) {
				existsPflicht = true;
			}
			else if (Durchgang.KUER.equals(w.getDurchgang())) {
				existsKuer = true;
			}
		}
		if (!existsPflicht) {
			throw new IllegalArgumentException("No Pflichtuebung given!");
		}
		if (!existsKuer) {
			throw new IllegalArgumentException("No Kueruebung given!");
		}

		PflichtORKuerErgebnisChangeListener l = new PflichtORKuerErgebnisChangeListener();
		this.wertungen.addAll(wertungen);
		this.wertungen.forEach(w -> w.addPropertyChangeListener(Wertung.ERGEBNIS_CHANGE_PROPERTY, l));

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

	public List<Wertung> getWertungen() {
		return wertungen;
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

	public int compareForStartreihenfolge(Ergebnis other) {
		return Integer.compare(random, other.random);
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
			Optional<BigDecimal> result = wertungen.stream().map(w -> w.getErgebnis()).reduce(BigDecimal::add);
			if (result.isPresent()) {
				BigDecimal oldValue = ergebnis;
				ergebnis = result.get();
				pcs.firePropertyChange(ERGEBNIS_CHANGE_PROPERTY, oldValue, ergebnis);
			}
		}
	}

	public void setStarterName(String starterName2) {
		String oldValue = this.starterName;
		this.starterName = starterName2;
		pcs.firePropertyChange(STARTERNAME_CHANGE_PROPERTY, oldValue, starterName2);
	}

}
