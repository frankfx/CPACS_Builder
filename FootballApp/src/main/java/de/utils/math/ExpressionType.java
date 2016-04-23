package de.utils.math;

public enum ExpressionType {
	AND, OR, EXPENSES, WINVALUE, ID, LOWER, GREATER, NUMBER, VALUE;

	public static ExpressionType getType(String pType) {

		if (pType.equalsIgnoreCase(AND.toString())) {
			return AND;
		} else if (pType.equalsIgnoreCase(OR.toString())) {
			return OR;
		} else if (pType.equalsIgnoreCase(EXPENSES.toString())) {
			return EXPENSES;
		} else if (pType.equalsIgnoreCase(WINVALUE.toString())) {
			return WINVALUE;
		} else if (pType.equalsIgnoreCase(ID.toString())) {
			return ID;
		} else if (pType.equalsIgnoreCase("<")) {
			return LOWER;
		} else if (pType.equalsIgnoreCase(">")) {
			return GREATER;
		} else if (pType.matches("\\d+")) {
			return NUMBER;
		} else if (pType.matches(".*\\d+.*")) {
			return VALUE;
		} else {
			return null;
		}
	}
}
