package de.presentation.popups.popupViews;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;

public class TipicoAccountBalancePopup implements IPopup {

	JSpinner lSpinAccountBalance;
	
	public TipicoAccountBalancePopup(Object[] pParams) {
		lSpinAccountBalance = new JSpinner(new SpinnerNumberModel((float) pParams[0], -1000, 10000, 0.5));
	}
	
	@Override
	public String[] requestInputData() {
		Object[] message = { "Account balance", lSpinAccountBalance };

		int n = DefaultPopup.runPopup(message, "change account balance");
		
		if (n == JOptionPane.OK_OPTION) {
			return new String [] {lSpinAccountBalance.getValue().toString()};
		}
		
		return null;
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		// TODO Auto-generated method stub
		return null;
	}
}
