package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.presentation.bundesliga.ConsolenPanel;
import de.services.Database;
import de.services.LoggerService;

public class SoccerwayActivityBean implements ISubController {

	private BundesligaActivityBean mBundesligaListener;
	private Database mDB = null;
	private ConsolenPanel mView;
	
	final static Logger logger = LoggerService.getInstance().getLogger();
		
	public SoccerwayActivityBean(ConsolenPanel pView) {
		logger.setLevel(Level.ALL);
		this.mView = pView;
		this.addListener();
	}
	
	@Override
	public void initBean(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBean() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUpdateListener(BundesligaActivityBean pSubController) {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

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
