package copy;

import java.math.BigDecimal;

public class Forderungen {
	
	BigDecimal mBetrag;
	BigDecimal mANBetrag;
	
	public Forderungen (BigDecimal a, BigDecimal b) {
		this.mBetrag = a;
		this.mANBetrag = b;
	}
	
	public BigDecimal getBetrag() {
		return mBetrag;
	}
	public void setBetrag(BigDecimal mBetrag) {
		this.mBetrag = mBetrag;
	}
	public BigDecimal getANBetrag() {
		return mANBetrag;
	}
	public void setANBetrag(BigDecimal mANBetrag) {
		this.mANBetrag = mANBetrag;
	}
	public String toString() {
		return mBetrag.toString() + " -- " + mANBetrag.toString();
	}
	
	@Override
	protected Forderungen clone() {
		return new Forderungen(this.mBetrag, this.mANBetrag);
	}
}
