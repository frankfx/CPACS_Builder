package de.types;

public enum TipicoDataType{
	ID, TEAM, BET, WINVALUE, EXPENSES, ATTEMPTS, DATE, SUCCESS;
	
	public static TipicoDataType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return ID;
		case 1 : return TEAM;
		case 2 : return BET;
		case 3 : return WINVALUE;
		case 4 : return EXPENSES;
		case 5 : return ATTEMPTS;
		case 6 : return DATE;
		case 7 : return SUCCESS;
		default : return null;
		}
	}
	
	public static int getSize(){
		return values().length;
	}
}
