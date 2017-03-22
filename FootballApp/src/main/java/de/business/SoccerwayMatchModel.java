package de.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SoccerwayMatchModel {
	private String mTeamID;
	private String mDay;
	private LocalDate mDate;
	private String mCompetition;
	private String mTeam1;
	private String mTeam2;
	private String mResult;
	private boolean mAccept;

	public SoccerwayMatchModel(String pTeamID, String pDay, LocalDate pDate, String pCompetition, String pTeam1, String pTeam2, String pResult, boolean pAccept) {
		mTeamID = pTeamID;
		mDay = pDay;
		mDate = pDate;
		mCompetition = pCompetition;
		mTeam1 = pTeam1;
		mTeam2 = pTeam2;
		mResult = pResult;
		mAccept = pAccept;
	}

	public SoccerwayMatchModel(String pTeamID) {
		this(pTeamID, null, LocalDate.MIN, null, null, null, null, false);
	}	
	
	public SoccerwayMatchModel() {
		this(null,null, LocalDate.MIN, null, null, null, null, false);
	}

	public String getTeamID(){
		return mTeamID;
	}

	public void setTeamID(String pTeamID){
		this.mTeamID = pTeamID;
	}	
	
	public String getDay() {
		return mDay;
	}

	public void setDay(String pDay) {
		this.mDay = pDay;
	}

	public LocalDate getDate() {
		return mDate;
	}

	public void setDate(LocalDate pDate) {
		this.mDate = pDate;
	}

	public void setDate(String pDate){
		if (!pDate.equals("") && pDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
			this.mDate = LocalDate.parse(pDate, formatter);
		}
	}

	public String getCompetition() {
		return mCompetition;
	}

	public void setCompetition(String pCompetition) {
		this.mCompetition = pCompetition;
	}

	public String getTeam1() {
		return mTeam1;
	}

	public void setTeam1(String pTeam1) {
		this.mTeam1 = pTeam1;
	}

	public String getTeam2() {
		return mTeam2;
	}

	public void setTeam2(String pTeam2) {
		this.mTeam2 = pTeam2;
	}

	public String getResult() {
		return mResult;
	}

	public void setResult(String pResult) {
		this.mResult = pResult;
	}
	
	public boolean getAccept(){
		return mAccept;
	}
	
	public void setAccept(boolean pAccept){
		this.mAccept = pAccept;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(mTeamID).append(' ').append(mDay).append(' ').append(mDate).append(' ').append(mCompetition).append(' ').append(mTeam1).append(' ').append(mResult).append(' ').append(mTeam2).toString();
	}
}
