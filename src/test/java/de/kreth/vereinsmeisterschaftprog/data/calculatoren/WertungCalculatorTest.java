package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;
import de.kreth.vereinsmeisterschaftprog.data.WertungFactory;

public class WertungCalculatorTest {

	private WertungCalculatorFor2ValuesHd calc2;

	private WertungCalculatorFor4ValuesHd calc4;

	private WertungCalculatorFor3ValuesHd calc3;

	private WertungFactory wf;

	@Before
	public void setUp() {
		calc2 = new WertungCalculatorFor2ValuesHd();
		calc4 = new WertungCalculatorFor4ValuesHd();
		calc3 = new WertungCalculatorFor3ValuesHd();

		wf = new WertungFactory();
		wf.setAnzahlHaltung(4);
		wf.setAnzahlHd(2);

	}

	@Test
	public void testKrummeWerte() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		wf.setup(pflicht);
		pflicht.get(ValueType.HALTUNG, 0).setValue(5.8);
		pflicht.get(ValueType.HALTUNG, 1).setValue(5.7);
		pflicht.get(ValueType.HALTUNG, 2).setValue(5.6);
		pflicht.get(ValueType.HALTUNG, 3).setValue(5.7);
		pflicht.get(ValueType.HD, 0).setValue(7.3);
		pflicht.get(ValueType.HD, 1).setValue(7.8);

		BigDecimal erg = calc4.calculate(pflicht);
		assertEquals(new BigDecimal("18.950"), erg);
	}

	@Test
	public void testCalculateCompleteJudges() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		wf.setup(pflicht);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.2);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.3);
		pflicht.get(ValueType.HALTUNG, 3).setValue(7.4);
		pflicht.get(ValueType.HD, 0).setValue(8.7);
		pflicht.get(ValueType.HD, 1).setValue(9.5);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		wf.setup(kuer);
		kuer.get(ValueType.HALTUNG, 0).setValue(7.1);
		kuer.get(ValueType.HALTUNG, 1).setValue(7.2);
		kuer.get(ValueType.HALTUNG, 2).setValue(7.3);
		kuer.get(ValueType.HALTUNG, 3).setValue(7.4);
		kuer.get(ValueType.HD, 0).setValue(8.7);
		kuer.get(ValueType.HD, 1).setValue(9.5);
		kuer.get(ValueType.SCHWIERIGKEIT, 0).setValue(20.5);

		BigDecimal erg = calc4.calculate(pflicht);
		assertEquals(new BigDecimal("23.600"), erg);

		erg = calc4.calculate(kuer);
		assertEquals(new BigDecimal("44.100"), erg);

		erg = WertungCalculatorFactory.calculate(kuer);
		assertEquals(new BigDecimal("44.100"), erg);
	}

	@Test
	public void testCalculate3er() {

		wf.setAnzahlHaltung(3);
		wf.setAnzahlHd(1);

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		wf.setup(pflicht);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.2);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.3);
		pflicht.get(ValueType.HD, 0).setValue(8.4);

		BigDecimal erg = calc3.calculate(pflicht);
		assertEquals(BigDecimal.valueOf(22.8).setScale(3), erg);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		wf.setup(kuer);
		kuer.get(ValueType.HALTUNG, 0).setValue(7.1);
		kuer.get(ValueType.HALTUNG, 1).setValue(7.2);
		kuer.get(ValueType.HALTUNG, 2).setValue(7.3);
		kuer.get(ValueType.HD, 0).setValue(9.3);
		kuer.get(ValueType.SCHWIERIGKEIT, 0).setValue(20.5);

		erg = calc3.calculate(kuer);
		assertEquals(BigDecimal.valueOf(44.2).setScale(3), erg);

		kuer.get(ValueType.HD, 0).setValue(8.3);
		erg = calc3.calculate(kuer);
		assertEquals(BigDecimal.valueOf(43.2).setScale(3), erg);

		pflicht = new Wertung(1, Durchgang.PFLICHT);
		wf.setup(pflicht);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.2);
		pflicht.get(ValueType.HALTUNG, 2).setValue(7.7);
		pflicht.get(ValueType.HD, 0).setValue(8.4);

		erg = calc3.calculate(pflicht);
		assertEquals(BigDecimal.valueOf(23.067), erg);

		erg = WertungCalculatorFactory.calculate(pflicht);
		assertEquals(BigDecimal.valueOf(23.067), erg);

	}

	@Test
	public void testCalculate2er() {

		wf.setAnzahlHaltung(2);
		wf.setAnzahlHd(1);

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		wf.setup(pflicht);
		pflicht.get(ValueType.HALTUNG, 0).setValue(7.1);
		pflicht.get(ValueType.HALTUNG, 1).setValue(7.4);
		pflicht.get(ValueType.HD, 0).setValue(8.7);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		wf.setup(kuer);
		kuer.get(ValueType.HALTUNG, 0).setValue(7.1);
		kuer.get(ValueType.HALTUNG, 1).setValue(7.7);
		kuer.get(ValueType.HD, 0).setValue(8.8);
		kuer.get(ValueType.SCHWIERIGKEIT, 0).setValue(20.5);

		BigDecimal erg = calc2.calculate(pflicht);
		assertEquals(new BigDecimal("23.200"), erg);

		erg = calc2.calculate(kuer);
		assertEquals(BigDecimal.valueOf(44.1).setScale(3), erg);

		kuer.get(ValueType.HALTUNG, 1).setValue(7.7);

		erg = calc2.calculate(kuer);
		assertEquals(BigDecimal.valueOf(44.1).setScale(3), erg);

		erg = WertungCalculatorFactory.calculate(kuer);
		assertEquals(BigDecimal.valueOf(44.1).setScale(3), erg);
	}

}
