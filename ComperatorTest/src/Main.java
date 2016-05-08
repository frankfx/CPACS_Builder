import java.util.Iterator;

public class Main {
	public static void main(String[] args) {
		
		Mahnung mahnung1 = new Mahnung(MahnStatus.BESCHEIDET);
		Mahnung mahnung2 = new Mahnung(MahnStatus.OFFEN);
		Mahnung mahnung3 = new Mahnung(MahnStatus.OFFEN);

		ForderungVerbindlichkeitModel[] ford1 = getRadomForderungen(4);
		ForderungVerbindlichkeitModel[] ford2 = getRadomForderungen(5);
		ForderungVerbindlichkeitModel[] ford3 = getRadomForderungen(12);
		
		// fill mahnung1
		for (ForderungVerbindlichkeitModel f : ford1) {
			mahnung1.addForderungen(f);
			f.addMahnung(mahnung1);
		}

		// fill mahnung2
		for (ForderungVerbindlichkeitModel f : ford2) {
			mahnung2.addForderungen(f);
			f.addMahnung(mahnung2);
		}

		// fill mahnung3 with two values of mahnung2
		for (ForderungVerbindlichkeitModel f : ford2) {
			mahnung3.addForderungen(f);
		}

		// fill mahnung3 with two values of mahnung1
		for (ForderungVerbindlichkeitModel f : ford1) {
			mahnung3.addForderungen(f);
		}

		// fill mahnung3
		for (ForderungVerbindlichkeitModel f : ford3) {
			mahnung3.addForderungen(f);
		}

		Iterator<ForderungVerbindlichkeitModel> iter = new MahnpositionenPrintModel(mahnung3).retrieveListDetailIterator();

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	public static ForderungVerbindlichkeitModel [] getRadomForderungen (int n){
		ForderungVerbindlichkeitModel[] fordM1 = new ForderungVerbindlichkeitModel[n];
		
		for (int i = 0; i < n; i++) {
			fordM1[i] = new ForderungVerbindlichkeitModel(MahnBelegartType.getRandomMahnbelegart());
		}
		
		return fordM1;
	}
}
