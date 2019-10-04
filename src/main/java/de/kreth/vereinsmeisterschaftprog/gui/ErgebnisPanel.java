package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class ErgebnisPanel extends JPanel {

	private static final long serialVersionUID = -126921232179715189L;

	private final WettkampfBusiness business;

	private final Ergebnis ergebnis;

	private JLabel lblStarterName;

	private JLabel lblPflicht;

	private JLabel lblKuer;

	private JLabel lblErgebnis;

	private JLabel lblPlatz;

	private Durchgang durchgang;

	private DecimalFormat df;

	/**
	 * Create the panel.
	 * @param durchgang
	 * @param business
	 */
	public ErgebnisPanel(final Ergebnis ergebnis, final Durchgang durchgang, WettkampfBusiness business) {
		this.ergebnis = ergebnis;
		this.business = business;
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setHgap(15);
		this.durchgang = durchgang;

		df = new DecimalFormat("#.0");

		lblStarterName = new JLabel("<StarterName>");
		add(lblStarterName);

		lblPflicht = new JLabel("<Pflicht>");
		add(lblPflicht);

		lblKuer = new JLabel("<Kür>");
		add(lblKuer);

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
		lblPflicht.setText(df.format(ergebnis.getPflicht().getErgebnis()));
		lblKuer.setText(df.format(ergebnis.getKuer().getErgebnis()));
		lblErgebnis.setText(df.format(ergebnis.getErgebnis()));
		lblPlatz.setText(df.format(ergebnis.getPlatz()));
	}

	private class PCL implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateValues();
		}

	}

	public void performButtonClick() {

		Wertung wertung = null;
		switch (durchgang) {
		case KUER:
			wertung = ergebnis.getKuer();
			break;
		case PFLICHT:
			wertung = ergebnis.getPflicht();
			break;
		}

		final WertenDialog dlg = new WertenDialog(business, ergebnis.getStarterName(), wertung);
		ergebnis.addPropertyChangeListener(dlg);
		dlg.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				ergebnis.removePropertyChangeListener(dlg);
			}
		});
		dlg.setVisible(true);
	}
}
