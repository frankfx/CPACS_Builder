package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.business.BundesligaModel;
import de.presentation.bundesliga.BundesligaView;

public class BundesligaActivityBean {
	BundesligaModel model;
	BundesligaView view;
	
	public BundesligaActivityBean(BundesligaModel model, BundesligaView view) {
		this.model = model;
		this.view = view;
	}

	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				view.initView();
				addListener();
			}
		});
	}
	
	private void addListener() {
		view.setButtonExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.dispose();
			}
		});
	}
}
