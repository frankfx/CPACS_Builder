package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import de.business.BundesligaModel;
import de.business.TipicoModel;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;
import de.presentation.popups.Popup;

public class BundesligaActivityBean {
	private BundesligaModel mModel;
	private BundesligaView mView;
	
	private TipicoActivityBean mSubController;
	
	public BundesligaActivityBean(BundesligaModel pModel, BundesligaView pView) {
		this.mModel = pModel;
		this.mView = pView;
	}
	
	public void addSubController(){
		this.mSubController = new TipicoActivityBean(new TipicoModel(), mView.mTipicoPanel);		
		Map<TeamIDEnum, TeamModel> map = mModel.getTeams();
		mView.mFixturePanel.createFixture(map.get(TeamIDEnum.BVB), map.get(TeamIDEnum.FCB), "1", "0");
		mView.mFixturePanel.createFixture(map.get(TeamIDEnum.FCA), map.get(TeamIDEnum.VFB), "3", "1");	
	}
	
	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				mView.initView();
				addListener();
				addSubController();
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
	}
}
