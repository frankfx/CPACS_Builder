package de.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.presentation.bundesliga.StatisticPanel;
import de.services.Database;
import de.services.LoggerService;
import de.utils.FAMessages;

public class StatisticActivityBean implements ISubController {

	private BundesligaActivityBean mBundesligaListener;
	private Database mDB = null;
	private StatisticPanel mView;	
	
	final static Logger logger = LoggerService.getInstance().getLogger();
	
	public StatisticActivityBean(StatisticPanel pView) {
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

	public void updateStatisticByBalance(float pBalance){
		mView.setBalanceValue(pBalance);
		int n = (int) (mView.getProgressBar().getValue() + mView.getBalanceValue()) / 10;
		mView.getProgressBar().setValue(n);
	}
	
	// ========================
	 // START INIT LISTENER
	 // ========================	
			
	/**
	 * sets all listeners from the presentation view
	 */		
	private void addListener() {	
		mView.setProgressbarListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				logger.info(FAMessages.MSG_PROGRESS_BAR_CHANGED);
			}
		});			
	}	
}
