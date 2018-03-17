package de.types;

public enum FilterConnectionType {
	AND, OR, EMPTY;
	
	public static FilterConnectionType getType(String pType) {
		if (pType.equals(AND.toString())){
			return AND;
		} else if (pType.equals(OR.toString())) {
			return OR;
		} 
		return EMPTY;
	}	
}
