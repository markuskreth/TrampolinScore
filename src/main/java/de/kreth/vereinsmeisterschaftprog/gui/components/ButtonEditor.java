package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

class ButtonEditor extends AbstractCellEditor implements ActionListener, TableCellEditor {

	private static final long serialVersionUID = -3269232034263059218L;

	private final JButton button;

	public ButtonEditor() {

		button = new JButton();
		button.addActionListener(this);
		button.setBorderPainted(false);
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		fireEditingStopped();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
			int column) {
		return (JButton) value;
	}

}