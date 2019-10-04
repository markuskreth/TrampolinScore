package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupEditDialog {

	private final JDialog dlg = new JDialog();

	private JTextField groupname;

	private JTextField groupDescription;

	private boolean closedWithOk = false;

	public GroupEditDialog() {
		groupname = new JTextField();
		groupDescription = new JTextField();

		JPanel grid = new JPanel(new GridLayout(2, 2));
		grid.add(new JLabel("Gruppenname"));
		grid.add(groupname);
		grid.add(new JLabel("Beschreibung"));
		grid.add(groupDescription);

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this::okPressed);
		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(ev -> dlg.dispose());
		JPanel buttons = new JPanel();
		buttons.add(okButton);
		buttons.add(cancelButton);

		JPanel content = new JPanel(new BorderLayout());
		content.add(grid, BorderLayout.CENTER);
		content.add(buttons, BorderLayout.PAGE_END);

		dlg.setModalityType(ModalityType.APPLICATION_MODAL);
		dlg.setContentPane(content);
		dlg.pack();

		SwingUtilities.getRootPane(okButton).setDefaultButton(okButton);
	}

	private void okPressed(ActionEvent ev) {
		closedWithOk = true;
		dlg.dispose();
	}

	public boolean isClosedWithOk() {
		return closedWithOk;
	}

	public String getGroupName() {
		if (closedWithOk) {
			return groupname.getText();
		}
		throw new IllegalStateException("Can not be called without ok pressed.");
	}

	public String getGroupDescritpion() {
		if (closedWithOk) {
			return groupDescription.getText();
		}
		throw new IllegalStateException("Can not be called without ok pressed.");
	}

	public void setVisible(boolean b) {
		dlg.setVisible(b);
	}
}
