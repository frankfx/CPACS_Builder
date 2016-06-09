package de.business;

import de.business.teams.TeamModel;

public class MatchModel {
	private int mId;
	private TeamModel mTeam1;
	private TeamModel mTeam2;
	private int mScore1;
	private int mScore2;

	// For Webservice LigaWService
	public MatchModel() {
	}

	public MatchModel(int pId, TeamModel pTeam1, TeamModel pTeam2, int pScore1, int pScore2){
		mId = pId;
		mTeam1 = pTeam1;
		mTeam2 = pTeam2;
		mScore1 = pScore1;
		mScore2 = pScore2;
	}

	public int getId() {
		return mId;
	}

	public void setId(int pId) {
		this.mId = pId;
	}	
	
	public TeamModel getTeam1() {
		return mTeam1;
	}

	public void setTeam1(TeamModel pTeam1) {
		this.mTeam1 = pTeam1;
	}

	public TeamModel getTeam2() {
		return mTeam2;
	}

	public void setTeam2(TeamModel pTeam2) {
		this.mTeam2 = pTeam2;
	}

	public int getScore1() {
		return mScore1;
	}

	public void setScore1(int pScore1) {
		this.mScore1 = pScore1;
	}

	public int getScore2() {
		return mScore2;
	}

	public void setScore2(int pScore2) {
		this.mScore2 = pScore2;
	}
	
	public String toString(){
		return "[" + getTeam1() + ", " + getTeam2() + "]";
	}
}
