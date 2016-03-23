package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.business.BundesligaModel;
import de.business.Match;
import de.business.LigaWService;
import de.business.TipicoModel;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;

public class BundesligaActivityBean {
	private BundesligaModel mModel;
	private BundesligaView mView;
	private List<ISubController> mSubController;
	
	/**
	 * Controller 
	 * 
	 * @param pModel the data model
	 * @param pView the presentation widget
	 */	
	public BundesligaActivityBean(BundesligaModel pModel, BundesligaView pView) {
		this.mModel = pModel;
		this.mView = pView;
		this.mSubController = new ArrayList<ISubController>();
	}
	
	/**
	 * Add a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */		
	public void addSubController(ISubController pController){
		this.mSubController.add(pController);		
	}

	/**
	 * Remove a child controller
	 * 
	 * @param pController adds a new sub controller to a controller list
	 */		
	public void removeSubController(ISubController pController){
		this.mSubController.remove(pController);		
	}
	
	/**
	 * Reads all fixtures from the Webservice and set them to the presentation view
	 * 
	 * @param lMatchday the specific match day.
	 * @param lLeague the football league.
	 * @param lYear the football season.
	 * 
	 * Example: 27, "bl1", "2015" 
	 */			
	public void initFixture(int lMatchDay, String lLeague, String lYear){
		Match [] lMatches = LigaWService.parseFootballData(lMatchDay, lLeague, lYear);
		
		Map<TeamIDEnum, TeamModel> map = mModel.getTeams();

		for(Match lMatch : lMatches)
			mView.getFixturePanel().createFixture(map.get(TeamIDEnum.getType(lMatch.getTeam1())), 
					map.get(TeamIDEnum.getType(lMatch.getTeam2())), 
					lMatch.getScore1(), lMatch.getScore2());		
	}
	
	/**
	 * Add Listener
	 */	
	private void addListener() {
		mView.setButtonExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.dispose();
			}
		});
		
		// Observer pattern to trigger this.actionUpdateConsole from the subcontroller
		for(ISubController lController : mSubController)
			lController.setUpdateListener(this);
	}	

	/**
	 * Action Events
	 */		
	public void actionUpdateConsole(String lMessage){
		mView.getConsolenPanel().setConsole(lMessage);
	}	
	
	/**
	 * Start App
	 */		
	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				mView.initView();
				addSubController(new TipicoActivityBean(new TipicoModel(), mView.getTipicoPanel()));
				addListener();
			}
		});
	}
}
