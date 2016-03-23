package de.presentation.popups;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.utils.SpinnerTemporalModel;

public class Popup {

	static int mTempID = 1;
	static String mTempTeam = "";
	static float mTempWinValue = 3.4f;	
	static float mTempExpenses = 3.4f;	
	static int mTempAttempts = 1;	
	static LocalDate mTempDate = LocalDate.now(); //new Date(1900, 1, 1);
	static boolean mTempSuccess = false;
	static boolean mIDEnalbe = true;
	
	public static String [] startTipicoPopupNew(){
		JSpinner lID = new JSpinner(new SpinnerNumberModel(mTempID, 1, 100, 1));
		JTextField lTeam = new JTextField(mTempTeam);
		
		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(mTempWinValue, 1, 100, 0.1));
		JSpinner lExpenses = new JSpinner(new SpinnerNumberModel(mTempExpenses, 1, 1000, 1.1));
		JSpinner lAttempts = new JSpinner(new SpinnerNumberModel(mTempAttempts, 1, 100, 1));
		JSpinner lDate = new JSpinner(new SpinnerTemporalModel(mTempDate, LocalDate.of(2016, 01, 01), LocalDate.of(2017, 01, 01), ChronoUnit.DAYS));
		
		
		JComboBox<Boolean> lSuccess = new JComboBox<Boolean>();
		lSuccess.addItem(true);
		lSuccess.addItem(false);
		
		lID.setEnabled(mIDEnalbe);
		
		Object[] message = {"TNr.", lID, "Team", lTeam, "Win Value", lWinValue, 
				"Expenses", lExpenses, "Attempts", lAttempts, "Date", lDate, "Successfull" , lSuccess};

		JOptionPane pane = new JOptionPane( message,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);
                
		pane.createDialog(null, "Tipico").setVisible(true);
		
		int n = Integer.parseInt(pane.getValue().toString());
		
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lID.getValue().toString(), lTeam.getText(), lWinValue.getValue().toString(), 
					lExpenses.getValue().toString(), lAttempts.getValue().toString(), lDate.getValue().toString(), lSuccess.getSelectedItem().toString()};
		} else {
			return null;
		}
	}

	public static String [] startTipicoPopupBetValue(){
//		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(mTempWinValue, 1, 100, 0.1));
		JSpinner lOdds = new JSpinner(new SpinnerNumberModel(3.0, 1, 40, 0.1));
		
		Object[] message = {"Odds.", lOdds};

		JOptionPane pane = new JOptionPane( message,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);		
		
		pane.createDialog(null, "Tipico").setVisible(true);
		
		int n = Integer.parseInt(pane.getValue().toString());
		
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lOdds.getValue().toString()};
		} else {
			return null;
		}
	}	

	public static void setPopupInputValues(int pID, String pTeam, float pWinValue, float pExpenses, int pAttempts, LocalDate pDate, boolean pSuccess, boolean pIDEnable){
		mTempID = pID;
		mTempTeam = pTeam;
		mTempWinValue = pWinValue;
		mTempExpenses = pExpenses;
		mTempAttempts = pAttempts;
		mTempDate = pDate;
		mTempSuccess = pSuccess;
		mIDEnalbe = pIDEnable;
	}

	public static void startHintPopup(String pMessage){
		new JOptionPane(pMessage, JOptionPane.WARNING_MESSAGE).createDialog("Hint").setVisible(true);
	}
	
	public static void resetPopupInputValues(){
		setPopupInputValues(1, "", 3.4f, 3.4f, 1, LocalDate.now(), false, true);
	}
	
	public static void startPopupError(String pMessage){
		new JOptionPane(pMessage, JOptionPane.ERROR_MESSAGE).createDialog("ERROR").setVisible(true);
	}
}