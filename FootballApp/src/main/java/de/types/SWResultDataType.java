package de.types;

public enum SWResultDataType {
	DATE, COMPETITION, TEAM1, TEAM2, RESULT, ACCEPT;
	
	public static SWResultDataType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return DATE;
		case 1 : return COMPETITION;
		case 2 : return TEAM1;
		case 3 : return TEAM2;
		case 4 : return RESULT;
		case 5 : return ACCEPT;
		default : return null;
		}
	}
	
	public static int getSize(){
		return values().length;
	}
}
