package de.types;

public enum FilterOperationType {
	LESS, EQUAL, GREATER, UNEQUAL;
	
	public static FilterOperationType getType(String pType) {
		if (pType.equals(LESS.toString())){
			return LESS;
		} else if (pType.equals(EQUAL.toString())) {
			return EQUAL;
		} else if (pType.equals(GREATER.toString())) {
			return GREATER;
		} else if (pType.equals(UNEQUAL.toString())) {
			return UNEQUAL;
		} 
		return null;
	}	
}
