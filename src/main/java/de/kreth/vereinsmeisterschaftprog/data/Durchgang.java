package de.kreth.vereinsmeisterschaftprog.data;

public enum Durchgang {

	PFLICHT("Pflicht"),
	KUER("Kür"),
	SEILSPRINGEN("Seilspringen"),
	DREISPRUNG("Dreisprung");

	private final String label;

	private Durchgang(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
