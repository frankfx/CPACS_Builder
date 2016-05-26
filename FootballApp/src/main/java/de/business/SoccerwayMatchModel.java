package de.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SoccerwayMatchModel {
	private String mDay;
	private LocalDate mDate;
	private String mCompetition;
	private String mTeam1;
	private String mTeam2;
	private String mResult;

	public SoccerwayMatchModel(String pDay, LocalDate pDate, String pCompetition, String pTeam1, String pTeam2, String pResult) {
		mDay = pDay;
		mDate = pDate;
		mCompetition = pCompetition;
		mTeam1 = pTeam1;
		mTeam2 = pTeam2;
		mResult = pResult;
	}
	
	public SoccerwayMatchModel() {
		this(null, LocalDate.MIN, null, null, null, null);
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		this.mDate = LocalDate.parse(pDate, formatter);
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
	
	@Override
	public String toString() {
		return new StringBuilder().append(mDay).append(' ').append(mDate).append(' ').append(mCompetition).append(' ').append(mTeam1).append(' ').append(mResult).append(' ').append(mTeam2).toString();
	}
}
