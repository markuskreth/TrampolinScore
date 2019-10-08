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
public class DreisprungCalculatorTest {

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
		wertung = new Wertung(1, Durchgang.DREISPRUNG);
		value = new Value(ValueType.DREISPRUNG, expected.scale());
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

	@Parameters(name = "{index}: DREISPRUNG with GruppeId={0}, Meter ={1} => {2} ")
	public static List<Object[]> data() {
		List<Object[]> data = new ArrayList<>();

		data.add(new Object[] { 4, BigDecimal.valueOf(6.5), BigDecimal.valueOf(11.0).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(6.0), BigDecimal.valueOf(10.0).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(5.5), BigDecimal.valueOf(9.0).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(4.7), BigDecimal.valueOf(7.4).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(3.5), BigDecimal.valueOf(5.0).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(2.8), BigDecimal.valueOf(3.6).setScale(1) });
		data.add(new Object[] { 4, BigDecimal.valueOf(0.0).setScale(5), BigDecimal.valueOf(0.0).setScale(5) });

		data.add(new Object[] { 3, BigDecimal.valueOf(6.0), BigDecimal.valueOf(11.0).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(4.0), BigDecimal.valueOf(7.0).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(3.5), BigDecimal.valueOf(6.0).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(2.0), BigDecimal.valueOf(3.0).setScale(1) });
		data.add(new Object[] { 3, BigDecimal.valueOf(0.0).setScale(5), BigDecimal.valueOf(0.0).setScale(5) });

		data.add(new Object[] { 2, BigDecimal.valueOf(6.0), BigDecimal.valueOf(12.0).setScale(1) });
		data.add(new Object[] { 2, BigDecimal.valueOf(4.0), BigDecimal.valueOf(8.0).setScale(1) });
		data.add(new Object[] { 2, BigDecimal.valueOf(3.5), BigDecimal.valueOf(7.0).setScale(1) });
		data.add(new Object[] { 2, BigDecimal.valueOf(2.0), BigDecimal.valueOf(4.0).setScale(1) });
		data.add(new Object[] { 2, BigDecimal.valueOf(0.0).setScale(5), BigDecimal.valueOf(0.0).setScale(5) });

		data.add(new Object[] { 1, BigDecimal.valueOf(6.0), BigDecimal.valueOf(13.0).setScale(1) });
		data.add(new Object[] { 1, BigDecimal.valueOf(4.0), BigDecimal.valueOf(9.0).setScale(1) });
		data.add(new Object[] { 1, BigDecimal.valueOf(3.5), BigDecimal.valueOf(8.0).setScale(1) });
		data.add(new Object[] { 1, BigDecimal.valueOf(2.0), BigDecimal.valueOf(5.0).setScale(1) });
		data.add(new Object[] { 1, BigDecimal.valueOf(0.0).setScale(5), BigDecimal.valueOf(0.0).setScale(5) });

		data.add(new Object[] { 0, BigDecimal.valueOf(6.0), BigDecimal.valueOf(14.0).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(4.0), BigDecimal.valueOf(10.0).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(3.5), BigDecimal.valueOf(9.0).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(2.0), BigDecimal.valueOf(6.0).setScale(1) });
		data.add(new Object[] { 0, BigDecimal.valueOf(0.0).setScale(5), BigDecimal.valueOf(0.0).setScale(5) });

		return data;
	}
}
