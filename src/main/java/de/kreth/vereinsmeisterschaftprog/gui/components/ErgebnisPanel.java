package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.text.WordUtils;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class ErgebnisPanel extends JPanel {

	private static final long serialVersionUID = -126921232179715189L;

	private final Ergebnis ergebnis;

	private final JLabel lblStarterName;

	private final List<JLabel> wertungLabels = new ArrayList<>();

	private final JLabel lblErgebnis;

	private final JLabel lblPlatz;

	private final DecimalFormat df;

	/**
	 * Create the panel.
	 * @param durchgang
	 * @param business
	 */
	public ErgebnisPanel(final Ergebnis ergebnis, final Durchgang durchgang, WettkampfBusiness business) {
		this.ergebnis = ergebnis;
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(15);

		df = new DecimalFormat("#.0");

		lblStarterName = new JLabel("<StarterName>");
		add(lblStarterName);

		for (Wertung w : ergebnis.getWertungen()) {
			String labelText = "<" + WordUtils.capitalizeFully(w.getDurchgang().name()) + ">";
			JLabel label = new JLabel(labelText);
			wertungLabels.add(label);
			add(label);
		}

		lblErgebnis = new JLabel("<Ergebnis>");
		add(lblErgebnis);

		lblPlatz = new JLabel("<Platz>");
		add(lblPlatz);

		ergebnis.addPropertyChangeListener(new PCL());
		updateValues();
	}

	public void setColor(Color backgrnd) {
		lblStarterName.setBackground(backgrnd);
	}

	private void updateValues() {
		lblStarterName.setText(ergebnis.getStarterName());
		List<Wertung> wertungen = ergebnis.getWertungen();
		for (int i = 0; i < wertungen.size(); i++) {
			wertungLabels.get(i).setText(df.format(wertungen.get(i).getErgebnis()));
		}

		lblErgebnis.setText(df.format(ergebnis.getErgebnis()));
		lblPlatz.setText(df.format(ergebnis.getPlatz()));
	}

	private class PCL implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateValues();
		}

	}
//
//	public void performButtonClick() {
//
//		Optional<Wertung> wertung = ergebnis.getWertungen().stream().filter(w -> durchgang == w.getDurchgang())
//				.findAny();
//
//		if (wertung.isPresent()) {
//
//			final WertenDialog dlg = new WertenDialog(business, ergebnis.getStarterName(), wertung.get());
//			ergebnis.addPropertyChangeListener(dlg);
//			dlg.addWindowListener(new WindowAdapter() {
//				@Override
//				public void windowClosed(WindowEvent e) {
//					ergebnis.removePropertyChangeListener(dlg);
//				}
//			});
//			dlg.setVisible(true);
//		}
//	}
}
