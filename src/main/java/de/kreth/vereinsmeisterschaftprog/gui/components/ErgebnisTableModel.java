package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Sortierung;

class ErgebnisTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2910124315519583475L;

	private final String[] columnNames = { "Starter", "Pflicht", "Kür", "Gesamt", "Platz", "" };

	private final List<Ergebnis> data = new ArrayList<>();

	private final List<JButton> editButtons = new ArrayList<>();

	private final DecimalFormat df = new DecimalFormat("0.0##");

	private final SortierungComperator comperator = new SortierungComperator();

	private final WettkampfBusiness business;

	private final Supplier<Durchgang> durchgangSupplier;

	public ErgebnisTableModel(WettkampfBusiness business, Supplier<Durchgang> durchgangSupplier) {
		super();
		this.business = business;
		this.durchgangSupplier = durchgangSupplier;
	}

	public void setSortierung(Sortierung sort) {
		comperator.setSortierung(sort);
		sort();
	}

	void sort() {
		Collections.sort(data, comperator);
		fireTableDataChanged();
	}

	public void addElement(final Ergebnis e) {
		data.add(e);
		e.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Collections.sort(data, comperator);
				fireTableDataChanged();
			}
		});
		JButton b = new JButton("Werten");
		b.addActionListener(new ActionListener() {
			int index = data.size() - 1;

			@Override
			public void actionPerformed(ActionEvent ev) {
				business.werteErgebnis(data.get(index), durchgangSupplier.get());
			}
		});
		editButtons.add(b);
		fireTableDataChanged();
	}

	public void removeAllElements() {
		data.clear();
		editButtons.clear();
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 5)
			return true;
		return false;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 5)
			return JComponent.class;

		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ergebnis ergebnis = data.get(rowIndex);
		Object value;
		switch (columnIndex) {
		case 0:
			value = ergebnis.getStarterName();
			break;

		case 1:
			value = df.format(ergebnis.getPflicht().getErgebnis());
			break;

		case 2:
			value = df.format(ergebnis.getKuer().getErgebnis());
			break;

		case 3:
			value = df.format(ergebnis.getErgebnis());
			break;
		case 4:
			value = ergebnis.getPlatz() + ".";
			break;
		case 5:
			value = editButtons.get(rowIndex);
			break;
		default:
			value = "";
			break;
		}

		return value;
	}
}