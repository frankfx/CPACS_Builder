package de.types;

public enum SoccerwayMatchType {
	// must be lower case for soccerway url
	all, home, away;

	public static SoccerwayMatchType getType(String pType) {
		if (pType == null){
			return null;
		} else if (pType.equals(all.toString())){
			return all;
		} else if (pType.equals(home.toString())) {
			return home;
		} else if (pType.equals(away.toString())) {
			return away;
		}
		return null;
	}
}