package de.business;

import java.time.LocalDate;

public class TipicoModel {
	private int mTnr;
	private String mTeam;
	private float mWinValue;
	private float mExpenses;
	private float mBet;
	private float mProfit;
	private LocalDate mNextGame;
	
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
	public float getBet() {
		return mBet;
	}
	public void setBet(float pBet) {
		this.mBet = pBet;
	}
	public float getProfit() {
		return mProfit;
	}
	public void setProfit(float pProfit) {
		this.mProfit = pProfit;
	}	
	public LocalDate getNextGame() {
		return mNextGame;
	}
	public void setNextGame(LocalDate pNextGame) {
		this.mNextGame = pNextGame;
	}	
	
	@Override
	public String toString(){
		return "[" + mTnr + ", " + mTeam + ", " + mWinValue + ", " + mExpenses + ", " + mBet + ", " + mProfit + "]" ;
	}
}
