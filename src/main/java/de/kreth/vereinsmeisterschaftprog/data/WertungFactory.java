package de.kreth.vereinsmeisterschaftprog.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WertungFactory {

	private int anzahlHaltung = 4;

	private int anzahlHd = 2;

	public WertungFactory() {
	}

	public synchronized void setupWertungen(Wettkampf wettkampf) {
		Collection<Ergebnis> ergebnisse = wettkampf.getErgebnisse();
		for (Ergebnis ergebnis : ergebnisse) {
			if (ergebnis.getErgebnis().doubleValue() > 0.0) {
				throw new IllegalStateException("No Ergebnis must have values");
			}
			ergebnis.getWertungen().forEach(w -> setup(w));
		}
	}

	public synchronized void setup(Wertung wertung) {
		final List<Value> values = new ArrayList<>();

		if (Durchgang.KUER.equals(wertung.getDurchgang()) || Durchgang.PFLICHT.equals(wertung.getDurchgang())) {

			for (int i = 0; i < anzahlHaltung; i++) {
				Value val = new Value(ValueType.HALTUNG, 3, i);
				values.add(val);
			}

			for (int i = 0; i < anzahlHd; i++) {
				values.add(new Value(ValueType.HD, 3, i));
			}

			if (Durchgang.KUER.equals(wertung.getDurchgang())) {
				values.add(new Value(ValueType.SCHWIERIGKEIT, 1));
			}
		}
		else if (Durchgang.DREISPRUNG.equals(wertung.getDurchgang())) {
			values.add(new Value(ValueType.DREISPRUNG, 2));
		}
		else if (Durchgang.SEILSPRINGEN.equals(wertung.getDurchgang())) {
			values.add(new Value(ValueType.SEILSPRINGEN, 0));
		}

		wertung.setValues(values);
	}

	public synchronized void setAnzahlHaltung(int anzahlHaltung) {
		this.anzahlHaltung = anzahlHaltung;
	}

	public synchronized void setAnzahlHd(int anzahlHd) {
		this.anzahlHd = anzahlHd;
	}

}
