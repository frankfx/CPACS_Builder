package de.presentation.popups.popupViews;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.mysql.jdbc.StringUtils;

import de.business.SpinnerTemporalModel;
import de.business.TipicoModel;
import de.presentation.popups.IPopup;
import de.services.SWJSONParser;
import de.types.BetPredictionType;
import de.utils.TeamIDInputVerifier;
import de.utils.TeamIDKeyAdapter;

public class TipicoNewPopup implements IPopup {

	private JTextField mTeamID;
	private JSpinner mSpinWinValue;
	private JComboBox<BetPredictionType> mComboBetPrediction;
	private JSpinner mSpinExpenses;
	private JSpinner mSpinAttempts;
	private JSpinner mSpinDate;
	private JTextField mTeam;

	private String mPrevTeamID;
	
	public TipicoNewPopup(Object [] pParams){
		
		TipicoModel lTipicoModel = (TipicoModel) pParams[0];
		
		float lWin = lTipicoModel.getWinValue();
		float lExp = lTipicoModel.getExpenses();
		int lAtt = lTipicoModel.getAttempts();

		mPrevTeamID = lTipicoModel.getID();
		
		mSpinWinValue = new JSpinner(new SpinnerNumberModel(lWin < 1 ? 1.0 : lWin, 1.0, 100, 0.1));
		mSpinExpenses = new JSpinner(new SpinnerNumberModel(lExp < 1 ? 1.0 : lExp, 1.0, 1000, 1.0));
		mSpinAttempts = new JSpinner(new SpinnerNumberModel(lAtt < 1 ? 1 : lAtt, 1, 100, 1));
		mSpinDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(lTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2020, 01, 01), ChronoUnit.DAYS));
		
		mTeam = new JTextField();
		mTeamID = new JTextField();
		if (mPrevTeamID != null){
			mTeamID.setText(mPrevTeamID);
			mTeamID.setEditable(false);
			mTeam.setText(lTipicoModel.getTeam());
		} else {
			mTeam.setEditable(false);
			mTeamID.setInputVerifier(new TeamIDInputVerifier());
			mTeamID.addKeyListener(new TeamIDKeyAdapter(mTeamID));
			mTeamID.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e){
					actionComputeTeamByID();
				}
			});
		}
		
		BetPredictionType [] predictions = new BetPredictionType [] {BetPredictionType.DRAW, BetPredictionType.HOME_WIN, BetPredictionType.AWAY_WIN, BetPredictionType.FST_DRAW, BetPredictionType.FST_HOME_WIN, BetPredictionType.FST_AWAY_WIN, BetPredictionType.SND_DRAW, BetPredictionType.SND_HOME_WIN, BetPredictionType.SND_AWAY_WIN};
		mComboBetPrediction = new JComboBox<BetPredictionType>(predictions); 
	}
	
	public void actionComputeTeamByID(){
    	if ( !StringUtils.isNullOrEmpty(mTeamID.getText()) && 
    			(mPrevTeamID == null || !mPrevTeamID.equals(mTeamID.getText()))){
    		String team = SWJSONParser.getTeamNameByID(mTeamID.getText());
    		mTeam.setText(team);
    		mTeam.setEditable(team == null);
    		mPrevTeamID = mTeam.getText();
    	} else {
    		mTeam.setText(null);
    	}
	}

	@Override
	public String[] requestInputData() {
		JComboBox<Boolean> lSuccess = new JComboBox<Boolean>();
		lSuccess.addItem(false);
		lSuccess.addItem(true);
			
		Object[] message = { "TNr.", mTeamID, "Team", mTeam, "Bet prediction", mComboBetPrediction , "Win Value", mSpinWinValue,
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
			if (StringUtils.isEmptyOrWhitespaceOnly(mTeamID.getText())){
				return null;
			}
			
			return new String[] { mTeamID.getText(), mTeam.getText(), mComboBetPrediction.getSelectedItem().toString(), mSpinWinValue.getValue().toString(),
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