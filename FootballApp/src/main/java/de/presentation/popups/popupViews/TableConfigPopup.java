package de.presentation.popups.popupViews;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;

public class TableConfigPopup implements IPopup {

	public TableConfigPopup(Object[] pParams) {
	}

	@Override
	public String[] requestInputData() {

		JTextField lRegex = new JTextField("(id,<,5) and (attempts,<,4) or (team,=,M*)");
		Object[] message = { "Filter regex: ", lRegex };

		int n = DefaultPopup.runPopup(message, "filter dialoge");

		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lRegex.getText() };
		} else {
			return null;
		}
	}

}
