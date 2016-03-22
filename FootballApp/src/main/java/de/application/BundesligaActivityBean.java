package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import de.business.BundesligaModel;
import de.business.Match;
import de.business.OpenLigaDBModel;
import de.business.TipicoModel;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;

public class BundesligaActivityBean {
	private BundesligaModel mModel;
	private BundesligaView mView;
	private TipicoActivityBean mSubController;
	
	public BundesligaActivityBean(BundesligaModel pModel, BundesligaView pView) {
		this.mModel = pModel;
		this.mView = pView;
	}
	
	public void addSubController(){
		this.mSubController = new TipicoActivityBean(new TipicoModel(), mView.getTipicoPanel());		
	}

	// 27, "bl1", "2015"
	public void initFixture(int lMatchDay, String lLeague, String lYear){
		Match [] lMatches = OpenLigaDBModel.parseFootballData(lMatchDay, lLeague, lYear);
		
		Map<TeamIDEnum, TeamModel> map = mModel.getTeams();

		for(Match lMatch : lMatches)
			mView.getFixturePanel().createFixture(map.get(TeamIDEnum.getType(lMatch.getTeam1())), 
					map.get(TeamIDEnum.getType(lMatch.getTeam2())), 
					lMatch.getScore1(), lMatch.getScore2());		
	}
	
	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				mView.initView();
				addSubController();
				addListener();
				
			}
		});
	}
	
	private void addListener() {
		mView.setButtonExitListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mView.dispose();
			}
		});
		
		mSubController.setUpdateListener(this);
	}
	
	public void actionUpdateConsole(String lMessage){
		mView.getConsolenPanel().setConsole(lMessage);
	}
}
