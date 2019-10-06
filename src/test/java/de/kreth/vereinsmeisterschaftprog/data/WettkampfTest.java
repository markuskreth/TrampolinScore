package de.kreth.vereinsmeisterschaftprog.data;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WettkampfTest {

	private WertungFactory wf;

	@Before
	public void setup() {
		wf = new WertungFactory();
		wf.setAnzahlHaltung(4);
		wf.setAnzahlHd(2);
	}

	@Test
	public void test() {
		Wettkampf w = new Wettkampf("Test", new Gruppe(1, "P2", "Gruppe P2"));

		Ergebnis m = new Ergebnis(1, "Markus", w, 1,
				Arrays.asList(new Wertung(1, Durchgang.PFLICHT), new Wertung(2, Durchgang.KUER)));
		w.add(m);
		Ergebnis j = new Ergebnis(2, "Jasmin", w, 2,
				Arrays.asList(new Wertung(3, Durchgang.PFLICHT), new Wertung(4, Durchgang.KUER)));
		w.add(j);
		Ergebnis k = new Ergebnis(3, "Kira", w, 3,
				Arrays.asList(new Wertung(5, Durchgang.PFLICHT), new Wertung(6, Durchgang.KUER)));
		w.add(k);

		wf.setupWertungen(w);

		Wertung pflicht = m.getWertungen().get(0);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.0);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.0);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.0);

		pflicht = j.getWertungen().get(0);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.1);

		pflicht = k.getWertungen().get(0);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.2);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.2);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.2);

		assertEquals(1, k.getPlatz());
		assertEquals(2, j.getPlatz());
		assertEquals(3, m.getPlatz());
	}

}
