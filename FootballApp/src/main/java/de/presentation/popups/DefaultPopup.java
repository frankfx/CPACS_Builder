package de.presentation.popups;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class DefaultPopup {

	public static int runPopup(Object[] message, String pTitle) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

		JDialog lDialog = pane.createDialog(null, pTitle);
		lDialog.setVisible(true);

		Object selectedValue = pane.getValue();
		int n = -1;

		if (selectedValue == null)
			n = JOptionPane.CLOSED_OPTION;
		else
			n = Integer.parseInt(selectedValue.toString());

		lDialog.dispose();
		return n;
	}
}
