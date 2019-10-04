package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JButton btn = (JButton) value;
		if (isSelected) {
			btn.setForeground(table.getSelectionForeground());
			btn.setBackground(table.getSelectionBackground());
		}
		else {
			btn.setForeground(table.getForeground());
			btn.setBackground(UIManager.getColor("Button.background"));
		}
		return btn;
	}

}