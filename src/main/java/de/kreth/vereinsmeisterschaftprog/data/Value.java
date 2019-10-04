package de.kreth.vereinsmeisterschaftprog.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

public class Value implements Cloneable {

	private final ValueType type;

	private final int precision;

	private final int index;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private BigDecimal value = BigDecimal.ZERO;

	/**
	 * Wert mit index = 0, nur für eindeutige Werte
	 * @param type
	 * @param precision
	 */
	public Value(ValueType type, int precision) {
		this(type, precision, 0);
	}

	public Value(ValueType type, int precision, int index) {
		super();
		this.type = type;
		this.index = index;
		this.precision = precision;
	}

	private Value(Value value) {
		this(value.type, value.precision, value.index);
		this.value = value.value;
	}

	public ValueType getType() {
		return type;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(double value) {
		BigDecimal old = this.value;
		this.value = BigDecimal.valueOf(value).setScale(precision);
		pcs.firePropertyChange(identifier(), old, this.value);
	}

	public void setValue(BigDecimal value) {
		BigDecimal old = this.value;
		this.value = value.setScale(precision);
		pcs.firePropertyChange(identifier(), old, this.value);
	}

	int getIndex() {
		return index;
	}

	/**
	 * Identifikation über typ und index.
	 * <p>
	 * kann auch verwendet werden für {@link #addPropertyChangeListener(String, PropertyChangeListener)}
	 * @return
	 */
	public String identifier() {
		return type.name() + "_" + index;
	}

	@Override
	public Value clone() {
		return new Value(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + precision;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Value other = (Value) obj;
		if (precision != other.precision) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		}
		else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[" + type + "=" + value + "]";
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

}
