package de;

import de.application.BundesligaActivityBean;
import de.application.IController;
import de.presentation.bundesliga.BundesligaView;

public class Main {
	public static void main(String[] args) {
    	LoginDialog loginDlg = new LoginDialog(null);
    	loginDlg.setVisible(true);
                       
    	// if logon not successfully
    	if (loginDlg.isSucceeded()) {
            //Make sure we have nice window decorations.
    		//JFrame.setDefaultLookAndFeelDecorated(true);
    		BundesligaView view = new BundesligaView();
    		
    		IController controller = new BundesligaActivityBean(view);
    		controller.runApp();    		
    	}		
	}
}
