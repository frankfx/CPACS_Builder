package de.presentation.popups;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import de.business.TipicoModel;
import de.utils.SpinnerTemporalModel;

public class Popup {
	
	public static String [] startTipicoPopupNew(TipicoModel pTipicoModel, boolean pIDEnable){
		JSpinner lID = new JSpinner(new SpinnerNumberModel(pTipicoModel.getTnr(), 1, 100, 1));
		JSpinner lWinValue = new JSpinner(new SpinnerNumberModel(pTipicoModel.getWinValue(), 0, 100, 0.1));
		JSpinner lExpenses = new JSpinner(new SpinnerNumberModel(pTipicoModel.getExpenses(), 0, 1000, 1.1));
		JSpinner lAttempts = new JSpinner(new SpinnerNumberModel(pTipicoModel.getAttempts(), 0, 100, 1));
		JSpinner lDate = new JSpinner(new SpinnerTemporalModel<LocalDate>(pTipicoModel.getDate(), LocalDate.of(2016, 01, 01), LocalDate.of(2020, 01, 01), ChronoUnit.DAYS));

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

		int n = runPopup(message, "bet value recommendation");
	    
		if (n == JOptionPane.OK_OPTION){
			return new String[]{lOdds.getValue().toString(), lWinValue.getValue().toString()};
		} else {
			return null;
		}
	}

	

	public static void startDatabaseBrowser(List<TipicoModel> pTipicos){
		DefaultListModel<TipicoModel> lListModel = new DefaultListModel<TipicoModel>();
		
		for(TipicoModel tm : pTipicos)
			lListModel.addElement(tm);
		
		JList<TipicoModel> lList = new JList<TipicoModel>(lListModel);
		
		JScrollPane fruitListScrollPane = new JScrollPane(lList);
		
		Object[] message = {"List", fruitListScrollPane};
		
		runPopup(message, "database browser");
		
	}
	
	public static void startHintPopup(String pMessage){
		JOptionPane.showMessageDialog(null, pMessage, "HINT", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void startErrorPopup(String pMessage){
		JOptionPane.showMessageDialog(null, pMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	
	private static int runPopup(Object[] message, String pTitle) {
		JOptionPane pane = new JOptionPane( message,
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.OK_CANCEL_OPTION);		
		
		JDialog lDialog = pane.createDialog(null, pTitle);
		lDialog.setVisible(true);
		
		Object selectedValue = pane.getValue();
	    int n = -1;

	    if(selectedValue == null)
	        n = JOptionPane.CLOSED_OPTION;      
	    else
	        n = Integer.parseInt(selectedValue.toString());				

	    lDialog.dispose();
		return n;
	}	
}