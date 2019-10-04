package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import de.kreth.vereinsmeisterschaftprog.business.InputConverter;
import de.kreth.vereinsmeisterschaftprog.business.WettkampfBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Durchgang;
import de.kreth.vereinsmeisterschaftprog.data.Ergebnis;
import de.kreth.vereinsmeisterschaftprog.data.Value;
import de.kreth.vereinsmeisterschaftprog.data.ValueType;
import de.kreth.vereinsmeisterschaftprog.data.Wertung;

public class WertenDialog extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = -878644536065558566L;

	private final JPanel contentPanel = new JPanel();

	private final List<JFormattedTextField> valueFields = new ArrayList<>();

	private InputConverter converter = new InputConverter();

	private JLabel lblStarter;

	private final Wertung wertung;

	private String starterName;

	private Wertung dummy;

	private JLabel lblErgebnis;

	/**
	 * Create the dialog.
	 */
	public WertenDialog(final WettkampfBusiness business, String starterName, Wertung wertung) {

		this.starterName = starterName;

		setTitle(wertung.getDurchgang() + " " + starterName);

		this.wertung = wertung;

		DecimalFormat df = new DecimalFormat("0.0#");

		setBounds(100, 100, 655, 214);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblStarter = new JLabel();
				lblStarter.setPreferredSize(new Dimension(50, 10));
				panel.add(lblStarter);
				lblStarter.setFont(new Font("Dialog", Font.BOLD, 17));
			}
			{
				JButton btnNamendern = new JButton("Name Ändern");
				btnNamendern.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						business.changePersonName(starterName);

					}
				});
				panel.add(btnNamendern, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 91, 91, 91, 91, 91, 91, 91, 0 };
			gbl_panel.rowHeights = new int[] { 70, 70, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel lblNewLabel = new JLabel("Kari1");
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
				gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel.gridx = 0;
				gbc_lblNewLabel.gridy = 0;
				panel.add(lblNewLabel, gbc_lblNewLabel);
			}
			{
				JLabel lblKari = new JLabel("Kari2");
				GridBagConstraints gbc_lblKari = new GridBagConstraints();
				gbc_lblKari.fill = GridBagConstraints.BOTH;
				gbc_lblKari.insets = new Insets(0, 0, 5, 5);
				gbc_lblKari.gridx = 1;
				gbc_lblKari.gridy = 0;
				panel.add(lblKari, gbc_lblKari);
			}
			{
				JLabel lblKari_1 = new JLabel("Kari3");
				GridBagConstraints gbc_lblKari_1 = new GridBagConstraints();
				gbc_lblKari_1.fill = GridBagConstraints.BOTH;
				gbc_lblKari_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblKari_1.gridx = 2;
				gbc_lblKari_1.gridy = 0;
				panel.add(lblKari_1, gbc_lblKari_1);
			}
			{
				JLabel lblKari_2 = new JLabel("Kari4");
				GridBagConstraints gbc_lblKari_2 = new GridBagConstraints();
				gbc_lblKari_2.fill = GridBagConstraints.BOTH;
				gbc_lblKari_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblKari_2.gridx = 3;
				gbc_lblKari_2.gridy = 0;
				panel.add(lblKari_2, gbc_lblKari_2);
			}
			{
				JLabel lblKari_3 = new JLabel("Kari5");
				GridBagConstraints gbc_lblKari_3 = new GridBagConstraints();
				gbc_lblKari_3.fill = GridBagConstraints.BOTH;
				gbc_lblKari_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblKari_3.gridx = 4;
				gbc_lblKari_3.gridy = 0;
				panel.add(lblKari_3, gbc_lblKari_3);
			}
			{
				JLabel lblSchwierigkeit = new JLabel("Schwierigkeit");
				GridBagConstraints gbc_lblSchwierigkeit = new GridBagConstraints();
				gbc_lblSchwierigkeit.fill = GridBagConstraints.BOTH;
				gbc_lblSchwierigkeit.insets = new Insets(0, 0, 5, 5);
				gbc_lblSchwierigkeit.gridx = 5;
				gbc_lblSchwierigkeit.gridy = 0;
				panel.add(lblSchwierigkeit, gbc_lblSchwierigkeit);
			}
			{
				JLabel lblErgebnis_2 = new JLabel("Ergebnis");
				GridBagConstraints gbc_lblErgebnis_2 = new GridBagConstraints();
				gbc_lblErgebnis_2.fill = GridBagConstraints.BOTH;
				gbc_lblErgebnis_2.insets = new Insets(0, 0, 5, 0);
				gbc_lblErgebnis_2.gridx = 6;
				gbc_lblErgebnis_2.gridy = 0;
				panel.add(lblErgebnis_2, gbc_lblErgebnis_2);
			}
			int gridx = 0;
			for (Value v : wertung.allValues()) {
				JFormattedTextField txtKari = new JFormattedTextField(df);
				txtKari.setName(v.identifier());
				new JTextField().putClientProperty(Value.class, v);
				GridBagConstraints gbc_txtKari1 = new GridBagConstraints();
				gbc_txtKari1.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtKari1.insets = new Insets(0, 0, 5, 5);
				gbc_txtKari1.gridx = gridx;
				gbc_txtKari1.gridy = 1;
				panel.add(txtKari, gbc_txtKari1);
				txtKari.setText("1");
				txtKari.addFocusListener(new DecimalFocusListener(txtKari, v));
				txtKari.setColumns(10);
				this.valueFields.add(txtKari);
				gridx++;
			}
			{
				lblErgebnis = new JLabel("90,3");
				lblErgebnis.setHorizontalAlignment(SwingConstants.CENTER);
				lblErgebnis.setHorizontalTextPosition(SwingConstants.CENTER);
				GridBagConstraints gbc_lblErgebnis = new GridBagConstraints();
				gbc_lblErgebnis.insets = new Insets(0, 0, 5, 0);
				gbc_lblErgebnis.fill = GridBagConstraints.HORIZONTAL;
				gbc_lblErgebnis.gridx = 6;
				gbc_lblErgebnis.gridy = 1;
				panel.add(lblErgebnis, gbc_lblErgebnis);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						for (Value value : WertenDialog.this.wertung.allValues()) {
							for (Value original : dummy.allValues()) {
								if (original.identifier().equals(value.identifier())) {
									value.setValue(original.getValue());
									break;
								}
							}
						}
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}

		updateView();

		valueFields.get(0).requestFocus();
	}

	private void markAsError(JTextField field) {
		field.setSelectionStart(0);
		field.setBackground(Color.ORANGE);
		field.setSelectionEnd(field.getText().length());
		field.requestFocus();
	}

	private void updateView() {

		lblStarter.setText(starterName);
		this.dummy = wertung.clone();
		for (JFormattedTextField f : valueFields) {
			Value v = (Value) f.getClientProperty(Value.class);
			if (ValueType.SCHWIERIGKEIT.equals(v.getType())) {

				if (wertung.getDurchgang() == Durchgang.PFLICHT) {
					f.setEnabled(false);
					f.setText("");
				}
				else {
					f.setEnabled(true);
				}
			}

			if (v.getValue().doubleValue() <= 0) {
				f.setText("");
			}
			else {
				f.setText(converter.format(v.getValue().doubleValue()));
			}
		}

		if (wertung.getErgebnis().doubleValue() <= 0)
			lblErgebnis.setText(converter.format(0));
		else
			lblErgebnis.setText(converter.format(wertung.getErgebnis().doubleValue()));

		valueFields.get(0).requestFocus();
	}

	private class DecimalFocusListener extends FocusAdapter {

		private JTextField field;

		private Value kari;

		public DecimalFocusListener(JTextField field, Value kari) {

			this.field = field;
			this.kari = kari;
		}

		@Override
		public void focusLost(FocusEvent e) {

			try {

				double number = converter.convert(field.getText());
				field.setText(converter.format(number));
				kari.setValue(number);

			}
			catch (ParseException e1) {
				markAsError(field);
				e1.printStackTrace();
			}

			lblErgebnis.setText(converter.format(wertung.getErgebnis().doubleValue()));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().matches(Ergebnis.STARTERNAME_CHANGE_PROPERTY)) {
			starterName = evt.getNewValue().toString();
			lblStarter.setText(starterName);
		}
	}
}
