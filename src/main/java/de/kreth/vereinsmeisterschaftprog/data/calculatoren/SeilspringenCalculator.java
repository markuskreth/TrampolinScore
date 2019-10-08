package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;

public class SeilspringenCalculator extends MaxValueCalculator {

	public SeilspringenCalculator() {
		super(ValueType.SEILSPRINGEN);
	}

	@Override
	protected BigDecimal valueTransform(BigDecimal value, Gruppe currentGroup) {
		if (BigDecimal.ZERO.compareTo(value) == 0) {
			return value;
		}
		int gruppeId = (int) currentGroup.getId();
		BigDecimal augend = getAugend(gruppeId);
		return value.multiply(BigDecimal.valueOf(.15)).add(augend).setScale(1, RoundingMode.HALF_UP);

	}

	private BigDecimal getAugend(int gruppeId) {
		BigDecimal augend;
		switch (gruppeId) {
		case 0:
			augend = BigDecimal.valueOf(3);
			break;

		case 1:
			augend = BigDecimal.valueOf(2);
			break;

		case 2:
			augend = BigDecimal.ONE;
			break;

		case 3:
			augend = BigDecimal.ZERO;
			break;

		default:
			augend = BigDecimal.valueOf(-1.5);
			break;
		}
		return augend;
	}
}
