package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;
import de.kreth.vereinsmeisterschaftprog.data.WertungOld;

public class WertungCalculatorFactory {

	public static <T extends Wertung> double calculate(T wertung) {
		double result = 0;

		if (wertung instanceof WertungOld) {

			WertungOld wert = (WertungOld) wertung;
			switch (getAnzahlWerte(wertung)) {
			case 3:
				result = new WertungCalculatorFor3Values().calculate(wert);
				break;

			case 4:
				result = new WertungCalculatorFor4Values().calculate(wert);
				break;

			case 5:
				result = new WertungCalculatorFor5Values().calculate(wert);
				break;

			default:
				break;
			}
		}
		else {

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

		if (wertung instanceof WertungOld) {
			WertungOld wert = (WertungOld) wertung;
			if (wert.getKari5() > 0)
				anzahl++;

		}
		return anzahl;
	}
}
