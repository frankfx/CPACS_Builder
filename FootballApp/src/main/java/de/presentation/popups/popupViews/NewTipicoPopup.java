package de.presentation.popups.popupViews;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.business.TipicoModel;
import de.presentation.popups.IPopup;
import de.utils.SpinnerTemporalModel;

public class NewTipicoPopup implements IPopup {

	private JSpinner lSpinID;
	private JSpinner lSpinWinValue;
	private JSpinner lSpinExpenses;
	private JSpinner lSpinAttempts;
	private JSpinner lSpinDate;
	private JTextField lTeam;
	
	private boolean mIDEnable; 
	
	public NewTipicoPopup(Object [] pParams){
		
		TipicoModel lTipicoModel = (TipicoModel) pParams[0];
		mIDEnable = (boolean) pParams[1];
		
		lSpinID = new JSpinner(new SpinnerNumberModel(lTipicoModel.getTnr(), 1, 100, 1));
		lSpinWinValue = new JSpinner(new SpinnerNumberModel(lTipicoModel.getWinValue(), 0, 100, 0.1));
		lSpinExpenses = new JSpinner(new SpinnerNumberModel(lTipicoModel.getExpenses(), 0, 1000, 1.1));
		lSpinAttempts = new JSpinner(new SpinnerNumberModel(lTipicoModel.getAttempts(), 0, 100, 1));
		lSpinDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(lTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2020, 01, 01), ChronoUnit.DAYS));

		lTeam = new JTextField(lTipicoModel.getTeam());
	}

	@Override
	public String[] requestInputData() {
		JComboBox<Boolean> lSuccess = new JComboBox<Boolean>();
		lSuccess.addItem(false);
		lSuccess.addItem(true);
			
		lSpinID.setEnabled(mIDEnable);
			
		Object[] message = { "TNr.", lSpinID, "Team", lTeam, "Win Value", lSpinWinValue,
				"Expenses", lSpinExpenses, "Attempts", lSpinAttempts, "Date", lSpinDate, "Successfull", lSuccess };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	                
		JDialog lDialog = pane.createDialog(null, "Tipico");
		lDialog.setVisible(true);
			
		Object selectedValue = pane.getValue();
		int n = -1;

		if (selectedValue == null)
			n = JOptionPane.CLOSED_OPTION;
		else
			n = Integer.parseInt(selectedValue.toString());
			
		lDialog.dispose();
			
		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lSpinID.getValue().toString(), lTeam.getText(), lSpinWinValue.getValue().toString(),
					lSpinExpenses.getValue().toString(), lSpinAttempts.getValue().toString(), lSpinDate.getValue().toString(), lSuccess.getSelectedItem().toString() };
		} else {
			return null;
		}
	}
}