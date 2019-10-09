package de.kreth.vereinsmeisterschaftprog.business;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import de.kreth.trampolinbusiness.DecimalFormatHelper;

/**
 * Diese Klasse konvertiert String Eingaben in double-Wert.
 * <ul>
 * <li>Leerer String => 0</li>
 * <li>Wert ohne Kommastelle => Wert/10</li>
 * <li>Wert mit Komma => Wert</li>
 * </ul>
 * 
 * @author markus
 *
 */
public class InputConverter {

	public final String format(double number) {
		return formatter.format(number);
	}

	private DecimalFormat formatter;

	private int precisision;

	public InputConverter(int precisision) {
		formatter = DecimalFormatHelper.getFormatter(precisision);
		this.precisision = precisision;
	}

	/**
	 * 
	 * <ul>
	 * <li>Leerer String => 0</li>
	 * <li>Wert ohne Kommastelle => Wert/10</li>
	 * <li>Wert mit Komma => Wert</li>
	 * </ul>
	 * 
	 * @param string
	 * @return Komma-Wert, der den String-Wert repräsentiert.
	 * @throws ParseException
	 */
	public double convert(String string) throws ParseException {

		BigDecimal number;
		if (string.length() == 0)
			number = BigDecimal.ZERO;
		else {
			number = BigDecimal.valueOf(formatter.parse(string).doubleValue());
			if (precisision > 0
					&& !string.contains(String.valueOf(formatter.getDecimalFormatSymbols().getDecimalSeparator()))
					&& number.multiply(BigDecimal.TEN).remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) == 0) {
				number = number.divide(BigDecimal.TEN);
			}
		}
		return number.doubleValue();
	}

}
