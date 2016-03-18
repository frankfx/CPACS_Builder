package de.business;

public class Match {
	private int mId;
	private String mTeam1;
	private String mTeam2;
	private int mScore1;
	private int mScore2;

	public Match(int pId, String pTeam1, String pTeam2, int pScore1, int pScore2){
		mId = pId;
		mTeam1 = pTeam1;
		mTeam2 = pTeam2;
		mScore1 = pScore1;
		mScore2 = pScore2;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}	
	
	public String getmTeam1() {
		return mTeam1;
	}

	public void setmTeam1(String mTeam1) {
		this.mTeam1 = mTeam1;
	}

	public String getmTeam2() {
		return mTeam2;
	}

	public void setmTeam2(String mTeam2) {
		this.mTeam2 = mTeam2;
	}

	public int getmScore1() {
		return mScore1;
	}

	public void setmScore1(int mScore1) {
		this.mScore1 = mScore1;
	}

	public int getmScore2() {
		return mScore2;
	}

	public void setmScore2(int mScore2) {
		this.mScore2 = mScore2;
	}

}
