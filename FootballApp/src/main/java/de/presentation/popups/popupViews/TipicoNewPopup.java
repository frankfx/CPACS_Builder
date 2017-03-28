package de.presentation.popups.popupViews;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.mysql.jdbc.StringUtils;

import de.business.SpinnerTemporalModel;
import de.business.TipicoModel;
import de.presentation.popups.IPopup;
import de.services.SWHTMLParser;
import de.types.BetPredictionType;
import de.utils.TeamIDInputVerifier;
import de.utils.TeamIDKeyAdapter;

public class TipicoNewPopup implements IPopup {

	private JTextField mTeamID;
	private JSpinner mSpinWinValue;
	private JComboBox<BetPredictionType> mComboBetPrediction;
	private JSpinner mSpinExpenses;
	private JSpinner mSpinDate;
	private JTextField mTeam;
	private JComboBox<Boolean> mSuccess;
	private JTextArea mTextDescription;
	private JScrollPane lScrollDescription;
	
	private String mPrevTeamID;
	
	public TipicoNewPopup(Object [] pParams){
		
		TipicoModel lTipicoModel = (TipicoModel) pParams[0];
		
		float lWin = lTipicoModel.getWinValue();
		float lExp = lTipicoModel.getExpenses();

		mPrevTeamID = lTipicoModel.getID();
		mSpinWinValue = new JSpinner(new SpinnerNumberModel(lWin < 1 ? 1.0 : lWin, 1.0, 100, 0.1));
		mSpinExpenses = new JSpinner(new SpinnerNumberModel(lExp < 1 ? 1.0 : lExp, 1.0, 1000, 1.0));
		mSpinDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(lTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2020, 01, 01), ChronoUnit.DAYS));
		mSuccess = new JComboBox<Boolean>();
		mSuccess.addItem(false);
		mSuccess.addItem(true);
		mSuccess.setSelectedItem(lTipicoModel.getSuccess());
		mTextDescription = new JTextArea(6, 1);
		mTextDescription.setText(lTipicoModel.getDescription());
		mTextDescription.setLineWrap(true);
		mTextDescription.setWrapStyleWord(false);
		lScrollDescription= new JScrollPane( mTextDescription );
		
		
		
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
		
		BetPredictionType [] predictions = new BetPredictionType [] {BetPredictionType.WIN, BetPredictionType.LOSE, BetPredictionType.DRAW, BetPredictionType.FST_WIN, BetPredictionType.FST_LOSE, BetPredictionType.FST_DRAW, BetPredictionType.SND_WIN, BetPredictionType.SND_LOSE, BetPredictionType.SND_DRAW};
		mComboBetPrediction = new JComboBox<BetPredictionType>(predictions); 
		mComboBetPrediction.setSelectedItem(lTipicoModel.getBetPrediction());
	}
	
	public void actionComputeTeamByID(){
    	if ( !StringUtils.isNullOrEmpty(mTeamID.getText()) && 
    			(mPrevTeamID == null || !mPrevTeamID.equals(mTeamID.getText()))){
    		String team = SWHTMLParser.getTeamNameByID(mTeamID.getText());
    		mTeam.setText(team);
    		mTeam.setEditable(team == null);
    		mPrevTeamID = mTeam.getText();
    	} else {
    		mTeam.setText(null);
    	}
	}

	@Override
	public String[] requestInputData() {
		Object[] message = { "TNr.", mTeamID, "Team", mTeam, "Bet prediction", mComboBetPrediction , "Win Value", mSpinWinValue,
				"Expenses", mSpinExpenses, "Date", mSpinDate, "Successfull", mSuccess, "Description", lScrollDescription };

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
					mSpinExpenses.getValue().toString(), mSpinDate.getValue().toString(), mSuccess.getSelectedItem().toString(), mTextDescription.getText() };
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