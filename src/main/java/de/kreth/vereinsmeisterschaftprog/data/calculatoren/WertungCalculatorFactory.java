package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFactory {

	public static double calculate(Wertung wertung) {
		double result = 0;

		switch (getAnzahlWerte(wertung)) {
		case 2:
			result = new WertungCalculatorFor2ValuesHd().calculate(wertung);
			break;

		case 3:
			result = new WertungCalculatorFor3ValuesHd().calculate(wertung);
			break;

		case 4:
			result = new WertungCalculatorFor4ValuesHd().calculate(wertung);
			break;

		default:
			break;
		}
		return result;
	}

	private static int getAnzahlWerte(Wertung wertung) {
		int anzahl = 0;

		if (wertung.getKari1() > 0)
			anzahl++;

		if (wertung.getKari2() > 0)
			anzahl++;

		if (wertung.getKari3() > 0)
			anzahl++;

		if (wertung.getKari4() > 0)
			anzahl++;

		return anzahl;
	}
}
