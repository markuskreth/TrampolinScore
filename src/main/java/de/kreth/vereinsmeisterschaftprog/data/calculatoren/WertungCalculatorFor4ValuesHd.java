package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.util.ArrayList;
import java.util.List;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor4ValuesHd implements WertungCalcularor {

	@Override
	public double calculate(Wertung wertung) {
		double result = 0;

		List<Double> values = new ArrayList<>();
		if (wertung.getKari1() > 0) {
			values.add(wertung.getKari1());
			result += wertung.getKari1();
		}

		if (wertung.getKari2() > 0) {
			values.add(wertung.getKari2());
			result += wertung.getKari2();
		}

		if (wertung.getKari3() > 0) {
			values.add(wertung.getKari3());
			result += wertung.getKari3();
		}

		if (wertung.getKari4() > 0) {
			values.add(wertung.getKari4());
			result += wertung.getKari4();
		}

		result -= WertungCalcularor.min(values);
		result -= WertungCalcularor.max(values);

		result += WertungCalcularor.calculateHd(wertung).doubleValue();

		if (wertung.getDurchgang() == Durchgang.KUER)
			result += wertung.getSchwierigkeit();

		return result;
	}

}
