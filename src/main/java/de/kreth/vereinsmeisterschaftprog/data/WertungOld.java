package de.kreth.vereinsmeisterschaftprog.data;

public class WertungOld extends Wertung {

	public static final String KARI5_CHANGE_PROPERTY = Wertung.class.getName() + " Kari5 geändert!";

	private double kari5 = 0;

	public WertungOld(int id, Durchgang durchgang) {
		super(id, durchgang);
	}

	@Override
	public WertungOld clone() {
		WertungOld clone = new WertungOld(getId(), getDurchgang());
		clone.kari5 = kari5;
		super.clone(clone);
		return clone;
	}

	public double getKari5() {
		return kari5;
	}

	public void setKari5(double kari5) {
		this.kari5 = kari5;
	}

}
