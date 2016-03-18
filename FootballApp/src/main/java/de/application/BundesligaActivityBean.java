package de.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import de.business.BundesligaModel;
import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;
import de.presentation.bundesliga.BundesligaView;

public class BundesligaActivityBean {
	BundesligaModel model;
	BundesligaView view;
	
	public BundesligaActivityBean(BundesligaModel model, BundesligaView view) {
		this.model = model;
		this.view = view;
	}
	
	public void initView(){
		Map<TeamIDEnum, TeamModel> map = model.getTeams();
		view.mFixturePanel.createFixture(map.get(TeamIDEnum.BVB), map.get(TeamIDEnum.FCB), "1", "0");
		view.mFixturePanel.createFixture(map.get(TeamIDEnum.FCA), map.get(TeamIDEnum.VFB), "3", "1");
	}
	

	public void runApp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				view.initView();
				addListener();
				initView();
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
