package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Value;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

abstract class AbstractWertungCalcularor implements WertungCalcularor {

	protected BigDecimal sumAllHaltung(Wertung wertung) {
		BigDecimal result = wertung.getByType(ValueType.HALTUNG).stream().map(v -> v.getValue())
				.reduce((v1, v2) -> v1.add(v2)).orElse(BigDecimal.ZERO);
		return result;
	}

	protected Value min(List<Value> values) {
		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException("Values must not be empty");
		}
		List<Value> sorted = new ArrayList<>(values);
		sorted.sort(this::compareValue);
		return sorted.get(0);
	}

	protected Value max(List<Value> values) {

		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException("Values must not be empty");
		}
		List<Value> sorted = new ArrayList<>(values);
		sorted.sort(this::compareValue);
		return sorted.get(sorted.size() - 1);
	}

	protected int compareValue(Value v1, Value v2) {
		return v1.getValue().compareTo(v2.getValue());
	}

	protected BigDecimal addHdAndDifficulty(Wertung wertung, BigDecimal result) {
		result = result.add(calculateHd(wertung));
		result = addDifficulty(wertung, result);
		return result;
	}

	protected BigDecimal addDifficulty(Wertung wertung, BigDecimal result) {
		if (wertung.getDurchgang() == Durchgang.KUER) {
			List<Value> difficulty = wertung.getByType(ValueType.SCHWIERIGKEIT);
			if (difficulty.size() != 1) {
				throw new IllegalStateException("Kuer must not have other than 1 SCHWIERIGKEIT");
			}
			result = result.add(difficulty.get(0).getValue());
		}
		return result;
	}

	private BigDecimal calculateHd(Wertung wertung) {
		BigDecimal result = BigDecimal.ZERO;

		List<Value> hdWerte = wertung.getByType(ValueType.HD);
		for (Value v : hdWerte) {
			result = result.add(v.getValue());
		}
		result = result.divide(BigDecimal.valueOf(hdWerte.size()), RoundingMode.HALF_UP);
		return result;
	}

}
