package de.types;

public enum TipicoDataType{
	TEAM, BET, WINVALUE, EXPENSES, DATE;
	
	public static TipicoDataType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return TEAM;
		case 1 : return BET;
		case 2 : return WINVALUE;
		case 3 : return EXPENSES;
		case 4 : return DATE;
		default : return null;
		}
	}
	
	public static int getSize(){
		return values().length;
	}
}
