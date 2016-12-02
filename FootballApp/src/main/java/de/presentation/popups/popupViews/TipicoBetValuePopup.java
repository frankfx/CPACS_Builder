package de.presentation.popups.popupViews;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.presentation.popups.DefaultPopup;
import de.presentation.popups.IPopup;
import de.utils.math.MathTipico;

public class TipicoBetValuePopup implements IPopup {
	JSpinner lSpinWinValue;
	JSpinner lSpinOdds;
	JSpinner lSpinExpenses;
	JTextField lBetValueCompution;

	public TipicoBetValuePopup(Object[] pParams) {
		lSpinWinValue = new JSpinner(new SpinnerNumberModel((float) pParams[0], 1, 100, 0.1));
		lSpinOdds = new JSpinner(new SpinnerNumberModel(3.0, 1, 40, 0.1));
		lSpinExpenses = new JSpinner(new SpinnerNumberModel((float) pParams[1], 0, 1000, 0.1));
		lBetValueCompution = new JTextField("");
		lBetValueCompution.setEditable(false);

		this.computeBetValue();
		
		lSpinWinValue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				computeBetValue();
			}
		});
		
		lSpinOdds.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				computeBetValue();
			}
		});
		
		lSpinExpenses.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				computeBetValue();
			}
		});
	}

	@Override
	public String[] requestInputData() {
		Object[] message = { "New bet value", lBetValueCompution, "Odds.", lSpinOdds, "WinValue", lSpinWinValue, "Expenses", lSpinExpenses };

		int n = DefaultPopup.runPopup(message, "bet value recommendation");

		if (n == JOptionPane.OK_OPTION) {
			return new String[] { lSpinWinValue.getValue().toString(), lBetValueCompution.getText() };
		} else {
			return null;
		}
	}
	
	private void computeBetValue(){
		float a = MathTipico.computeBetValue(Float.parseFloat(lSpinWinValue.getValue().toString()),
				Float.parseFloat(lSpinExpenses.getValue().toString()),
				Float.parseFloat(lSpinOdds.getValue().toString()));

		lBetValueCompution.setText(a + "");
	}

	@Override
	public List<?> requestInputDataAsObjectList() {
		// TODO Auto-generated method stub
		return null;
	}	
}
