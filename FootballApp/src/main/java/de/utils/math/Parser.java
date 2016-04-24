package de.utils.math;

import java.util.Stack;

public class Parser {

	private Node<IExpression> mExpression;
	private Stack<Character> mStack;

	public Parser(String pExpression) {
		try {
			mStack = new Stack<Character>();
			mExpression = this.buildTree(pExpression.replace(" ", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Node<IExpression> buildTree(String pExpression) throws Exception {

		String lExpression = pExpression.substring(1, pExpression.length() - 1);

		int lStart = -1, lEnd = -1;
		boolean isValueExp = true;
		
		for (int i = 0; i < lExpression.length(); i++) {
			switch (lExpression.charAt(i)) {
			case '(':
				isValueExp = false;
				mStack.push('(');
				break;
			case ')':
				if (mStack.isEmpty() || mStack.peek() != '(')
					throw new Exception("Expression not valid");
				else
					mStack.pop();
				break;
			default:
				break;
			}

			if (mStack.isEmpty()) {
				if (isValueExp) {
					return new Node<IExpression>(null, null, lExpression);
				} else {
					lStart = i + 1;
					lEnd = lExpression.indexOf('(', lStart);
					return new Node<IExpression>(
							buildTree(lExpression.substring(0, lStart)),
							buildTree(lExpression.substring(lEnd)),
							lExpression.substring(lStart, lEnd));

				}
			}
		}

		throw new Exception("Expression not valid");
	}

	public Node<IExpression> getExpressionTree() {
		return mExpression;
	}

	public static void main(String[] args) {
		String example = "(((EXPENSES;<;1) AND (WINVALUE;<;1)) AND (ID;<;5))";
		
		Parser parser = new Parser(example);
		System.out.println(parser.getExpressionTree());
	}
}
