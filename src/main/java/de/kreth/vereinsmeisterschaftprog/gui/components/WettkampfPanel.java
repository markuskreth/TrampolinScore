package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import de.kreth.vereinsmeisterschaftprog.Factory;
import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Sortierung;
import de.kreth.vereinsmeisterschaftprog.data.Wettkampf;

public class WettkampfPanel extends JPanel {

	private static final long serialVersionUID = -257839817852907002L;

	private final WettkampfBusiness business;

	private final JTable table;

	private final ErgebnisTableModel tableModel;

	private final JComboBox<Durchgang> comboBox_Durchgang = new JComboBox<>();

	/**
	 * Create the panel.
	 * 
	 * @param wettkampfBusiness
	 */
	public WettkampfPanel(final WettkampfBusiness wettkampfBusiness) {

		super(new BorderLayout(0, 0));

		this.business = wettkampfBusiness;

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JButton btnNeuerStarter = new JButton("Neuer Starter");
		btnNeuerStarter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				business.newStarter();
			}
		});
		panel.add(btnNeuerStarter);

		JLabel lblDurchgang = new JLabel("Durchgang");
		panel.add(lblDurchgang);

		comboBox_Durchgang.setModel(
				new DefaultComboBoxModel<Durchgang>(Factory.getInstance().getDurchgaenge().toArray(new Durchgang[0])));
		comboBox_Durchgang.setRenderer(new ListCellRenderer<Durchgang>() {

			@Override
			public Component getListCellRendererComponent(JList<? extends Durchgang> list, Durchgang value, int index,
					boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.getLabel());
			}
		});
		panel.add(comboBox_Durchgang);

		JLabel lblSortierung = new JLabel("Sortierung");
		panel.add(lblSortierung);

		final JComboBox<Sortierung> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<Sortierung>(Sortierung.values()));
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Sortierung sort = (Sortierung) comboBox.getSelectedItem();
				tableModel.setSortierung(sort);
			}
		});

		panel.add(comboBox);
		tableModel = new ErgebnisTableModel(business, () -> (Durchgang) comboBox_Durchgang.getSelectedItem());
		table = new JTable();
		table.setModel(tableModel);

		TableColumnModel columnModel = table.getColumnModel();
		TableColumn btnColumn = columnModel.getColumn(getButtonColumnIndex());
		int width = 50;
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			if (i == 0)
				columnModel.getColumn(i).setPreferredWidth(width * 3);
			else if (i == getButtonColumnIndex())
				columnModel.getColumn(i).setPreferredWidth((int) (width * 1.5));
			else
				columnModel.getColumn(i).setPreferredWidth(width);
		}
		btnColumn.setCellRenderer(new ButtonRenderer());
		btnColumn.setCellEditor(new ButtonEditor());
		JScrollPane scroller = new JScrollPane(table);
		add(scroller, BorderLayout.CENTER);

	}

	public int getButtonColumnIndex() {
		return tableModel.getColumnCount() - 1;
	}

	public void setWettkampf(Wettkampf wettkampf) {

		tableModel.removeAllElements();

		if (wettkampf != null) {

			for (Ergebnis e : wettkampf.getErgebnisse()) {
				tableModel.addElement(e);
			}

		}
		tableModel.sort();
	}
}
