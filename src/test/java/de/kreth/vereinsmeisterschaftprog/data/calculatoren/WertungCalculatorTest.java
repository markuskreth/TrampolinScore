package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorTest {

	private WertungCalculatorFor2ValuesHd calc2;

	private WertungCalculatorFor4ValuesHd calc4;

	private WertungCalculatorFor3ValuesHd calc3;

	@Before
	public void setUp() {
		calc2 = new WertungCalculatorFor2ValuesHd();
		calc4 = new WertungCalculatorFor4ValuesHd();
		calc3 = new WertungCalculatorFor3ValuesHd();

	}

	@Test
	public void testKrummeWerte() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		pflicht.setKari1(5.8);
		pflicht.setKari2(5.7);
		pflicht.setKari3(5.6);
		pflicht.setKari4(5.7);
		pflicht.setHd1(7.3);
		pflicht.setHd2(7.8);

		double erg = calc4.calculate(pflicht);
		assertEquals(18.95, erg, 0.001);
	}

	@Test
	public void testCalculateCompleteJudges() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		pflicht.setKari1(7.1);
		pflicht.setKari2(7.2);
		pflicht.setKari3(7.3);
		pflicht.setKari4(7.4);
		pflicht.setHd1(8.7);
		pflicht.setHd2(9.5);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		kuer.setKari1(7.1);
		kuer.setKari2(7.2);
		kuer.setKari3(7.3);
		kuer.setKari4(7.4);
		kuer.setHd1(8.7);
		kuer.setHd2(9.5);
		kuer.setSchwierigkeit(20.5);

		double erg = calc4.calculate(pflicht);
		assertEquals(23.6, erg, 0.01);

		erg = calc4.calculate(kuer);
		assertEquals(44.1, erg, 0.001);

		erg = WertungCalculatorFactory.calculate(kuer);
		assertEquals(44.1, erg, 0.001);
	}

	@Test
	public void testCalculate3er() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		pflicht.setKari1(7.1);
		pflicht.setKari2(7.2);
		pflicht.setKari3(7.3);
		pflicht.setHd1(8.4);

		double erg = calc3.calculate(pflicht);
		assertEquals(22.8, erg, 0.000000001);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		kuer.setKari1(7.1);
		kuer.setKari3(7.2);
		kuer.setKari4(7.3);
		kuer.setHd1(9.3);
		kuer.setSchwierigkeit(20.5);

		erg = calc3.calculate(kuer);
		assertEquals(44.2, erg, 0.000000001);

		kuer.setHd1(8.3);
		erg = calc3.calculate(kuer);
		assertEquals(43.2, erg, 0.000000001);

		pflicht = new Wertung(1, Durchgang.PFLICHT);
		pflicht.setKari1(7.1);
		pflicht.setKari2(7.2);
		pflicht.setKari4(7.7);
		pflicht.setHd1(8.4);

		erg = calc3.calculate(pflicht);
		assertEquals(23.067, erg, 0.001);

		erg = WertungCalculatorFactory.calculate(pflicht);
		assertEquals(23.067, erg, 0.001);

	}

	@Test
	public void testCalculate2er() {

		Wertung pflicht = new Wertung(1, Durchgang.PFLICHT);
		pflicht.setKari1(7.1);
		pflicht.setKari4(7.4);
		pflicht.setHd1(8.7);

		Wertung kuer = new Wertung(1, Durchgang.KUER);
		kuer.setKari1(7.1);
		kuer.setKari3(7.7);
		kuer.setHd2(8.8);
		kuer.setSchwierigkeit(20.5);

		double erg = calc2.calculate(pflicht);
		assertEquals(23.2, erg, 0.000000001);

		erg = calc2.calculate(kuer);
		assertEquals(44.1, erg, 0.000000001);

		kuer.setKari3(0);
		kuer.setKari4(7.7);
		erg = calc2.calculate(kuer);
		assertEquals(44.1, erg, 0.000000001);

		erg = WertungCalculatorFactory.calculate(kuer);
		assertEquals(44.1, erg, 0.000000001);
	}

}
