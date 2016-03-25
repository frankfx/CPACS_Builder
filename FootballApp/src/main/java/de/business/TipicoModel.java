package de.business;

import java.sql.Date;
import java.time.LocalDate;

import de.utils.PersistenceType;

public class TipicoModel {
	private int mTnr;
	private String mTeam;
	private float mWinValue;
	private float mExpenses;
	private int mAttempts;
	private LocalDate mDate;
	private boolean mSuccess;
	private PersistenceType mPersistantType;
	
	public int getTnr() {
		return mTnr;
	}
	public void setTnr(int pTnr) {
		this.mTnr = pTnr;
	}
	public String getTeam() {
		return mTeam;
	}
	public void setTeam(String team) {
		this.mTeam = team;
	}
	public float getWinValue() {
		return mWinValue;
	}
	public void setWinValue(float pWinValue) {
		this.mWinValue = pWinValue;
	}
	public float getExpenses() {
		return mExpenses;
	}
	public void setExpenses(float pExpenses) {
		this.mExpenses = pExpenses;
	}
	public int getAttempts() {
		return mAttempts;
	}
	public void setAttempts(int lAttempts) {
		this.mAttempts = lAttempts;
	}
	public LocalDate getDate() {
		return mDate;
	}
	public Date getSQLDate(){
		return Date.valueOf(mDate);
	}
	
	public void setDate(LocalDate lDate) {
		this.mDate = lDate;
	}
	
	public void setDate(Date lDate){
		this.mDate = lDate.toLocalDate();
	}
	
	public boolean getSuccess() {
		return mSuccess;
	}
	
	public void setSuccess(boolean lSuccess) {
		this.mSuccess = lSuccess;
	}
	
	public PersistenceType getPersistantType() {
		return mPersistantType;
	}
	
	public void setPersistantType(PersistenceType pPersistantType) {
		this.mPersistantType = pPersistantType;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.mTnr == ((TipicoModel)obj).getTnr();
	};
	
	@Override
	public String toString(){
		return "[" + mTnr + ", " + mTeam + ", " + mWinValue + ", " + mExpenses + ", " + mAttempts + ", " + mDate + ", " + mSuccess + "]" ;
	}
}
