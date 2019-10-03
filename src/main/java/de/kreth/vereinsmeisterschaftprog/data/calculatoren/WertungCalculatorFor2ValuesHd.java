package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor2ValuesHd implements WertungCalcularor<Wertung> {

	@Override
	public double calculate(Wertung wertung) {

		BigDecimal result = BigDecimal.valueOf(wertung.getKari1());
		result = result
				.add(BigDecimal.valueOf(wertung.getKari2()))
				.add(BigDecimal.valueOf(wertung.getKari3()))
				.add(BigDecimal.valueOf(wertung.getKari4()));

		result = result.add(WertungCalcularor.calculateHd(wertung));

		if (wertung.getDurchgang() == Durchgang.KUER)
			result = result.add(BigDecimal.valueOf(wertung.getSchwierigkeit()));

		return result.doubleValue();
	}

}
