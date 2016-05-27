package de.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserService {

	//create an object of SingleObject
	private static ParserService instance = new ParserService();

	//make the constructor private so that this class cannot be instantiated
	private ParserService() {
	}

	//Get the only object available
	public static ParserService getInstance() {
		return instance;
	}


	private boolean isWellFormedListString(String pListString) {
		Stack<Character> stack = new Stack<Character>();
		char cur;

		for (int i = 0; i < pListString.length(); i++) {
			cur = pListString.charAt(i);
			if (cur == '[') {
				stack.push(cur);
			} else if (cur == ']') {
				if (stack.isEmpty())
					return false;
				else
					stack.pop();
			}
		}
		return stack.isEmpty();
	}

	/**
	 * Parses a String which represents a list of String lists to a java list of
	 * Strings
	 * 
	 * @param pListString
	 *            a String which represents a list of several String lists.
	 *            Format example: [[a1, a2, a3], [b1, b2], [c1], [d1, d2, d3]]
	 * @return a java list with all String lists as separate elements. Format:
	 *         example: ["[a1, a2, a3]", "[b1, b2, c1]", "[d1, d2, d3]"]
	 */
	public List<String> parseListString(String pListString) {

		List<String> lResult = new ArrayList<String>();

		if (pListString != null && isWellFormedListString(pListString)) {
			// remove first and last bracket
			String lText = pListString.substring(1, pListString.length() - 1);

			// match all between the inner brackets to a new string
			final Pattern PATTERN = Pattern.compile("\\[(.*?)\\]");
			final Matcher matcher = PATTERN.matcher(lText);

			// add the new strings to the result list
			while (matcher.find()) {
				lResult.add(matcher.group());
			}
		}
		return lResult;
	}

	public static void main(String[] args) {
		ParserService p = ParserService.getInstance();

		List<String> list = p
				.parseListString("[[a1, a2, a3], [b1, b2], [c1], [d1, d2,d3]]");

		for (String s : list)
			System.out.println(s);
	}
}
