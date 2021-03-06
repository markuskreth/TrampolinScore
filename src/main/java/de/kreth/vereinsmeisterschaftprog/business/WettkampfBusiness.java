package de.kreth.vereinsmeisterschaftprog.business;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;
import de.kreth.vereinsmeisterschaftprog.db.Persister;
import de.kreth.vereinsmeisterschaftprog.gui.WertenDialog;
import de.kreth.vereinsmeisterschaftprog.gui.components.WettkampfPanel;

public class WettkampfBusiness {

	private WettkampfPanel panel;

	private Wettkampf wettkampf;

	private Persister persister;

	public WettkampfBusiness() {
		this.panel = new WettkampfPanel(this);
		persister = Factory.getInstance().getPersister();
	}

	public void setWettkampf(Wettkampf wettkampf) {
		this.wettkampf = wettkampf;
		panel.setWettkampf(wettkampf);
	}

	public WettkampfPanel getPanel() {
		return panel;
	}

	public void newStarter() {
		String starterName = JOptionPane.showInputDialog(panel, "Name des Starters", "Neuer Starter",
				JOptionPane.QUESTION_MESSAGE);
		if (starterName != null && !starterName.isEmpty()) {
			Ergebnis e = persister.createErgebnis(starterName, wettkampf);
			wettkampf.add(e);
			panel.setWettkampf(wettkampf);
		}
	}

	public void changePersonName(String oldStarterName) {
		for (Ergebnis e : new ArrayList<Ergebnis>(wettkampf.getErgebnisse())) {

			if (e.getStarterName().matches(oldStarterName)) {

				String starterName = JOptionPane.showInputDialog(panel, "Name des Starters", "Name ändern",
						JOptionPane.QUESTION_MESSAGE, null, null, oldStarterName).toString();
				e.setStarterName(starterName);
				break;
			}
		}
	}

	public void werteErgebnis(Ergebnis e, Wertung wertung) {

		WertenDialog dlg = new WertenDialog(this, e.getStarterName(), wertung);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setVisible(true);
	}

}
