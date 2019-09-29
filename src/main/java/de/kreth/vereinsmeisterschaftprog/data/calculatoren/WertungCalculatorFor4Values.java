package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertungCalculatorFor4Values implements WertungCalcularor {

	@Override
	public double calculate(Wertung wertung) {
		double result = 0;

		if (wertung.getKari1() > 0)
			result += wertung.getKari1();

		if (wertung.getKari2() > 0)
			result += wertung.getKari2();

		if (wertung.getKari3() > 0)
			result += wertung.getKari3();

		if (wertung.getKari4() > 0)
			result += wertung.getKari4();

//      if(wertung.getKari5()>0)
//         result +=wertung.getKari5();

		result = BigDecimal.valueOf(result).divide(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(3)).doubleValue();

		if (wertung.getDurchgang() == Durchgang.KUER)
			result += wertung.getSchwierigkeit();

		return result;
	}

}
