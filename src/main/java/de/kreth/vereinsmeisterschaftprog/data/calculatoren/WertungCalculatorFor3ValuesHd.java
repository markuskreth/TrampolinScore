package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

class WertungCalculatorFor3ValuesHd extends AbstractWertungCalcularor {

	@Override
	public BigDecimal calculate(Wertung wertung) {

		BigDecimal result = sumAllHaltung(wertung);
		int scale = result.scale();

		result = result.divide(BigDecimal.valueOf(3), scale + 1, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(2))
				.setScale(scale, RoundingMode.HALF_UP);

		result = addHdAndDifficulty(wertung, result);

		return result;
	}

}
