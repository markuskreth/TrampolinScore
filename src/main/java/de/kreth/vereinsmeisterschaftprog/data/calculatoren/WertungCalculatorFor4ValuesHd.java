package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor4ValuesHd extends AbstractWertungCalcularor {

	@Override
	public BigDecimal calculate(Wertung wertung) {
		BigDecimal result = sumAllHaltung(wertung);

		result = result
				.subtract(min(wertung.getByType(ValueType.HALTUNG)).getValue())
				.subtract(max(wertung.getByType(ValueType.HALTUNG)).getValue());

		result = addHdAndDifficulty(wertung, result);

		return result;
	}

}
