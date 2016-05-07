import java.util.Comparator;

public class MahnbelegartComparator implements Comparator<ForderungVerbindlichkeitModel> {

	@Override
	public int compare(ForderungVerbindlichkeitModel o1, ForderungVerbindlichkeitModel o2) {
		return o1.getMahnbelegart().toString().compareTo(o2.getMahnbelegart().toString());
	}
}
