package de.types;

public enum SWDataType {
	DATE, COMPETITION, TEAM1, TEAM2, RESULT, ACCEPT;
	
	public static String getSWDataTypeFixturesTableDescription(int pPosition){
		switch(pPosition){
		case 0 : return getSWDataTypeTableDescription(DATE);
		case 1 : return getSWDataTypeTableDescription(RESULT);
		case 2 : return getSWDataTypeTableDescription(COMPETITION);
		case 3 : return getSWDataTypeTableDescription(TEAM1);
		case 4 : return getSWDataTypeTableDescription(TEAM2);
		default : return null;
		}		
	}

	public static String getSWDataTypeResultsTableDescription(int pPosition){
		switch(pPosition){
		case 0 : return getSWDataTypeTableDescription(DATE);
		case 1 : return getSWDataTypeTableDescription(COMPETITION);
		case 2 : return getSWDataTypeTableDescription(TEAM1);
		case 3 : return getSWDataTypeTableDescription(TEAM2);
		case 4 : return getSWDataTypeTableDescription(RESULT);
		case 5 : return getSWDataTypeTableDescription(ACCEPT);
		default : return null;
		}		
	}	

	private static String getSWDataTypeTableDescription(SWDataType pDataType){
		switch(pDataType){
		case DATE : return "Date";
		case COMPETITION : return "Comp.";
		case TEAM1 : return "Team 1";
		case TEAM2 : return "Team 2";
		case RESULT : return "Result";
		case ACCEPT : return "Accept";
		default : return null;
		}		
	}	
	
	public static int getSize(){
		return values().length;
	}
}
