package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor2ValuesHd extends AbstractWertungCalcularor {

	@Override
	public BigDecimal calculate(Wertung wertung) {

		BigDecimal result = sumAllHaltung(wertung);

		result = addHdAndDifficulty(wertung, result);

		return result;
	}

}
