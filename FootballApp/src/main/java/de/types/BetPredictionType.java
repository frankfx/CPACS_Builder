package de.types;


public enum BetPredictionType {
	WIN, LOSE, DRAW, FST_WIN, FST_LOSE, FST_DRAW, SND_WIN, SND_LOSE, SND_DRAW; 
	
	public static BetPredictionType getDataType(int pPosition) {
		switch(pPosition){
		case 0 : return WIN;
		case 1 : return LOSE;
		case 2 : return DRAW;
		case 3 : return FST_WIN;
		case 4 : return FST_LOSE;
		case 5 : return FST_DRAW;
		case 6 : return SND_WIN; 
		case 7 : return SND_LOSE;
		case 8 : return SND_DRAW;
		default : return DRAW;
		}
	}
	
	public static BetPredictionType getType(String pType) {
		if (pType == null){
			return null;
		} else if (pType.equals(DRAW.toString())){
			return DRAW;
		} else if (pType.equals(WIN.toString())) {
			return WIN;
		} else if (pType.equals(LOSE.toString())) {
			return LOSE;
		} else if (pType.equals(FST_DRAW.toString())) {
			return FST_DRAW;
		} else if (pType.equals(FST_WIN.toString())) {
			return FST_WIN;
		} else if (pType.equals(FST_LOSE.toString())) {
			return FST_LOSE;
		} else if (pType.equals(SND_DRAW.toString())) {
			return SND_DRAW;
		} else if (pType.equals(SND_WIN.toString())) {
			return SND_WIN;
		} else if (pType.equals(SND_LOSE.toString())) {
			return SND_LOSE;
		} 
		return null;		
	}	
}
