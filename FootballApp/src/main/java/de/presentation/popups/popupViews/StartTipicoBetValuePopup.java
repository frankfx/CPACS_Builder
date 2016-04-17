package de.presentation.popups.popupViews;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;

public class StartTipicoBetValuePopup implements IPopup {
	JSpinner lSpinWinValue;
	JSpinner lSpinOdds;
	JSpinner lSpinExpenses;
	JCheckBox lCheckSubmitBet;

	public StartTipicoBetValuePopup(Object[] pParams) {
		lSpinWinValue = new JSpinner(new SpinnerNumberModel((float) pParams[0], 1, 100, 0.1));
		lSpinOdds = new JSpinner(new SpinnerNumberModel(3.0, 1, 40, 0.1));
		lSpinExpenses = new JSpinner(new SpinnerNumberModel((float) pParams[1], 0, 100, 0.1));
		lCheckSubmitBet = new JCheckBox("submit bet value!");
		lCheckSubmitBet.setEnabled((boolean) pParams[2]);
	}

	@Override
	public String[] requestInputData() {
		Object[] message = { "", lCheckSubmitBet, "Odds.", lSpinOdds, "WinValue", lSpinWinValue, "Expenses", lSpinExpenses };

		int n = DefaultPopup.runPopup(message, "bet value recommendation");

		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lSpinOdds.getValue().toString(), lSpinWinValue.getValue().toString(), lSpinExpenses.getValue().toString(), lCheckSubmitBet.isSelected() + "" };
		} else {
			return null;
		}
	}
}
