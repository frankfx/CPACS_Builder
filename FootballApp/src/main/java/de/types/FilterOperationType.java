package de.types;

public enum FilterOperationType {
	LESS, EQUAL, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL, UNEQUAL;
	
	public static FilterOperationType getType(String pType) {
		if (pType.equals(LESS.toString())){
			return LESS;
		} else if (pType.equals(EQUAL.toString())) {
			return EQUAL;
		} else if (pType.equals(LESS_OR_EQUAL.toString())) {
			return LESS_OR_EQUAL;
		} else if (pType.equals(GREATER.toString())) {
			return GREATER;
		} else if (pType.equals(GREATER_OR_EQUAL.toString())) {
			return GREATER_OR_EQUAL;
		} else if (pType.equals(UNEQUAL.toString())) {
			return UNEQUAL;
		} 
		return null;
	}	
}
