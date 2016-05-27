package de.presentation.popups.popupViews;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;

public class TableFilterPopup implements IPopup {

	public TableFilterPopup(Object[] pParams) {
	}

	@Override
	public String[] requestInputData() {

		//		JTextField lRegex = new JTextField("((id,<,5) and (attempts,<,4) or (team,=,M*))");
		JCheckBox lCheckFilter = new JCheckBox("Activate Filter");
		JTextField lRegex = new JTextField("(((EXPENSES;>;10) AND (WINVALUE;<;1)) AND (ID;<;10))");

		Object[] message = { "", lCheckFilter, "Filter regex: ", lRegex };

		int n = DefaultPopup.runPopup(message, "filter dialoge");

		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lCheckFilter.isSelected() + "", lRegex.getText() };
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
