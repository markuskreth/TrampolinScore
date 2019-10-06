package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Sortierung;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

class ErgebnisTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2910124315519583475L;

	private final List<String> columnNames = new ArrayList<>();

	private final List<Ergebnis> data = new ArrayList<>();

	private final List<JButton> editButtons = new ArrayList<>();

	private final DecimalFormat df = new DecimalFormat("0.0##");

	private final SortierungComperator comperator = new SortierungComperator();

	private final WettkampfBusiness business;

	private final Supplier<Durchgang> durchgangSupplier;

	public ErgebnisTableModel(WettkampfBusiness business, Supplier<Durchgang> durchgangSupplier) {
		super();
		columnNames.add("Starter");
		for (Durchgang d : Factory.getInstance().getDurchgaenge()) {
			columnNames.add(d.getLabel());
		}
		columnNames.addAll(Arrays.asList("Gesamt", "Platz", ""));
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
				Ergebnis ergebnis = data.get(index);
				Wertung wertung = ergebnis.getWertungen().stream()
						.filter(w -> w.getDurchgang().equals(durchgangSupplier.get())).findFirst().orElseThrow();
				business.werteErgebnis(ergebnis, wertung);
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
		return columnNames.get(column);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == columnNames.size() - 1)
			return true;
		return false;
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == columnNames.size() - 1)
			return JComponent.class;

		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ergebnis ergebnis = data.get(rowIndex);
		Object value;
		if (columnIndex == 0) {
			value = ergebnis.getStarterName();
		}
		else if (columnIndex == columnNames.size() - 1) {
			value = editButtons.get(rowIndex);
		}
		else if (columnIndex == columnNames.size() - 2) {
			value = ergebnis.getPlatz() + ".";
		}
		else if (columnIndex == columnNames.size() - 3) {
			value = df.format(ergebnis.getErgebnis());
		}
		else {
			Wertung wertung = ergebnis.getWertungen().get(columnIndex - 1);
			value = df.format(wertung.getErgebnis());
		}

		return value;
	}
}
