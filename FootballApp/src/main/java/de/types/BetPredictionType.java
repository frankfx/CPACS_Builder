package de.types;


public enum BetPredictionType {
	DRAW, HOME_WIN, AWAY_WIN, FST_HOME_WIN, FST_AWAY_WIN, SND_HOME_WIN, SND_AWAY_WIN; 
	
	public static BetPredictionType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return DRAW;
		case 1 : return HOME_WIN;
		case 2 : return AWAY_WIN;
		case 3 : return FST_HOME_WIN;
		case 4 : return FST_AWAY_WIN;
		case 5 : return SND_HOME_WIN;
		case 6 : return SND_AWAY_WIN;
		default : return DRAW;
		}
	}
	
	public static BetPredictionType getType(String pType) {
		if (pType == null){
			return null;
		} else if (pType.equals(DRAW.toString())){
			return DRAW;
		} else if (pType.equals(HOME_WIN.toString())) {
			return HOME_WIN;
		} else if (pType.equals(AWAY_WIN.toString())) {
			return AWAY_WIN;
		} else if (pType.equals(FST_HOME_WIN.toString())) {
			return FST_HOME_WIN;
		} else if (pType.equals(FST_AWAY_WIN.toString())) {
			return FST_AWAY_WIN;
		} else if (pType.equals(SND_HOME_WIN.toString())) {
			return SND_HOME_WIN;
		} else if (pType.equals(SND_AWAY_WIN.toString())) {
			return SND_AWAY_WIN;
		} 
		return null;		
	}	
}
