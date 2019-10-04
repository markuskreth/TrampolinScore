package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor4Values extends AbstractWertungCalcularor {

	@Override
	public BigDecimal calculate(Wertung wertung) {
		BigDecimal result = sumAllHaltung(wertung);

		result = addDifficulty(wertung, result);

		result = result.divide(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(3));
		return result;

	}

}
