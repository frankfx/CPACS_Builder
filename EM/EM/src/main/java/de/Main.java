package de;

import de.application.BundesligaDataActivityBean;
import de.presentation.MainFrame;

public class Main {
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		new BundesligaDataActivityBean(new MainFrame("Tset"), 27, "bl1", "2015");
            }
        });
	}	
}