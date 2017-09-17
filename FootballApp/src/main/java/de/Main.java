package de;

import de.application.BundesligaActivityBean;
import de.presentation.bundesliga.BundesligaView;

public class Main {
	
	private final static boolean sWITHLOGIN = true;
	
	public static void main(String[] args) {
    	
		if (sWITHLOGIN) {
			
			LoginDialog loginDlg = new LoginDialog(null);
	    	loginDlg.setVisible(true);
	                       
	    	// if login successfully
	    	if (loginDlg.isSucceeded()) {
	            //Make sure we have nice window decorations.
	    		//JFrame.setDefaultLookAndFeelDecorated(true);
	    		new BundesligaActivityBean(new BundesligaView()).runApp();		
	    	}		
		
		} else {
    		new BundesligaActivityBean(new BundesligaView()).runApp();
		}
	}
}
