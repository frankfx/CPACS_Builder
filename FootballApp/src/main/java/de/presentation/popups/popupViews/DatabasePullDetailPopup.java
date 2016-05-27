package de.presentation.popups.popupViews;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import de.presentation.popups.IPopup;
import de.services.SQLService;

public class DatabasePullDetailPopup implements IPopup {

	private JComboBox<String> mTemplateList;

	public DatabasePullDetailPopup(Object[] pParams) {
		mTemplateList = new JComboBox<String>();
		mTemplateList.addItem(SQLService.SQL_PULLDETAILS_OPEN);
	}

	@Override
	public String[] requestInputData() {
		Object[] mInput = { "Templates", mTemplateList };

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
			return new String[] { mTemplateList.getSelectedItem().toString() };
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
