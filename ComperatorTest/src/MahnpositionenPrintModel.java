import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MahnpositionenPrintModel {

	private static final String MAHNBELEGART = "mahnbelegart";
	private static final String TYP = "typ";

	private Sortierreihenfolge mSortierreihenfolge = Sortierreihenfolge.MAHNBELEGART;

	private Mahnung mMahnung;

	public MahnpositionenPrintModel(Mahnung pMahnung) {
		mMahnung = pMahnung;
	}
	
	
	public static enum Sortierreihenfolge {
		MAHNBELEGART, TYP;
	}


	public Iterator<ForderungVerbindlichkeitModel> retrieveListDetailIterator() {
		Iterator<ForderungVerbindlichkeitModel> result = null;
		List<ForderungVerbindlichkeitModel> lForderungen = new ArrayList<ForderungVerbindlichkeitModel>(this.getForderungenFuerZuDruckendePositionen(mMahnung));
		
		final Comparator<ForderungVerbindlichkeitModel> lCompositeComparator;
		
		final Comparator<ForderungVerbindlichkeitModel> lTypComparator = new Comparator<ForderungVerbindlichkeitModel>() {
			public int compare(final ForderungVerbindlichkeitModel o1, final ForderungVerbindlichkeitModel o2) {
				boolean o1IstBescheidet = o1.hatBescheideteMahnung();
				boolean o2IstBescheidet = o2.hatBescheideteMahnung();
			
					int result = 0;

					// sortiere offene nach vorn
					if (!o1IstBescheidet && o2IstBescheidet)
						result = -1;
					else if (o1IstBescheidet && !o2IstBescheidet)
						result = 1;
					else
						result = 0;

				boolean o1IstSZVZ = o1.getMahnbelegart().equals(MahnBelegartType.SAEUMNISZUSCHLAG) || o1.getMahnbelegart().equals(MahnBelegartType.VERSPAETUNGSZUSCHLAG);
				boolean o2IstSZVZ = o2.getMahnbelegart().equals(MahnBelegartType.SAEUMNISZUSCHLAG) || o2.getMahnbelegart().equals(MahnBelegartType.VERSPAETUNGSZUSCHLAG);

				if (o1IstSZVZ && !o1.getMahnungen().isEmpty() && o2IstSZVZ && !o2.getMahnungen().isEmpty()) {
					result = 0;
				} else if (o1IstSZVZ && !o1.getMahnungen().isEmpty()) {
					result = 1;
				} else if (o2IstSZVZ && !o2.getMahnungen().isEmpty()) {
					result = -1;
				}


				return result;
			}
		};
		lCompositeComparator = new CompositeComparator<ForderungVerbindlichkeitModel>(
				lTypComparator,
				CompositeComparator.sFORDERUNGVERBINDLICHKEIT_MAHNBELEGART_COMPARATOR);

		java.util.Collections.sort(lForderungen, lCompositeComparator);
		result = lForderungen.iterator();
		// USER SECTION END retrieveListDetailIterator~void
		// @formatter:off
		return result;
	}

	protected Collection<ForderungVerbindlichkeitModel> getForderungenFuerZuDruckendePositionen(Mahnung m) {
		return m.getAlleForderungenUndGebuehrenZurMahnung();
	}
}
