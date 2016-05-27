package de.presentation.popups.popupViews;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.presentation.popups.IPopup;

public class DatabaseConnectionPopup implements IPopup {

	private final JTextField lHostname;
	private final JTextField lDatabase;
	private final JSpinner lPort;
	private final JTextField lUser;
	private final JPasswordField lPass;
	private final JComboBox<String> lTemplates;


	public DatabaseConnectionPopup(Object[] pParams) {
		lHostname = new JTextField();
		lDatabase = new JTextField();
		lPort = new JSpinner(new SpinnerNumberModel(3306, 1, 9999, 1));
		lUser = new JTextField();
		lPass = new JPasswordField();
		lTemplates = new JComboBox<String>();
		lTemplates.addItem(null);
		lTemplates.addItem("localhost");
		lTemplates.addItem("db4free");
	}

	@Override
	public String[] requestInputData() {

		lTemplates.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!(lTemplates.getSelectedItem() == null)) {
					if (lTemplates.getSelectedItem().toString().equals("localhost")) {
						lHostname.setText("localhost");
						lDatabase.setText("TestData");
						lUser.setText("root");
					} else if (lTemplates.getSelectedItem().toString().equals("db4free")) {
						lHostname.setText("85.10.205.173");
						lDatabase.setText("testdb_tipico");
						lUser.setText("frankfx");
					}
				}
			}
		});

		Object[] mInput = { "Connections", lTemplates, "Host", lHostname, "Port", lPort, "Database", lDatabase,
				"User", lUser, "Password", lPass };

		JOptionPane pane = new JOptionPane(mInput,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);

		JDialog lDialog = pane.createDialog(null, "Database");
		lDialog.setVisible(true);

		Object selectedValue = pane.getValue();
		int n = -1;

		if (selectedValue == null)
			n = JOptionPane.CLOSED_OPTION;
		else
			n = Integer.parseInt(selectedValue.toString());

		lDialog.dispose();

		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lHostname.getText(), lPort.getValue().toString(), lDatabase.getText(),
					lUser.getText(), new String(lPass.getPassword()) };
		} else {
			return null;
		}
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		// TODO Auto-generated method stub
		return null;
	}	
}
