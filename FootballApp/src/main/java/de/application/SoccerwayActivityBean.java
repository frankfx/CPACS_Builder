package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.presentation.bundesliga.ConsolenPanel;
import de.services.LoggerService;

public class SoccerwayActivityBean implements ISubController {

	private ConsolenPanel mView;
	
	final static Logger logger = LoggerService.getInstance().getLogger();
		
	public SoccerwayActivityBean(ConsolenPanel pView) {
		logger.setLevel(Level.ALL);
		this.mView = pView;
		this.addListener();
	}
	
	@Override
	public void initBean(String[] args) {

	}

	@Override
	public void updateBean() {

	}

	@Override
	public void setUpdateListener(BundesligaActivityBean pSubController) {

	}

	@Override
	public void print() {

	}

	public void clearConsole() {
		this.mView.setConsole("");
	}	
	
	public void appendConsole(String lText) {
		this.mView.getConsole().append(System.getProperty("user.name") + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> " + lText + "\n");
	}	
	
	// ========================
	 // START INIT LISTENER
	 // ========================	
			
	/**
	 * sets all listeners from the presentation view
	 */		
	private void addListener() {	
		mView.setButtonClearListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearConsole();
			}
		});	
	}	
}
