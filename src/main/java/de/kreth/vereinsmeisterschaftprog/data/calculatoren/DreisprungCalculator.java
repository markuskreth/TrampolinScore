package de.kreth.vereinsmeisterschaftprog.data.calculatoren;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;

public class DreisprungCalculator extends MaxValueCalculator {

	public DreisprungCalculator() {
		super(ValueType.DREISPRUNG);
	}

	@Override
	protected BigDecimal valueTransform(BigDecimal value, Gruppe currentGroup) {

		if (BigDecimal.ZERO.compareTo(value) == 0) {
			return value;
		}
		int gruppeId = (int) currentGroup.getId();
		BigDecimal augend = getAugend(gruppeId);
		return value
				.multiply(BigDecimal.valueOf(2))
				.add(augend)
//				.subtract(BigDecimal.ONE)
				.setScale(1, RoundingMode.HALF_UP);
	}

	private BigDecimal getAugend(int gruppeId) {
		BigDecimal augend;
		switch (gruppeId) {
		case 0:
			augend = BigDecimal.valueOf(2);
			break;

		case 1:
			augend = BigDecimal.ONE;
			break;

		case 2:
			augend = BigDecimal.ZERO;
			break;

		case 3:
			augend = BigDecimal.valueOf(-1);
			break;

		default:
			augend = BigDecimal.valueOf(-2);
			break;
		}
		return augend;
	}
}
