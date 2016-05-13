package zahlungsverkehr.business.util;

import java.util.Date;

import baseutil.util.DateUtil;

/**
 * Diese einfache Klasse represäntiert ein Intervall. Das Intervall kann zu beiden seiten offen sein, d.h. Von oder Bis ist null.
 * 
 * @author Tim Benthaus
 * @version 2.11.2006
 */
public class Intervall {
	private Date mVon = null;
	private Date mBis = null;

	public Intervall(Date von, Date bis) {
		super();
		this.mVon = von;
		this.mBis = bis;
	}

	public Date getBis() {
		return this.mBis;
	}

	public Date getVon() {
		return this.mVon;
	}

	/**
	 * Ermittelt das älteste Datum. Die Parameter können null sein.
	 * 
	 * @param pDatum1
	 *            das erste Datum
	 * @param pDatum2
	 *            das zweite Datum
	 * @return das älteste Datum, oder null wenn Datum1 und Datum2 null;
	 */
	public static Date getMinDate(Date pDatum1, Date pDatum2) {
		if (pDatum1 == null)
			return pDatum2;
		if (pDatum2 == null)
			return pDatum1;
		if (pDatum1.before(pDatum2))
			return pDatum1;
		return pDatum2;
	}

	/**
	 * Ermittelt das jüngste Datum. Die Parameter können null sein.
	 * 
	 * @param pDatum1
	 *            das erste Datum
	 * @param pDatum2
	 *            das zweite Datum
	 * @return das jüngste Datum, oder null wenn Datum1 und Datum2 null;
	 */
	public static Date getMaxDate(Date pDatum1, Date pDatum2) {
		if (pDatum1 == null)
			return pDatum2;
		if (pDatum2 == null)
			return pDatum1;
		if (pDatum1.after(pDatum2))
			return pDatum1;
		return pDatum2;
	}

	public boolean istDatumInnerhalbIntervall(Date date) {
		return DateUtil.isIncludedIn(date, date, this.mVon, this.mBis);
	}

	public boolean istDatumVorIntervall(Date date) {
		return date.before(this.mVon);
	}

	public boolean istDatumNachIntervall(Date date) {
		return date.after(this.mBis);
	}
}