package de.types;

public enum FilterConnectionType {
	AND, OR, NOT, EMPTY;
	
	public static FilterConnectionType getType(String pType) {
		if (pType.equals(AND.toString())){
			return AND;
		} else if (pType.equals(OR.toString())) {
			return OR;
		} else if (pType.equals(NOT.toString())) {
			return NOT;
		}
		return EMPTY;
	}	
}
