package de.presentation.popups;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.business.TipicoModel;
import de.utils.SpinnerTemporalModel;

public class Popup {

//	static int mTempID = 1;
//	static String mTempTeam = "";
//	static float mTempWinValue = 3.4f;	
//	static float mTempExpenses = 3.4f;	
//	static int mTempAttempts = 1;	
//	static LocalDate mTempDate = LocalDate.now(); //new Date(1900, 1, 1);
//	static boolean mTempSuccess = false;
//	static boolean mIDEnalbe = true;
	
	public static String [] startTipicoPopupNew(TipicoModel pTipicoModel, boolean pIDEnable){

		JSpinner lID = new JSpinner(new SpinnerNumberModel(pTipicoModel.getTnr(), 1, 100, 1));
		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(pTipicoModel.getWinValue(), 1, 100, 0.1));
		JSpinner lExpenses = new JSpinner(new SpinnerNumberModel(pTipicoModel.getExpenses(), 1, 1000, 1.1));
		JSpinner lAttempts = new JSpinner(new SpinnerNumberModel(pTipicoModel.getAttempts(), 1, 100, 1));
		JSpinner lDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(pTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2017, 01, 01), ChronoUnit.DAYS));

		JTextField lTeam = new JTextField(pTipicoModel.getTeam());
		
		JComboBox<Boolean> lSuccess = new JComboBox<Boolean>();
		lSuccess.addItem(false);
		lSuccess.addItem(true);
		
		lID.setEnabled(pIDEnable);
		
		Object[] message = {"TNr.", lID, "Team", lTeam, "Win Value", lWinValue, 
				"Expenses", lExpenses, "Attempts", lAttempts, "Date", lDate, "Successfull" , lSuccess};

		JOptionPane pane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                
		JDialog lDialog = pane.createDialog(null, "Tipico");
		lDialog.setVisible(true);
		
	    Object selectedValue = pane.getValue();
	    int n = -1;

	    if(selectedValue == null)
	        n = JOptionPane.CLOSED_OPTION;      
	    else
	        n = Integer.parseInt(selectedValue.toString());		
		
	    lDialog.dispose();
		
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lID.getValue().toString(), lTeam.getText(), lWinValue.getValue().toString(), 
					lExpenses.getValue().toString(), lAttempts.getValue().toString(), lDate.getValue().toString(), lSuccess.getSelectedItem().toString()};
		} else {
			return null;
		}
	}

	
	public static String [] startDatabaseConnectionPopup(){
		JTextField lHostname = new JTextField("localhost");
		JTextField lDatabase = new JTextField("TestData");
		JSpinner lPort = new JSpinner(new SpinnerNumberModel(3306, 1, 9999, 1));
		JTextField lUser = new JTextField("root");
		JPasswordField lPass = new JPasswordField("130386");
		
		Object[] mInput = {"Host", lHostname, "Port", lPort, "Database", lDatabase, 
				"User", lUser, "Password", lPass};

		JOptionPane pane = new JOptionPane( mInput,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);		
		
		JDialog lDialog = pane.createDialog(null, "Database");
		lDialog.setVisible(true);
		
	    Object selectedValue = pane.getValue();
	    int n = -1;

	    if(selectedValue == null)
	        n = JOptionPane.CLOSED_OPTION;      
	    else
	        n = Integer.parseInt(selectedValue.toString());		
		
	    lDialog.dispose();
	    
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lHostname.getText(), lPort.getValue().toString(), lDatabase.getText(), 
					lUser.getText(), new String(lPass.getPassword())};
		} else {
			return null;
		}		
	}
	
	
	public static String [] startTipicoPopupBetValue(float winValue){
		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(winValue, 1, 100, 0.1));
		JSpinner lOdds = new JSpinner(new SpinnerNumberModel(3.0, 1, 40, 0.1));
		
		Object[] message = {"Odds.", lOdds, "WinValue", lWinValue};

		JOptionPane pane = new JOptionPane( message,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);		
		
		JDialog lDialog = pane.createDialog(null, "bet value recommendation");
		lDialog.setVisible(true);
		
		Object selectedValue = pane.getValue();
	    int n = -1;

	    if(selectedValue == null)
	        n = JOptionPane.CLOSED_OPTION;      
	    else
	        n = Integer.parseInt(selectedValue.toString());				

	    lDialog.dispose();
	    
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lOdds.getValue().toString(), lWinValue.getValue().toString()};
		} else {
			return null;
		}
	}	

	public static void startHintPopup(String pMessage){
		JOptionPane.showMessageDialog(null, pMessage, "HINT", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void startErrorPopup(String pMessage){
		JOptionPane.showMessageDialog(null, pMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}