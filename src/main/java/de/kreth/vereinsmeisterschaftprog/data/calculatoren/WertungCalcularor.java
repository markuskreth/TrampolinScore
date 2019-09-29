package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

interface WertungCalcularor {
	/**
	 * Liefert zu den Werten in wertung das entprechende Ergebnis
	 * @param wertung Daten, aus denen das Ergebnis errechnet werden soll.
	 * @return Ergebnis
	 */
	double calculate(Wertung wertung);

	static BigDecimal calculateHd(Wertung wertung) {
		BigDecimal result = BigDecimal.ZERO;

		if (wertung.getHd1() > 0) {
			result = result.add(BigDecimal.valueOf(wertung.getHd1()));
		}
		if (wertung.getHd2() > 0) {
			result = result.add(BigDecimal.valueOf(wertung.getHd2()));
		}
		if (wertung.getHd1() > 0 && wertung.getHd2() > 0) {
			result = result.divide(BigDecimal.valueOf(2));
		}
		return result;
	}

	static double min(List<Double> values) {
		double[] arr = convert(values);
		return min(arr);
	}

	static double[] convert(List<Double> values) {
		double[] arr = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			arr[i] = values.get(i);
		}
		return arr;
	}

	public static double min(double... values) {
		return reduce(Math::min, values);
	}

	static double max(List<Double> values) {
		double[] arr = convert(values);
		return max(arr);
	}

	public static double max(double... values) {
		return reduce(Math::max, values);
	}

	private static double reduce(BiFunction<Double, Double, Double> func, double... values) {
		if (values == null || values.length == 0) {
			throw new IllegalArgumentException("Given Values must not be null.");
		}
		double min = values[0];

		for (int i = 1; i < values.length; i++) {
			min = func.apply(min, values[i]);
		}
		return min;
	}

}
