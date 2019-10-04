package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

class WertungCalculatorFor3Values extends AbstractWertungCalcularor {

	@Override
	public BigDecimal calculate(Wertung wertung) {

		BigDecimal result = sumAllHaltung(wertung);

		result = addDifficulty(wertung, result);

		return result;
	}

}
