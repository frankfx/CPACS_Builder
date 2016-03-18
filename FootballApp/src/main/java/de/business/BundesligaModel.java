package de.business;

import java.util.HashMap;
import java.util.Map;

import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;


public class BundesligaModel {
	
	private Map<TeamIDEnum, TeamModel> mTeams = new HashMap<TeamIDEnum, TeamModel>();

	public BundesligaModel(){
		mTeams.put(TeamIDEnum.BVB, new TeamModel("Borussia Dortmund", "dortmund.png"));
		mTeams.put(TeamIDEnum.FCB, new TeamModel("Bayern Muenchen", "bayern.png"));
		mTeams.put(TeamIDEnum.Hertha, new TeamModel("Hertha BSC", "berlin.gif"));
		mTeams.put(TeamIDEnum.BMG, new TeamModel("Gladbach", "gladbach.gif"));
		mTeams.put(TeamIDEnum.S04, new TeamModel("Schalke 04", "schalke.gif"));
		mTeams.put(TeamIDEnum.FSV, new TeamModel("Mainz", "dortmund.png"));
		mTeams.put(TeamIDEnum.LEV, new TeamModel("Leverkusen", "leverkusen.gif"));
		mTeams.put(TeamIDEnum.VFL, new TeamModel("Wolfsburg", "wolfsburg.gif"));
		mTeams.put(TeamIDEnum.FCK, new TeamModel("Koeln", "dortmund.png"));
		mTeams.put(TeamIDEnum.Ingol, new TeamModel("Ingolstadt", "dortmund.png"));
		mTeams.put(TeamIDEnum.VFB, new TeamModel("Stuttgart", "stuttgart.gif"));
		mTeams.put(TeamIDEnum.HSV, new TeamModel("Hamburg", "hamburg.png"));
		mTeams.put(TeamIDEnum.FCA, new TeamModel("Augsburg", "augsburg.gif"));
		mTeams.put(TeamIDEnum.D89, new TeamModel("Darmstadt", "dortmund.png"));
		mTeams.put(TeamIDEnum.SVW, new TeamModel("Bremen", "bremen.png"));
		mTeams.put(TeamIDEnum.EINTRACHT, new TeamModel("Frankfurt", "frankfurt.png"));
		mTeams.put(TeamIDEnum.TSG, new TeamModel("Hoffenheim", "hoffenheim.gif"));
		mTeams.put(TeamIDEnum.H96, new TeamModel("Hannover", "hannover.gif"));
	}
	
	public Map<TeamIDEnum, TeamModel> getTeams() {
		return mTeams;
	}

	public void setTeams(Map<TeamIDEnum, TeamModel> teams) {
		this.mTeams = teams;
	}
}
