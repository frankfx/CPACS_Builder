package de;

import javax.swing.JFrame;

import de.application.BundesligaActivityBean;
import de.business.BundesligaModel;
import de.presentation.bundesliga.BundesligaView;

public class Main {
	public static void main(String[] args) {

        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
		BundesligaModel model = new BundesligaModel();
		BundesligaView view = new BundesligaView();
		
		BundesligaActivityBean controller = new BundesligaActivityBean(model, view);
		controller.runApp();
	}
}
