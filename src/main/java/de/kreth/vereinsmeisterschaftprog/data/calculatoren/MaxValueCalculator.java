package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import de.kreth.vereinsmeisterschaftprog.business.GruppeChangeListener;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Value;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public abstract class MaxValueCalculator implements WertungCalcularor, GruppeChangeListener {

	private final ValueType relevantValueType;

	private Gruppe currentGruppe;

	public MaxValueCalculator(ValueType relevantValueType) {
		super();
		this.relevantValueType = relevantValueType;
	}

	@Override
	public BigDecimal calculate(Wertung wertung) {
		List<Value> werte = wertung.getByType(relevantValueType);
		BigDecimal max = getMax(werte);
		return valueTransform(max, currentGruppe);
	}

	private BigDecimal getMax(List<Value> werte) {
		BigDecimal result = BigDecimal.ZERO;
		Optional<BigDecimal> max = werte.stream().map(v -> v.getValue()).reduce((d1, d2) -> {
			return (d1.doubleValue() > d2.doubleValue() ? d1 : d2);
		});
		if (max.isPresent()) {
			result = max.get();
		}
		return result;
	}

	protected abstract BigDecimal valueTransform(BigDecimal value, Gruppe currentGroup);

	@Override
	public void changedTo(Gruppe gruppe) {
		this.currentGruppe = gruppe;
	}

}
