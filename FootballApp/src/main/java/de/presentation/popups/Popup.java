package de.presentation.popups;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class Popup {

	static int mTempID = 1;
	static String mTempTeam = "";
	static float mTempWinValue = 3.4f;	
	static float mTempExpenses = 3.4f;	
	static float mTempBet = 1.0f;	
	static float mTempProfit = 3.0f;
	static boolean mIDEnalbe = true;
	
	public static String [] startTipicoPopupNew(){
		JSpinner lID = new JSpinner(new SpinnerNumberModel(mTempID, 1, 100, 1));
		JTextField lTeam = new JTextField(mTempTeam);
		
		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(mTempWinValue, 1, 100, 0.1));
		JSpinner lExpenses = new JSpinner(new SpinnerNumberModel(mTempExpenses, 1, 1000, 1.1));
		JSpinner lBet = new JSpinner(new SpinnerNumberModel(mTempBet, 1, 100, 0.2));
		JSpinner lProfit = new JSpinner(new SpinnerNumberModel(mTempProfit, 0.0f, 1000, 1.0));
				
		lID.setEnabled(mIDEnalbe);
		
		Object[] message = {"TNr.", lID, "Team", lTeam, "Win Value", lWinValue, 
				"Expenses", lExpenses, "Bet", lBet, "Profit", lProfit};

		JOptionPane pane = new JOptionPane( message,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);
                
		pane.createDialog(null, "Tipico").setVisible(true);
		
		int n = Integer.parseInt(pane.getValue().toString());
		
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lID.getValue().toString(), lTeam.getText(), lWinValue.getValue().toString(), 
					lExpenses.getValue().toString(), lBet.getValue().toString(), lProfit.getValue().toString()};
		} else {
			return null;
		}
	}

	public static void setPopupInputValues(int pID, String pTeam, float pWinValue, float pExpenses, float pBet, float pProfit, boolean pIDEnable){
		mTempID = pID;
		mTempTeam = pTeam;
		mTempWinValue = pWinValue;
		mTempExpenses = pExpenses;
		mTempBet = pBet;
		mTempProfit = pProfit;
		mIDEnalbe = pIDEnable;
	}

	public static void startHintPopup(String pMessage){
		new JOptionPane(pMessage, JOptionPane.WARNING_MESSAGE).createDialog("Hint").setVisible(true);;
	}
	
	public static void resetPopupInputValues(){
		setPopupInputValues(1, "", 3.4f, 3.4f, 1.0f, 3.0f, true);
	}
	
	public static void startPopupError(String pMessage){
		new JOptionPane(pMessage, JOptionPane.ERROR_MESSAGE).createDialog("ERROR").setVisible(true);
	}
}