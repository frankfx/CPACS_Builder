import java.util.ArrayList;
import java.util.List;

public class ForderungVerbindlichkeitModel {

	private List<Mahnung> list = new ArrayList<Mahnung>();
	private MahnBelegartType mMahnbelegart;

	public ForderungVerbindlichkeitModel(MahnBelegartType pMahnbelegart) {
		mMahnbelegart = pMahnbelegart;
	}

	public void addMahnung(Mahnung pMahnung) {
		list.add(pMahnung);
	}

	public List<Mahnung> getMahnungen() {
		return list;
	}

	public MahnBelegartType getMahnbelegart() {
		return mMahnbelegart;
	}

	public boolean hatBescheideteMahnung() {
		for (Mahnung m : list)
			if (m.getMahnstatus().equals(MahnStatus.BESCHEIDET))
				return true;
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Mahnbelegart: ");
		sb.append(mMahnbelegart.name());
		sb.append(",  Hat bescheidete Mahnung: ");
		sb.append(hatBescheideteMahnung());

		for (Mahnung m : list) {
			sb.append(",  ");
			sb.append(m);
		}

		return sb.toString();
	}
}
