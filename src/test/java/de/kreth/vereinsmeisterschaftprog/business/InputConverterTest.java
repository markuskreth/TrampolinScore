package de.kreth.vereinsmeisterschaftprog.business;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InputConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private InputConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new InputConverter(1);
	}

	@Test
	public void testInputWithoutComma() throws ParseException {
		double result = converter.convert("73");
		assertEquals(7.3, result, 0.001);

		result = converter.convert("11");
		assertEquals(1.1, result, 0.001);

		result = converter.convert("99");
		assertEquals(9.9, result, 0.001);

		result = converter.convert("0");
		assertEquals(0.0, result, 0.001);

		result = converter.convert("");
		assertEquals(0.0, result, 0.001);

		result = converter.convert("90");
		assertEquals(9.0, result, 0.001);

		result = converter.convert("10");
		assertEquals(1.0, result, 0.001);

		result = converter.convert("1");
		assertEquals(.1, result, 0.001);

		result = converter.convert("6");
		assertEquals(.6, result, 0.001);
	}

	@Test
	public void testInputWithComma() throws ParseException {

		double result = converter.convert("7,3");
		assertEquals(7.3, result, 0.001);

		result = converter.convert("1,1");
		assertEquals(1.1, result, 0.001);

		result = converter.convert("9,9");
		assertEquals(9.9, result, 0.001);

		result = converter.convert("0");
		assertEquals(0.0, result, 0.001);

		result = converter.convert(",0");
		assertEquals(0.0, result, 0.001);

		result = converter.convert("0,0");
		assertEquals(0.0, result, 0.001);

		result = converter.convert("9,0");
		assertEquals(9.0, result, 0.001);

		result = converter.convert("1,0");
		assertEquals(1.0, result, 0.001);

		result = converter.convert(",1");
		assertEquals(.1, result, 0.001);

		result = converter.convert("1,");
		assertEquals(1.0, result, 0.001);

		result = converter.convert(",6");
		assertEquals(.6, result, 0.001);

		result = converter.convert("6,");
		assertEquals(6.0, result, 0.001);

		result = converter.convert("6,1");
		assertEquals(6.1, result, 0.001);
	}

	@Test
	public void testSchwierigkeit() throws ParseException {
		double result = converter.convert("17,3");
		assertEquals(17.3, result, 0.001);

		result = converter.convert("11,1");
		assertEquals(11.1, result, 0.001);

		result = converter.convert("19,9");
		assertEquals(19.9, result, 0.001);

		result = converter.convert("19,0");
		assertEquals(19.0, result, 0.001);

		result = converter.convert("110");
		assertEquals(11, result, 0.001);

		result = converter.convert("119");
		assertEquals(11.9, result, 0.001);

		result = converter.convert("173");
		assertEquals(17.3, result, 0.001);

		result = converter.convert("190");
		assertEquals(19.0, result, 0.001);

		result = converter.convert("110");
		assertEquals(11.0, result, 0.001);

		result = converter.convert("111");
		assertEquals(11.1, result, 0.001);

		result = converter.convert("116");
		assertEquals(11.6, result, 0.001);
	}
}
