package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.kreth.vereinsmeisterschaftprog.business.GruppeChangeListener;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Value;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;
import de.kreth.vereinsmeisterschaftprog.data.WertungValueSetter;

@RunWith(Parameterized.class)
public class SeilsprungCalculatorTest {

	@Parameter(0)
	public int gruppeId;

	@Parameter(1)
	public BigDecimal measured;

	@Parameter(2)
	public BigDecimal expected;

	private Wertung wertung;

	private Value value;

	private GruppeChangeListener gruppeChangeListener;

	private List<Value> valueList;

	@Before
	public void initWertung() {
		wertung = new Wertung(1, Durchgang.SEILSPRINGEN);
		value = new Value(ValueType.SEILSPRINGEN, 2);
		gruppeChangeListener = new GruppeChangeListener() {

			@Override
			public void changedTo(Gruppe gruppe) {
				for (GruppeChangeListener l : WertungCalculatorFactory.getGruppeListeners()) {
					l.changedTo(gruppe);
				}
			}
		};
		valueList = new ArrayList<>();
		valueList.add(value);
	}

	@Test
	public void test() {
		gruppeChangeListener.changedTo(new Gruppe(gruppeId, "P" + gruppeId, ""));
		value.setValue(measured);
		WertungValueSetter.setValues(wertung, valueList);
		BigDecimal result = WertungCalculatorFactory.calculate(wertung);
		assertEquals(wertung.getErgebnis(), result);
		assertEquals(expected, result);
	}

	@Parameters(name = "{index}: SEILSPRINGEN with GruppeId={0}, Sprünge ={1} => {2} ")
	public static List<Object[]> data() {
		List<Object[]> data = new ArrayList<>();
		data.add(new Object[] { 4, BigDecimal.valueOf(30), BigDecimal.valueOf(3).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(35), BigDecimal.valueOf(3.8).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(50), BigDecimal.valueOf(6).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(60), BigDecimal.valueOf(7.5).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(70), BigDecimal.valueOf(9).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(77), BigDecimal.valueOf(10.1).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(80), BigDecimal.valueOf(10.5).setScale(1) });

		data.add(new Object[] { 3, BigDecimal.valueOf(24), BigDecimal.valueOf(3.6).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(29), BigDecimal.valueOf(4.4).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(44), BigDecimal.valueOf(6.6).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(54), BigDecimal.valueOf(8.1).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(64), BigDecimal.valueOf(9.6).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(74), BigDecimal.valueOf(11.1).setScale(1) });

		data.add(new Object[] { 0, BigDecimal.valueOf(10), BigDecimal.valueOf(4.5).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(15), BigDecimal.valueOf(5.3).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(30), BigDecimal.valueOf(7.5).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(40), BigDecimal.valueOf(9.0).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(50), BigDecimal.valueOf(10.5).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(60), BigDecimal.valueOf(12.0).setScale(1) });

		return data;
	}
}
