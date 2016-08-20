package de.presentation.popups.popupViews;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.business.SpinnerTemporalModel;
import de.business.TipicoModel;
import de.presentation.popups.IPopup;
import de.types.BetPredictionType;

public class TipicoNewPopup implements IPopup {

	private JSpinner mSpinID;
	private JSpinner mSpinWinValue;
	private JComboBox<BetPredictionType> mComboBetPrediction;
	private JSpinner mSpinExpenses;
	private JSpinner mSpinAttempts;
	private JSpinner mSpinDate;
	private JTextField mTeam;
	
	private boolean mIDEnable; 
	
	public TipicoNewPopup(Object [] pParams){
		
		TipicoModel lTipicoModel = (TipicoModel) pParams[0];
		mIDEnable = (boolean) pParams[1];
		
		float lWin = lTipicoModel.getWinValue();
		float lExp = lTipicoModel.getExpenses();
		int lAtt = lTipicoModel.getAttempts();

		mSpinID = new JSpinner(new SpinnerNumberModel(lTipicoModel.getTnr(), 1, 100, 1));
		mSpinWinValue = new JSpinner(new SpinnerNumberModel(lWin < 1 ? 1.0 : lWin, 1.0, 100, 0.1));
		mSpinExpenses = new JSpinner(new SpinnerNumberModel(lExp < 1 ? 1.0 : lExp, 1.0, 1000, 1.0));
		mSpinAttempts = new JSpinner(new SpinnerNumberModel(lAtt < 1 ? 1 : lAtt, 1, 100, 1));
		mSpinDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(lTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2020, 01, 01), ChronoUnit.DAYS));

		BetPredictionType [] predictions = new BetPredictionType [] {BetPredictionType.DRAW, BetPredictionType.HOME_WIN, BetPredictionType.AWAY_WIN, BetPredictionType.FST_HOME_WIN, BetPredictionType.FST_AWAY_WIN, BetPredictionType.SND_HOME_WIN, BetPredictionType.SND_AWAY_WIN};
		mComboBetPrediction = new JComboBox<BetPredictionType>(predictions); 
		
		mTeam = new JTextField(lTipicoModel.getTeam());
	}

	@Override
	public String[] requestInputData() {
		JComboBox<Boolean> lSuccess = new JComboBox<Boolean>();
		lSuccess.addItem(false);
		lSuccess.addItem(true);
			
		mSpinID.setEnabled(mIDEnable);
			
		Object[] message = { "TNr.", mSpinID, "Team", mTeam, "Bet prediction", mComboBetPrediction , "Win Value", mSpinWinValue,
				"Expenses", mSpinExpenses, "Attempts", mSpinAttempts, "Date", mSpinDate, "Successfull", lSuccess };

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
			return new String[] { mSpinID.getValue().toString(), mTeam.getText(), mComboBetPrediction.getSelectedItem().toString(), mSpinWinValue.getValue().toString(),
					mSpinExpenses.getValue().toString(), mSpinAttempts.getValue().toString(), mSpinDate.getValue().toString(), lSuccess.getSelectedItem().toString() };
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