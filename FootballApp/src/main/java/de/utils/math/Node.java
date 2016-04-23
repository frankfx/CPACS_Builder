package de.utils.math;


public class Node<T> {
	private Node<IExpression> mLeftChild;
	private Node<IExpression> mRightChild;
	private String mData;
	
	public Node(Node<IExpression> pLeftChild, Node<IExpression> pRightChild, String pData) {
		mLeftChild = pLeftChild;
		mRightChild = pRightChild;
		mData = pData;
	}

	public Node<IExpression> getLeftChild() {
		return mLeftChild;
	}

	public Node<IExpression> getRightChild() {
		return mRightChild;
	}

	public String getData() {
		return mData;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(mData);
		sb.append(" ");
		sb.append(mLeftChild);
		sb.append(" ");
		sb.append(mRightChild);
		sb.append(")");
		
		return sb.toString();
	}
}
