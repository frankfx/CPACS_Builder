package de;

import de.application.BundesligaActivityBean;
import de.presentation.bundesliga.BundesligaView;

public class Main {
	public static void main(String[] args) {

        //Make sure we have nice window decorations.
		//JFrame.setDefaultLookAndFeelDecorated(true);
		BundesligaView view = new BundesligaView();
		
		BundesligaActivityBean controller = new BundesligaActivityBean(view);
		controller.runApp();
	}
}
