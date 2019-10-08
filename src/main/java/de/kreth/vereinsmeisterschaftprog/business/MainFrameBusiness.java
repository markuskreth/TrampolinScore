package de.kreth.vereinsmeisterschaftprog.business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;
import de.kreth.vereinsmeisterschaftprog.data.calculatoren.WertungCalculatorFactory;
import de.kreth.vereinsmeisterschaftprog.db.Persister;
import de.kreth.vereinsmeisterschaftprog.exporter.CsvExporter;
import de.kreth.vereinsmeisterschaftprog.gui.MainView;
import de.kreth.vereinsmeisterschaftprog.gui.components.WettkampfPanel;

public class MainFrameBusiness {

	private final List<GruppeChangeListener> gruppeListeners = new ArrayList<>();

	Map<Gruppe, Wettkampf> wettkaempfe;

	private WettkampfBusiness wettkampfBusiness;

	private Gruppe currentGruppe;

	private List<Gruppe> gruppen;

	private Persister persister;

	private MainView view;

	public MainFrameBusiness(MainView frame) {
		this.view = frame;
		wettkaempfe = new HashMap<Gruppe, Wettkampf>();
		wettkampfBusiness = new WettkampfBusiness();

		persister = Factory.getInstance().getPersister();
		gruppen = persister.loadPflichten();

		gruppeListeners.addAll(Arrays.asList(WertungCalculatorFactory.getGruppeListeners()));
		if (gruppen.size() > 0) {
			currentGruppe = gruppen.get(0);
			gruppeListeners.forEach(g -> g.changedTo(currentGruppe));
		}
		else {
			currentGruppe = Gruppe.INVALID;
		}

		for (Gruppe p : gruppen) {
			Wettkampf wettkampf = new Wettkampf(p.getName(), p);
			wettkaempfe.put(p, wettkampf);
			persister.fillWithStartern(wettkampf);
		}

		wettkampfBusiness.setWettkampf(wettkaempfe.get(currentGruppe));
	}

	public void addPflicht() {
		view.showNewGruppeDialog();
	}

	public void pflichtChange(Gruppe p) {
		if (p.getId() >= 0) {
			this.currentGruppe = p;
			wettkampfBusiness.setWettkampf(wettkaempfe.get(p));
		}
		else {
			if (gruppen.isEmpty()) {
				addPflicht();
				if (gruppen.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Es ist keine Gruppe vorhanden, Programm wird beendet.");
					System.exit(0);
				}
			}
			else {
				addPflicht();
			}
			currentGruppe = gruppen.get(0);
			wettkampfBusiness.setWettkampf(wettkaempfe.get(currentGruppe));
		}

		gruppeListeners.forEach(g -> g.changedTo(currentGruppe));
	}

	public List<Gruppe> getGruppen() {
		return gruppen;
	}

	public WettkampfPanel getPanel() {
		return wettkampfBusiness.getPanel();
	}

	public void doExport() {
		Wettkampf wettkampf = wettkaempfe.get(this.currentGruppe);
		File file = new File(this.currentGruppe.getName() + ".csv");

		if (file.exists())
			file.delete();

		Collection<Ergebnis> ergebnisse = wettkampf.getErgebnisse();
		CsvExporter exporter;

		try {
			FileWriter writer = new FileWriter(file);
			exporter = new CsvExporter(writer);
			exporter.export(ergebnisse);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createGroup(String groupName, String groupDescription) {
		Gruppe g = persister.createPflicht(groupName, groupDescription);
		gruppen.add(g);

		Wettkampf wettkampf = new Wettkampf(g.getName(), g);
		wettkaempfe.put(g, wettkampf);
	}

}
