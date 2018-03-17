package de.utils;

public class Tupel<T, V> {

	private final T mFirst;
	private final V mSecond;

	public Tupel(final T pFirst, final V pSecond) {
		super();
		this.mFirst = pFirst;
		this.mSecond = pSecond;
	}

	public final T getFirst() {
		return mFirst;
	}

	public final V getSecond() {
		return mSecond;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tupel<?, ?>)) {
			return false;
		}
		Tupel<?, ?> lTupel = (Tupel<?, ?>) obj;
		
		return mFirst.equals(lTupel.mFirst) && mSecond.equals(lTupel.mSecond);
	}
	
	public static void main(String[] args) {
		//Tupel<String, Integer> t = new Tupel<String, Integer>("sdf", 1);
	}
}
