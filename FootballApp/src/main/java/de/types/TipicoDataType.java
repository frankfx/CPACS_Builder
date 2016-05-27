package de.types;

public enum TipicoDataType {
	ID, TEAM, WINVALUE, EXPENSES, ATTEMPTS, DATE, SUCCESS;
	
	public static TipicoDataType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return ID;
		case 1 : return TEAM;
		case 2 : return WINVALUE;
		case 3 : return EXPENSES;
		case 4 : return ATTEMPTS;
		case 5 : return DATE;
		case 6 : return SUCCESS;
		default : return null;
		}
	}
	
	public static int getSize(){
		return values().length;
	}
}
