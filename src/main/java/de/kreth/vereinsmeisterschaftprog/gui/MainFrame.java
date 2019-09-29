package de.kreth.vereinsmeisterschaftprog.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.kreth.vereinsmeisterschaftprog.business.MainFrameBusiness;
import de.kreth.vereinsmeisterschaftprog.data.Gruppe;

public class MainFrame extends JFrame implements MainView {

	private static final long serialVersionUID = 5118057573440157488L;

	private final JPanel contentPane;

	private final MainFrameBusiness business;

	private final DefaultListModel<Gruppe> model;

	public MainFrame() {
		setTitle("Vereinsmeisterschaften 2014");
		business = new MainFrameBusiness(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 736, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		model = new DefaultListModel<Gruppe>() {

			private static final long serialVersionUID = -3809219187518599443L;

			/**
			 * Löscht alle Einträge und Initialisiert mit "Hinzufügen"-Eintrag.
			 */
			@Override
			public void clear() {
				super.clear();
				addElement(new Gruppe(-1, "Hinzufügen", "Kein Eintrag! Neu erstellen, wenn gewählt!"));
			}
		};

		refreshGroups();

		contentPane.add(business.getPanel(), BorderLayout.CENTER);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));

		final JList<Gruppe> pflichtenView = new JList<Gruppe>();
		panel.add(pflichtenView, BorderLayout.CENTER);

		pflichtenView.setFont(new Font("Dialog", Font.BOLD, 14));
		pflichtenView.setPreferredSize(new Dimension(100, 0));
		pflichtenView.setMinimumSize(new Dimension(150, 150));
		pflichtenView.setSelectedIndex(1);
		pflichtenView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		pflichtenView.setModel(model);

		pflichtenView.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {

					Gruppe selection = pflichtenView.getSelectedValue();
					if (selection != null)
						business.pflichtChange(selection);
				}

			}
		});
		if (pflichtenView.getModel().getSize() > 1)
			pflichtenView.setSelectedIndex(1);
		else
			pflichtenView.setSelectedIndex(0);

		JButton btnExport = new JButton("Exportieren");
		btnExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				business.doExport();
			}
		});
		panel.add(btnExport, BorderLayout.SOUTH);
	}

	@Override
	public void showNewGruppeDialog() {
		GroupEditDialog dlg = new GroupEditDialog();
		dlg.setVisible(true);

		if (dlg.isClosedWithOk()) {
			business.createGroup(dlg.getGroupName(), dlg.getGroupDescritpion());
			refreshGroups();
		}
	}

	private void refreshGroups() {
		model.clear();

		for (Gruppe p : business.getGruppen())
			model.addElement(p);

	}

}
