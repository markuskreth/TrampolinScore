package de.kreth.vereinsmeisterschaftprog.gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;

public class ErgebnisListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -5584364908962488266L;

	private final WettkampfBusiness business;

	private Durchgang durchgang = Durchgang.PFLICHT;

	public ErgebnisListRenderer(WettkampfBusiness business) {
		super();
		this.business = business;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		ErgebnisPanel panel = new ErgebnisPanel((Ergebnis) value, durchgang, business);

		if (durchgang == Durchgang.KUER)
			panel.setColor(Color.YELLOW);

		panel.setBackground(comp.getBackground());
		panel.setForeground(comp.getForeground());

		return panel;
	}

	public void setDurchgang(Durchgang durchgang) {
		this.durchgang = durchgang;
	}

	public void buttonClicked(Component renderComp) {
		ErgebnisPanel panel = (ErgebnisPanel) renderComp;
		panel.performButtonClick();
	}
}
