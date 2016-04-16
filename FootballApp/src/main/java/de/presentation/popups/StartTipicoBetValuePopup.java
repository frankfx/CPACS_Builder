package de.presentation.popups;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class StartTipicoBetValuePopup implements IPopup {
	JSpinner lSpinWinValue;
	JSpinner lSpinOdds;
	JSpinner lSpinExpenses;
	JCheckBox lCheckSubmitBet;

	public StartTipicoBetValuePopup(Object[] pParams) {
		lSpinWinValue = new JSpinner(new SpinnerNumberModel(((Float) pParams[0]).floatValue(), 1, 100, 0.1));
		lSpinOdds = new JSpinner(new SpinnerNumberModel(3.0, 1, 40, 0.1));
		lSpinExpenses = new JSpinner(new SpinnerNumberModel(((Float) pParams[1]).floatValue(), 0, 100, 0.1));
		lCheckSubmitBet = new JCheckBox("submit bet value!");
		lCheckSubmitBet.setEnabled(((Boolean) pParams[2]).booleanValue());
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
