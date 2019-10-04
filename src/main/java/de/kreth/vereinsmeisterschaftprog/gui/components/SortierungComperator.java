package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.util.Comparator;

import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Sortierung;

class SortierungComperator implements Comparator<Ergebnis> {

	private Sortierung sort = Sortierung.Nach_Startreihenfolge;

	@Override
	public int compare(Ergebnis o1, Ergebnis o2) {
		switch (sort) {
		case Nach_Ergebnis:
			int compare = Double.compare(o1.getPlatz(), o2.getPlatz());
			return compare == 0 ? Integer.compare(o1.getId(), o2.getId()) : compare;
		case Nach_Startreihenfolge:
			return o1.compareForStartreihenfolge(o2);
		default:
			break;
		}
		return 0;
	}

	public void setSortierung(Sortierung sort) {
		this.sort = sort;
	}

}