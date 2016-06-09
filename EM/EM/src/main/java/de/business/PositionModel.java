package de.business;

public class PositionModel {
	private int mPlace;
	private int mPoints;
	
	public PositionModel(){
		this(0,0);
	}
	
	public PositionModel(int pPlace, int pPoints){
		this.mPlace = pPlace;
		this.mPoints = pPoints;
	}
	
	public int getPlace() {
		return mPlace;
	}

	public void setPlace(int pPlace) {
		this.mPlace = pPlace;
	}

	public int getPoints() {
		return mPoints;
	}

	public void setPoints(int pPoints) {
		this.mPoints = pPoints;
	}	
}
